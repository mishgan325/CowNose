package ru.mishgan325.cownose.ui.historydetails

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import ru.mishgan325.cownose.data.network.BASE_URL
import ru.mishgan325.cownose.ui.common.AppHeader
import ru.mishgan325.cownose.ui.common.ResultsPanel
import ru.mishgan325.cownose.ui.history.HistoryViewModel
import ru.mishgan325.cownose.ui.results.CoordinatesPanel
import ru.mishgan325.cownose.ui.results.CroppedImage
import ru.mishgan325.cownose.ui.results.ExpandableDetailsPanel
import org.koin.compose.viewmodel.koinViewModel
import java.io.File
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

const val TAG = "HistoryDetailsScreen"

@Composable
fun HistoryDetailsScreen(
    noseSearchResultId: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel: HistoryViewModel = koinViewModel()
    val noseSearchResults by viewModel.noseSearchResults.collectAsStateWithLifecycle()
    val noseSearchResult = noseSearchResults.find { it.id == noseSearchResultId }
    var imageUri: Uri



    Scaffold(
        topBar = { AppHeader("Подробности", onNavigateBack) },
        snackbarHost = { }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {


            if (noseSearchResult?.imageFilepath != null) {

                val similarCowsWithPainters = noseSearchResult.similarCows.take(3).map {
                    val url = BASE_URL + it.imageUrl.substring(1)
                    object {
                        val painter = rememberAsyncImagePainter(url)
                        val cow = it
                    }
                }

                similarCowsWithPainters.all {
                    it.painter.state.collectAsStateWithLifecycle().value is AsyncImagePainter.State.Success
                }

                imageUri = Uri.fromFile(File(noseSearchResult.imageFilepath))
                Spacer(Modifier.height(10.dp))
                val isSuccess = noseSearchResult.status.equals("success", ignoreCase = true)

                Text(
                    text = if (isSuccess) "Успех" else "Провал",
                    color = if (isSuccess) Color(0xff28b128) else Color.Red,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(10.dp))


                ResultsPanel(Modifier.fillMaxWidth()) {
                    ExpandableImagePreview(
                        imageUri, !isSuccess
                    )
                }

                Spacer(Modifier.height(10.dp))
                if (isSuccess) {

                    ResultsPanel(
                        Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                        ) {
                            val coordinates = noseSearchResult.noseCoordinates

                            CroppedImage(
                                imageUri = imageUri,
                                noseCoordinates = coordinates,
                                modifier = Modifier.clip(RoundedCornerShape(10.dp))
                            )
                            Spacer(Modifier.height(10.dp))

                            Text(
                                "Координаты носа",
                                style = MaterialTheme.typography.titleMedium

                            )

                            Spacer(Modifier.height(8.dp))

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
                            noseSearchResult.databaseSize,
                            noseSearchResult.embeddingSize,
                            noseSearchResult.searchAlgorithm
                        )
                    }
                    Spacer(Modifier.height(10.dp))


                }

                Row(
                    Modifier.padding(horizontal = 10.dp)
                ) {
                    val style = MaterialTheme.typography.titleMedium
                    Text("Дата", style = style)
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = noseSearchResult.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                        modifier = Modifier.padding(start = 8.dp),
                        style = style
                    )
                }
                Spacer(Modifier.height(10.dp))


                var showDialog by remember { mutableStateOf(false) }

                Button(
                    onClick = {
                        showDialog = true
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        "Удалить запись",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(Modifier.height(10.dp))


                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Удалить запись?") },

                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDialog = false
                                    onNavigateBack()
                                    viewModel.deleteNoseSearchResult(
                                        noseSearchResult.id,
                                        noseSearchResult.imageFilepath
                                    )
                                }
                            ) {
                                Text("Подтвердить")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showDialog = false }
                            ) {
                                Text("Отмена")
                            }
                        }
                    )
                }


            } else {
                Text("Image filepath is null!")
            }


        }
    }
}
