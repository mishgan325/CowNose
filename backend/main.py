from fastapi import FastAPI, File, UploadFile, HTTPException
from fastapi.responses import JSONResponse, FileResponse
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
try:
    import faiss
except ImportError:
    faiss = None

app = FastAPI(title="Cow Nose Detector API", description="API for detecting cow noses and finding similar cows")

# Model classes (unchanged)
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

# Model loading functions
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

# Utility functions
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

# Load models and embeddings at startup
yolo_model = load_yolo_model()
simclr_model = load_simclr_model()
embeddings_db = load_embeddings()

# Directory for cow images
IMAGE_DIR = "cow_f_crop"

@app.post("/detect-and-search")
async def detect_and_search(file: UploadFile = File(...)):
    try:
        # Validate file type
        if file.content_type not in ["image/jpeg", "image/png"]:
            raise HTTPException(status_code=400, detail="Only JPEG or PNG images are supported")
        
        # Read and process image
        contents = await file.read()
        original_image = Image.open(io.BytesIO(contents)).convert("RGB")
        original_image = ImageOps.exif_transpose(original_image)
        img_array = np.array(original_image)
        
        # Detect nose
        results = yolo_model.predict(img_array)
        if len(results[0].boxes) == 0:
            raise HTTPException(status_code=404, detail="No nose detected in the image")
        
        # Get nose coordinates
        xywhn = results[0].boxes.xywhn[0].cpu().numpy()
        img_width, img_height = original_image.size
        left, top, right, bottom = convert_coordinates(xywhn, img_width, img_height)
        
        # Crop nose and get embedding
        cropped_nose = original_image.crop((left, top, right, bottom))
        nose_embedding = get_embedding(simclr_model, cropped_nose)
        
        # Find similar cows
        similar_cows = find_similar_cows(nose_embedding, embeddings_db, top_k=3)
        
        # Prepare response
        response = {
            "status": "success",
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

# Function to save new embedding
def save_embedding_to_db(name, embedding, path="cow_embeddings.npy"):
    try:
        # Check if file exists and is readable
        if os.path.exists(path):
            try:
                db = np.load(path, allow_pickle=True).item()
            except Exception as e:
                raise ValueError(f"Failed to load existing database: {str(e)}")
        else:
            db = {}
        
        # Validate input
        if not isinstance(name, str) or not name.strip():
            raise ValueError("Name must be a non-empty string")
        if not isinstance(embedding, np.ndarray):
            raise ValueError("Embedding must be a numpy array")
        
        # Save embedding
        db[name] = embedding
        
        # Attempt to save to file
        try:
            np.save(path, db)
        except Exception as e:
            raise IOError(f"Failed to save database to {path}: {str(e)}")
            
    except (ValueError, IOError) as e:
        raise Exception(f"Error saving embedding: {str(e)}")
    except Exception as e:
        raise Exception(f"Unexpected error while saving embedding: {str(e)}")

@app.post("/add-embedding")
async def add_embedding(file: UploadFile = File(...), name: str = None):
    global embeddings_db  # Используем глобальную переменную для обновления
    if not name:
        raise HTTPException(status_code=400, detail="Name is required")
    
    try:
        # Чтение и обработка изображения
        contents = await file.read()
        image = Image.open(io.BytesIO(contents)).convert("RGB")
        image = ImageOps.exif_transpose(image)
        img_array = np.array(image)
        
        # Детекция носа
        results = yolo_model.predict(img_array)
        if len(results[0].boxes) == 0:
            raise HTTPException(status_code=404, detail="No nose detected in the image")
        
        # Обрезка носа
        xywhn = results[0].boxes.xywhn[0].cpu().numpy()
        img_width, img_height = image.size
        left, top, right, bottom = convert_coordinates(xywhn, img_width, img_height)
        cropped_nose = image.crop((left, top, right, bottom))
        
        # Получение эмбеддинга
        embedding = get_embedding(simclr_model, cropped_nose)
        
        # Сохранение эмбеддинга
        save_embedding_to_db(name, embedding)
        
        # Перезагрузка базы эмбеддингов
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

@app.get("/status")
async def health_check():
    return {"status": "OK", "message": "Cow Nose Detector API is running"}