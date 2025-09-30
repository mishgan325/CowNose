package ru.mishgan325.cownose.ui.screen.app.addembedding

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mishgan325.cownose.R
import ru.mishgan325.cownose.ui.screen.common.image.CapturePhotoButton
import ru.mishgan325.cownose.ui.screen.common.image.ImagePreview
import ru.mishgan325.cownose.ui.screen.common.image.ImagePreviewPlaceholder
import ru.mishgan325.cownose.ui.screen.common.image.LoadFromGalleryButton
import ru.mishgan325.cownose.ui.screen.common.state.UiState

@Composable
fun AddEmbeddingScreen(addEmbeddingViewModel: AddEmbeddingViewModel) {
    val context = LocalContext.current
    val uiState = addEmbeddingViewModel.uiState.collectAsState().value
    val imageUri = (uiState as? UiState.Default)?.imageUri
    val isDialogOpen = remember { mutableStateOf(value = false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(weight = 1f)
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 12.dp)
        ) {
            if (imageUri != null) ImagePreview(
                imageUri = imageUri,
                modifier = Modifier
                    .wrapContentSize()
                    .clip(shape = MaterialTheme.shapes.large)
                    .weight(weight = 1f, fill = true)
            )
            else ImagePreviewPlaceholder(modifier = Modifier.weight(weight = 1f, fill = true))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(space = 12.dp)
        ) {
            LoadFromGalleryButton(
                onImageLoad = { addEmbeddingViewModel.setPreviewImage(it) },
                modifier = Modifier.fillMaxWidth()
            )

            CapturePhotoButton(
                onImageLoad = { addEmbeddingViewModel.setPreviewImage(it) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedButton(
                onClick = {
                    if (imageUri != null)
                        isDialogOpen.value = true
                    else
                        Toast.makeText(
                            context,
                            R.string.select_image_first,
                            Toast.LENGTH_SHORT
                        ).show()
                },
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.add_nose),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        if (isDialogOpen.value) AddEmbeddingDialog(
            onDismiss = { isDialogOpen.value = false },
            onConfirm = { name ->
                imageUri?.let {
                    addEmbeddingViewModel.addEmbedding(name = name, imageUri = it)
                    isDialogOpen.value = false
                    Toast.makeText(context, R.string.nose_will_be_added, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}