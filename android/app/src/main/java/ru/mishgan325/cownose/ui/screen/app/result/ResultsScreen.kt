package ru.mishgan325.cownose.ui.screen.app.result

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import ru.mishgan325.cownose.R
import ru.mishgan325.cownose.data.database.entity.NoseCoordinates
import ru.mishgan325.cownose.data.database.entity.NoseSearchResult
import ru.mishgan325.cownose.data.network.BASE_URL
import ru.mishgan325.cownose.ui.screen.common.image.CroppedImage
import ru.mishgan325.cownose.ui.screen.common.image.ImagePreview
import ru.mishgan325.cownose.ui.screen.common.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.math.roundToInt

@RequiresApi(value = Build.VERSION_CODES.O)
@Composable
fun ResultsScreen(resultsViewModel: ResultsViewModel, onNavigateToSearch: () -> Unit) {
    val context = LocalContext.current

    val uiState = resultsViewModel.uiState.collectAsState().value
    val snackBarHostState = remember { SnackbarHostState() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 10.dp).let {
            if (uiState is UiState.NoseFound) it.verticalScroll(state = rememberScrollState())
            else it
        }
    ) {
        when (uiState) {
            is UiState.InProgress ->
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

            is UiState.NoseFound -> {
                val state = uiState
                val similarCowsWithPainters = state.nose.similarCows.take(n = 3).map {
                    val url = BASE_URL + it.imageUrl.substring(startIndex = 1)
                    object {
                        val painter = rememberAsyncImagePainter(model = url)
                        val cow = it
                    }
                }

                val allLoaded = similarCowsWithPainters.all {
                    it.painter.state.collectAsStateWithLifecycle().value is AsyncImagePainter.State.Success
                }

                if (!allLoaded)
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                else
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(space = 10.dp)
                    ) {
                        ResultsPanel(Modifier.fillMaxWidth()) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                            ) {
                                CroppedImage(
                                    imageUri = state.imageUri,
                                    noseCoordinates = state.nose.noseCoordinates,
                                    modifier = Modifier.clip(shape = RoundedCornerShape(size = 10.dp))
                                )
                                Text(
                                    text = stringResource(id = R.string.nose_coordinates),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                CoordinatesPanel(
                                    coordinates = state.nose.noseCoordinates,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Text(
                            text = stringResource(id = R.string.similar_noses),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Row(
                            modifier = Modifier.height(IntrinsicSize.Max),
                            horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
                        ) {
                            similarCowsWithPainters.forEach { cow ->
                                ResultsPanel(Modifier.weight(weight = 1f)) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(space = 6.dp)
                                    ) {
                                        Image(
                                            painter = cow.painter,
                                            contentDescription = null,
                                            modifier = Modifier.clip(shape = RoundedCornerShape(size = 10.dp))
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.similarity),
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                            Text(
                                                text = "${(cow.cow.similarity * 100).roundToInt()}",
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        ResultsPanel {
                            ExpandableDetailsPanel(
                                databaseSize = state.nose.databaseSize,
                                embeddingSize = state.nose.embeddingSize,
                                searchAlgorithm = state.nose.searchAlgorithm
                            )
                        }

                        val scope = rememberCoroutineScope()
                        SaveAndRetryPanel(
                            onSaveClick = {
                                resultsViewModel.insertNose(
                                    nose = state.nose,
                                    imageUri = state.imageUri
                                )
                                scope.launch {
                                    delay(timeMillis = 100)
                                    snackBarHostState.showSnackbar(
                                        message = context.getString(R.string.saved),
                                        withDismissAction = true
                                    )
                                }
                            },
                            onTryAgainClick = { onNavigateToSearch() },
                            showSavedMessage = state.showSavedMessage
                        )
                    }

            }

            is UiState.NoseNotFound ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = 16.dp)
                ) {
                    val state = uiState

                    Text(
                        text = stringResource(id = R.string.nose_not_found),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    ImagePreview(
                        imageUri = state.imageUri,
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxHeight(fraction = 0.7f)
                            .clip(shape = RoundedCornerShape(size = 16.dp))
                    )

                    val scope = rememberCoroutineScope()
                    SaveAndRetryPanel(
                        onSaveClick = {
                            resultsViewModel.insertNose(
                                NoseSearchResult(
                                    id = -1,
                                    status = "failure",
                                    noseCoordinates = NoseCoordinates(
                                        left = -1,
                                        top = -1,
                                        right = -1,
                                        bottom = -1,
                                        width = -1,
                                        height = -1
                                    ),
                                    similarCows = emptyList(),
                                    date = LocalDateTime.now(),
                                    databaseSize = -1,
                                    embeddingSize = -1,
                                    searchAlgorithm = "",
                                    imageFilepath = null
                                ),
                                imageUri = state.imageUri
                            )
                            scope.launch {
                                delay(timeMillis = 100)
                                snackBarHostState.showSnackbar(
                                    message = context.getString(R.string.saved),
                                    withDismissAction = true
                                )
                            }
                        },
                        onTryAgainClick = { onNavigateToSearch() },
                        showSavedMessage = state.showSavedMessage
                    )
                }

            is UiState.Default -> Unit
            is UiState.Error -> Unit
        }
    }
}