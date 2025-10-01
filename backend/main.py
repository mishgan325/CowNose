from fastapi import FastAPI, File, UploadFile, HTTPException, Depends, Header
from fastapi.responses import JSONResponse, FileResponse, Response
from PIL import Image, ImageOps
import numpy as np
import torch
import torch.nn as nn
import torchvision.transforms as T
import torchvision.models as models
from ultralytics import YOLO
from sklearn.metrics.pairwise import cosine_similarity
import io
import os
import psycopg
import binascii
from typing import Optional
import re
try:
    import faiss
except ImportError:
    faiss = None

try:
    from dotenv import load_dotenv
    load_dotenv()
except Exception:
    pass

FIREBASE_ENABLED = False
try:
    import firebase_admin
    from firebase_admin import auth as fb_auth, credentials as fb_credentials
    service_account_path = os.getenv("FIREBASE_SERVICE_ACCOUNT_JSON")
    if not firebase_admin._apps:
        if service_account_path and os.path.exists(service_account_path):
            firebase_admin.initialize_app(fb_credentials.Certificate(service_account_path))
        else:
            firebase_admin.initialize_app()
    FIREBASE_ENABLED = True
except Exception:
    FIREBASE_ENABLED = False

DSN = {
    "host": os.getenv("DB_HOST", "localhost"),
    "port": int(os.getenv("DB_PORT", 5432)),
    "user": os.getenv("DB_USER", "postgres"),
    "password": os.getenv("DB_PASSWORD", "secret"),
    "dbname": os.getenv("DB_NAME", "postgres"),
    "connect_timeout": int(os.getenv("DB_TIMEOUT", 10))
}

app = FastAPI(title="Cow Nose Detector API", description="API for detecting cow noses and finding similar cows")

class ProjectionHead(nn.Module):
    def __init__(self, in_dim=2048, hidden_dim=512, out_dim=128):
        super().__init__()
        self.net = nn.Sequential(
            nn.Linear(in_dim, hidden_dim),
            nn.BatchNorm1d(hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, out_dim)
        )
    def forward(self, x):
        return nn.functional.normalize(self.net(x), dim=1)

class SimCLR(nn.Module):
    def __init__(self):
        super().__init__()
        self.backbone = models.resnet50(weights=models.ResNet50_Weights.DEFAULT)
        self.backbone.fc = nn.Identity()
        self.projector = ProjectionHead(2048)
    def forward(self, x):
        h = self.backbone(x)
        z = self.projector(h)
        return z

def load_yolo_model():
    return YOLO('best.pt')

def load_simclr_model():
    device = 'cuda' if torch.cuda.is_available() else 'cpu'
    model = SimCLR()
    checkpoint = torch.load('simclr_cow_model.pth', map_location=device)
    model.load_state_dict(checkpoint['model_state_dict'])
    model.eval()
    return model.to(device)

def load_embeddings():
    return np.load("cow_embeddings.npy", allow_pickle=True).item()

def get_embedding(model, image):
    device = next(model.parameters()).device
    transform = T.Compose([
        T.Resize(256),
        T.CenterCrop(224),
        T.ToTensor(),
        T.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
    ])
    img_tensor = transform(image).unsqueeze(0).to(device)
    with torch.no_grad():
        emb = model.backbone(img_tensor).squeeze().cpu().numpy()
        return emb / np.linalg.norm(emb)

def find_similar_cows(query_embedding, embeddings_db, top_k=3):
    query_vector = query_embedding.reshape(1, -1)
    names = list(embeddings_db.keys())
    all_vectors = np.stack([embeddings_db[name] for name in names])
    if faiss is not None:
        index = faiss.IndexFlatIP(all_vectors.shape[1])
        index.add(all_vectors.astype('float32'))
        D, I = index.search(query_vector.astype('float32'), top_k)
        return [{"name": names[idx], "similarity": float(D[0][i]), "image_url": f"/images/{names[idx]}"} for i, idx in enumerate(I[0])]
    else:
        sims = cosine_similarity(query_vector, all_vectors)[0]
        top_idxs = sims.argsort()[::-1][:top_k]
        return [{"name": names[idx], "similarity": float(sims[idx]), "image_url": f"/images/{names[idx]}"} for idx in top_idxs]

def convert_coordinates(xywh, img_width, img_height):
    x_center, y_center, w, h = xywh
    left = (x_center - w / 2) * img_width
    right = (x_center + w / 2) * img_width
    top = (y_center - h / 2) * img_height
    bottom = (y_center + h / 2) * img_height
    return int(left), int(top), int(right), int(bottom)

def _extract_cow_id(candidate: str) -> Optional[int]:
    if candidate is None:
        return None
    try:
        return int(candidate)
    except Exception:
        pass
    match = re.search(r"(\d+)", str(candidate))
    if match:
        try:
            return int(match.group(1))
        except Exception:
            return None
    return None

def find_cow_by_nfc_uid(nfc_uid_hex: str):
    try:
        nfc_uid = binascii.unhexlify(nfc_uid_hex.replace(" ", ""))
    except binascii.Error:
        raise ValueError("Invalid NFC UID format. Must be valid hex string.")
    with psycopg.connect(**DSN) as conn, conn.cursor() as cur:
        cur.execute("""
            SELECT c.cow_id, c.cow_name, c.last_milking_date, c.cow_pen
            FROM public.cow_nfc_tags n
            JOIN public.names_statistic c ON n.cow_id = c.cow_id
            WHERE n.nfc_uid = %s
        """, (nfc_uid,))
        result = cur.fetchone()
        if not result:
            return None
        cow_id, cow_name, last_milking_date, cow_pen = result
        cur.execute("""
            SELECT content, content_type, width, height
            FROM public.cow_images
            WHERE cow_id = %s
        """, (cow_id,))
        image_result = cur.fetchone()
        return {
            "cow_id": cow_id,
            "cow_name": cow_name,
            "last_milking_date": last_milking_date.isoformat() if last_milking_date else None,
            "cow_pen": cow_pen,
            "has_image": image_result is not None,
            "image_info": {
                "content_type": image_result[1] if image_result else None,
                "width": image_result[2] if image_result else None,
                "height": image_result[3] if image_result else None
            } if image_result else None
        }

yolo_model = load_yolo_model()
simclr_model = load_simclr_model()
embeddings_db = load_embeddings()
IMAGE_DIR = "cow_f_crop"

class AuthenticatedUser:
    def __init__(self, uid: str, token: dict):
        self.uid = uid
        self.token = token

def get_current_user(authorization: Optional[str] = Header(None)) -> Optional[AuthenticatedUser]:
    if not FIREBASE_ENABLED:
        return None     
    if not authorization or not authorization.lower().startswith("bearer "):
        raise HTTPException(status_code=401, detail="Missing or invalid Authorization header")
    id_token = authorization.split(" ", 1)[1].strip()
    try:
        decoded = fb_auth.verify_id_token(id_token)
        return AuthenticatedUser(uid=decoded.get("uid", ""), token=decoded)
    except Exception:
        raise HTTPException(status_code=401, detail="Invalid or expired token")

@app.post("/detect-and-search")
async def detect_and_search(file: UploadFile = File(...)):
    try:
        if file.content_type not in ["image/jpeg", "image/png"]:
            raise HTTPException(status_code=400, detail="Only JPEG or PNG images are supported")
        contents = await file.read()
        original_image = Image.open(io.BytesIO(contents)).convert("RGB")
        original_image = ImageOps.exif_transpose(original_image)
        img_array = np.array(original_image)
        results = yolo_model.predict(img_array)
        if len(results[0].boxes) == 0:
            raise HTTPException(status_code=404, detail="No nose detected in the image")
        xywhn = results[0].boxes.xywhn[0].cpu().numpy()
        img_width, img_height = original_image.size
        left, top, right, bottom = convert_coordinates(xywhn, img_width, img_height)
        cropped_nose = original_image.crop((left, top, right, bottom))
        nose_embedding = get_embedding(simclr_model, cropped_nose)
        similar_cows = find_similar_cows(nose_embedding, embeddings_db, top_k=3)
        top_match = similar_cows[0] if len(similar_cows) > 0 else None
        threshold = 0.7
        is_match = bool(top_match and top_match.get("similarity", 0.0) >= threshold)
        cow_name_from_db = None
        cow_id_from_key = _extract_cow_id(top_match["name"]) if top_match else None
        if is_match and cow_id_from_key is not None:
            try:
                with psycopg.connect(**DSN) as conn, conn.cursor() as cur:
                    cur.execute("SELECT cow_name FROM public.names_statistic WHERE cow_id = %s", (cow_id_from_key,))
                    row = cur.fetchone()
                    if row:
                        cow_name_from_db = row[0]
            except Exception:
                cow_name_from_db = None
        resolved_name = cow_name_from_db or (top_match["name"] if top_match else None)
        message = "не наша корова" if not is_match else f"Найдена корова: {resolved_name}"
        response = {
            "status": "success",
            "message": message,
            "is_match": is_match,
            "cow_name": resolved_name if is_match else None,
            "similarity": float(top_match["similarity"]) if top_match else None,
            "nose_coordinates": {
                "left": left,
                "top": top,
                "right": right,
                "bottom": bottom,
                "width": right - left,
                "height": bottom - top
            },
            "similar_cows": similar_cows,
            "database_size": len(embeddings_db),
            "embedding_size": nose_embedding.shape[0],
            "search_algorithm": "FAISS" if faiss else "Cosine Similarity"
        }
        return JSONResponse(content=response)
    except Exception as e:
        raise HTTPException(status_code=404, detail=f"Error processing image: {str(e)}")

def save_embedding_to_db(name, embedding, path="cow_embeddings.npy"):
    try:
        if os.path.exists(path):
            try:
                db = np.load(path, allow_pickle=True).item()
            except Exception as e:
                raise ValueError(f"Failed to load existing database: {str(e)}")
        else:
            db = {}
        if not isinstance(name, str) or not name.strip():
            raise ValueError("Name must be a non-empty string")
        if not isinstance(embedding, np.ndarray):
            raise ValueError("Embedding must be a numpy array")
        db[name] = embedding
        try:
            np.save(path, db)
        except Exception as e:
            raise IOError(f"Failed to save database to {path}: {str(e)}")
    except (ValueError, IOError) as e:
        raise Exception(f"Error saving embedding: {str(e)}")
    except Exception as e:
        raise Exception(f"Unexpected error while saving embedding: {str(e)}")

@app.post("/add-embedding")
async def add_embedding(file: UploadFile = File(...), name: str = None, user: Optional[object] = Depends(get_current_user)):
    global embeddings_db
    if not name:
        raise HTTPException(status_code=400, detail="Name is required")
    try:
        contents = await file.read()
        image = Image.open(io.BytesIO(contents)).convert("RGB")
        image = ImageOps.exif_transpose(image)
        img_array = np.array(image)
        results = yolo_model.predict(img_array)
        if len(results[0].boxes) == 0:
            raise HTTPException(status_code=404, detail="No nose detected in the image")
        xywhn = results[0].boxes.xywhn[0].cpu().numpy()
        img_width, img_height = image.size
        left, top, right, bottom = convert_coordinates(xywhn, img_width, img_height)
        cropped_nose = image.crop((left, top, right, bottom))
        embedding = get_embedding(simclr_model, cropped_nose)
        save_embedding_to_db(name, embedding)
        embeddings_db = load_embeddings()
        response = {
            "message": f"Embedding for {name} successfully added",
            "embedding_size": embedding.shape[0],
            "db_size": len(embeddings_db)
        }
        return JSONResponse(content=response)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/images/{image_name}")
async def get_image(image_name: str):
    image_path = os.path.join(IMAGE_DIR, image_name)
    if not os.path.exists(image_path):
        raise HTTPException(status_code=404, detail="Image not found")
    return FileResponse(image_path)

@app.get("/cows/{cow_id}/image")
async def get_cow_image(cow_id: int):
    with psycopg.connect(**DSN) as conn, conn.cursor() as cur:
        cur.execute("SELECT content, content_type FROM public.cow_images WHERE cow_id=%s", (cow_id,))
        row = cur.fetchone()
        if not row:
            raise HTTPException(status_code=404, detail="Image not found")
        data, mime = row
        return Response(content=data, media_type=mime)

@app.get("/nfc/{nfc_uid}")
async def get_cow_by_nfc(nfc_uid: str, user: Optional[object] = Depends(get_current_user)):
    try:
        cow_info = find_cow_by_nfc_uid(nfc_uid)
        if not cow_info:
            raise HTTPException(status_code=404, detail="Cow not found for this NFC UID")
        response = {
            "status": "success",
            "cow_id": cow_info["cow_id"],
            "cow_name": cow_info["cow_name"],
            "last_milking_date": cow_info["last_milking_date"],
            "cow_pen": cow_info["cow_pen"],
            "has_image": cow_info["has_image"],
            "image_url": f"/cows/{cow_info['cow_id']}/image" if cow_info["has_image"] else None,
            "image_info": cow_info["image_info"]
        }
        return JSONResponse(content=response)
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Database error: {str(e)}")

@app.get("/status")
async def health_check():
    return {"status": "OK", "message": "Cow Nose Detector API is running"}

@app.get("/farm_count")
async def get_farm_count():
    raw_value = os.getenv("FARM_COUNT", "3")
    try:
        value = int(raw_value)
    except ValueError:
        raise HTTPException(status_code=500, detail="FARM_COUNT must be an integer")
    return {"farm_count": value}
