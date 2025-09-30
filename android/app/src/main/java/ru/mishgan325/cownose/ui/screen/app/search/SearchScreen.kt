package ru.mishgan325.cownose.ui.screen.app.search

import android.net.Uri
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mishgan325.cownose.R
import ru.mishgan325.cownose.ui.screen.common.image.CapturePhotoButton
import ru.mishgan325.cownose.ui.screen.common.image.ImagePreview
import ru.mishgan325.cownose.ui.screen.common.image.ImagePreviewPlaceholder
import ru.mishgan325.cownose.ui.screen.common.image.LoadFromGalleryButton
import ru.mishgan325.cownose.ui.screen.common.state.UiState

@Composable
fun SearchScreen(searchViewModel: SearchViewModel, onNavigateToResults: (Uri) -> Unit) {
    val uiState = searchViewModel.uiState.collectAsState().value

    when (uiState) {
        is UiState.Default -> {
            val imageUri = uiState.imageUri

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
                    if (imageUri != null)
                        ImagePreview(
                            imageUri = imageUri,
                            modifier = Modifier
                                .wrapContentSize()
                                .clip(shape = MaterialTheme.shapes.large)
                                .weight(weight = 1f, fill = true)
                        )
                    else
                        ImagePreviewPlaceholder(
                            modifier = Modifier.weight(weight = 1f, fill = true)
                        )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 12.dp)
                ) {
                    LoadFromGalleryButton(
                        onImageLoad = { searchViewModel.setPreviewImage(it) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    CapturePhotoButton(
                        onImageLoad = { searchViewModel.setPreviewImage(it) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedButton(
                        onClick = {
                            imageUri?.let {
                                searchViewModel.setImageForRecognition(it)
                                onNavigateToResults(it)
                            }
                        },
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.recognize_nose),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }

        is UiState.Error -> Unit
        is UiState.InProgress -> Unit
        is UiState.NoseFound -> Unit
        is UiState.NoseNotFound -> Unit
    }
}