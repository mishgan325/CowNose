package ru.mishgan325.cownose.ui.results

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import ru.mishgan325.cownose.data.network.BASE_URL
import ru.mishgan325.cownose.di.koinModule
import ru.mishgan325.cownose.domain.entities.NoseCoordinates
import ru.mishgan325.cownose.domain.entities.NoseSearchResult
import ru.mishgan325.cownose.ui.common.AppHeader
import ru.mishgan325.cownose.ui.common.ResultsPanel
import ru.mishgan325.cownose.ui.upload.ImagePreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDateTime
import kotlin.math.roundToInt

@Composable
fun ResultsScreen(
    onNavigateToUpload: () -> Unit,
    onNavigateBack: () -> Unit,

    modifier: Modifier = Modifier,

    ) {
    val viewModel: ResultViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        topBar = { AppHeader("Результаты", onNavigateBack, Modifier.padding(bottom = 10.dp)) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(padding).padding(horizontal = 10.dp)
                .let {
                    if (uiState is UiState.NoseFound) it.verticalScroll(rememberScrollState()) else it
                }
        ) {


            when (uiState) {
                UiState.InProgress -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.NoseFound -> {
                    val state = uiState as UiState.NoseFound
                    val similarCowsWithPainters = state.nose.similarCows.take(3).map {
                        val url = BASE_URL + it.imageUrl.substring(1)
                        object {
                            val painter = rememberAsyncImagePainter(url)
                            val cow = it
                        }
                    }

                    val allSimilarCowPaintersLoaded = similarCowsWithPainters.all {
                        it.painter.state.collectAsStateWithLifecycle().value is AsyncImagePainter.State.Success
                    }
                    if (allSimilarCowPaintersLoaded) {


                        ResultsPanel(
                            Modifier.fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                            ) {
                                CroppedImage(
                                    imageUri = state.imageUri,
                                    noseCoordinates = state.nose.noseCoordinates,
                                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                                )
                                Spacer(Modifier.height(10.dp))

                                Text(
                                    "Координаты носа",
                                    style = MaterialTheme.typography.titleMedium

                                )

                                Spacer(Modifier.height(8.dp))

                                val coordinates = state.nose.noseCoordinates
                                CoordinatesPanel(coordinates, Modifier.fillMaxWidth())


                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Text("Похожие носы", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.height(IntrinsicSize.Max)
                        ) {
                            for (cow in similarCowsWithPainters) {

                                ResultsPanel(
                                    Modifier.weight(1f)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                    ) {

                                        Image(
                                            painter = cow.painter,
                                            contentDescription = null,
                                            modifier = Modifier.clip(RoundedCornerShape(10.dp))

                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                "Похожесть:",
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                            val similarity = (cow.cow.similarity * 100).roundToInt()
                                            Text(
                                                "$similarity%",
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }

                                    }
                                }

                                Spacer(Modifier.width(4.dp))
                            }
                        }

                        Spacer(Modifier.height(10.dp))
                        ResultsPanel {
                            ExpandableDetailsPanel(
                                state.nose.databaseSize,
                                state.nose.embeddingSize,
                                state.nose.searchAlgorithm
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Spacer(Modifier.height(10.dp))
                        val scope = rememberCoroutineScope()
                        SaveAndRetryPanel(
                            onSaveClick = {
                                viewModel.insertNose(state.nose, state.imageUri)
                                scope.launch {
                                    delay(100)
                                    snackbarHostState.showSnackbar(
                                        "Сохранено",
                                        withDismissAction = true
                                    )
                                }
                            },
                            onTryAgainClick = {
                                onNavigateToUpload()
                            },
                            showSavedMessage = state.showSavedMessage,
                            modifier = Modifier
                        )


                    } else {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }


                }

                UiState.Error -> TODO()
                is UiState.NoseNotFound -> {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val state = uiState as UiState.NoseNotFound
                        Text(
                            "Нос не найден",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(Modifier.height(10.dp))

                        ImagePreview(
                            state.imageUri, Modifier
                                .wrapContentSize()
                                .clip(RoundedCornerShape(16.dp))
                                .weight(1f)
                        )
                        Spacer(Modifier.height(20.dp))
                        val scope = rememberCoroutineScope()
                        SaveAndRetryPanel(
                            onSaveClick = {
                                viewModel.insertNose(
                                    NoseSearchResult(
                                        id = -1,
                                        status = "failure",
                                        noseCoordinates = NoseCoordinates(-1, -1, -1, -1, -1, -1),
                                        similarCows = emptyList(),
                                        date = LocalDateTime.now(),
                                        databaseSize = -1,
                                        embeddingSize = -1,
                                        searchAlgorithm = "",
                                        imageFilepath = null
                                    ),
                                    state.imageUri
                                )
                                scope.launch {
                                    delay(100)
                                    snackbarHostState.showSnackbar(
                                        "Сохранено",
                                        withDismissAction = true
                                    )
                                }
                            },
                            onTryAgainClick = {
                                onNavigateToUpload()
                            },
                            showSavedMessage = state.showSavedMessage,
                            modifier = Modifier
                        )

                    }
                }
            }


        }
    }
}

@Composable
@Preview(
    backgroundColor = 0xFFF8F4F7, device = "id:pixel_5", showSystemUi = false,
    showBackground = true
)
fun ResultsPanelPreview(modifier: Modifier = Modifier) {
    ResultsPanel {
        Text("asdasasdas")
    }
}


@Composable
@Preview(
    backgroundColor = 0xFFF8F4F7, device = "id:pixel_5", showSystemUi = false,
    showBackground = true
)
fun ResultsScreenPreview(modifier: Modifier = Modifier) {
    KoinApplicationPreview(application = { koinModule }) {
        ResultsScreen({}, {})
    }
}
