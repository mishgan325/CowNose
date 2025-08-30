package ru.mishgan325.cownose.ui.addembedding

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mishgan325.cownose.ui.common.AppHeader
import ru.mishgan325.cownose.ui.common.CapturePhotoButton
import ru.mishgan325.cownose.ui.common.ImagePreview
import ru.mishgan325.cownose.ui.common.ImagePreviewPlaceholder
import ru.mishgan325.cownose.ui.common.LoadFromGalleryButton
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddEmbeddingScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: AddEmbeddingViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is UiState.Default -> {
            val state = uiState as UiState.Default
            Column(
                modifier = modifier
                    .padding(horizontal = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                AppHeader("Добавить нос", null, Modifier.padding(bottom = 10.dp))





                val imageUri = state.imageUri

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (imageUri != null) {
                        ImagePreview(
                            imageUri, Modifier
                                .wrapContentSize()
                                .clip(RoundedCornerShape(16.dp))
                                .weight(1f)
                        )
                    } else {
                        ImagePreviewPlaceholder(
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LoadFromGalleryButton(
                    onImageLoad = { viewModel.setPreviewImage(it) },
                    modifier = Modifier.padding(horizontal = 40.dp)
                )

                CapturePhotoButton(
                    onImageLoad = { viewModel.setPreviewImage(it) },
                    modifier = Modifier.padding(horizontal = 40.dp)
                )

                OutlinedButton(
                    onClick = {
                        if (imageUri != null) {
                            Toast.makeText(context, "Нос будет добавлен", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Сначала выберите изображение", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                ) {
                    Text(
                        "Добавить нос",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        else -> {}
    }
}

@Composable
@Preview(
    backgroundColor = 0xFFF8F4F7, device = "id:pixel_5", showSystemUi = false,
    showBackground = true
)
fun AddEmbeddingScreenPreview(modifier: Modifier = Modifier) {
    AddEmbeddingScreen(modifier = modifier)
}
