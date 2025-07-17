package ru.mishgan325.cownose.ui.upload

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mishgan325.cownose.ui.common.AppHeader
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UploadScreen(
    onNavigateToResults: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {


    val viewModel: UploadViewModel = koinViewModel()
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

                AppHeader("Загрузка", null, Modifier.padding(bottom = 10.dp))





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

                Spacer(Modifier.height(10.dp))

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
                        imageUri?.let {
                            viewModel.setImageForRecognition(imageUri)
                            onNavigateToResults(it)
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                ) {
                    Text(
                        "Распознать нос",
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
fun LoadImagePanelPreview(modifier: Modifier = Modifier) {
    ImagePreviewPlaceholder(modifier = Modifier)
}


@Composable
@Preview(
    backgroundColor = 0xFFF8F4F7, device = "id:pixel_5", showSystemUi = false,
    showBackground = true
)
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    UploadScreen({}, modifier = Modifier)
}


