package ru.mishgan325.cownose.ui.screen.separate.historydetails

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import ru.mishgan325.cownose.R
import ru.mishgan325.cownose.data.network.BASE_URL
import ru.mishgan325.cownose.ui.screen.app.history.HistoryViewModel
import ru.mishgan325.cownose.ui.screen.app.result.CoordinatesPanel
import ru.mishgan325.cownose.ui.screen.app.result.ExpandableDetailsPanel
import ru.mishgan325.cownose.ui.screen.app.result.ResultsPanel
import ru.mishgan325.cownose.ui.screen.common.image.CroppedImage
import java.io.File
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryDetailsScreen(noseSearchResultId: Int, historyViewModel: HistoryViewModel) {
    val noseSearchResults = historyViewModel.noseSearchResults.collectAsState().value
    val noseSearchResult = noseSearchResults.find { it.id == noseSearchResultId }

    val showDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (noseSearchResult?.imageFilepath != null) {
            val similarCows = noseSearchResult.similarCows.take(n = 3).map { cow ->
                val url = BASE_URL + cow.imageUrl.removePrefix(prefix = "/")
                url to cow
            }

            val imageUri = Uri.fromFile(File(noseSearchResult.imageFilepath))
            val isSuccess = noseSearchResult.status.equals("success", ignoreCase = true)

            Text(
                text = if (isSuccess) stringResource(id = R.string.success)
                else stringResource(id = R.string.failure),
                color = if (isSuccess) Color(0xff28b128) else MaterialTheme.colorScheme.error,
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp, bottom = 4.dp)
            )

            ResultsPanel(Modifier.fillMaxWidth()) {
                ExpandableImagePreview(imageUri = imageUri, expandedInitially = !isSuccess)
            }

            if (isSuccess) {
                ResultsPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val coordinates = noseSearchResult.noseCoordinates

                        CroppedImage(
                            imageUri = imageUri,
                            noseCoordinates = coordinates,
                            modifier = Modifier.clip(RoundedCornerShape(10.dp))
                        )

                        Text(
                            text = stringResource(R.string.nose_coordinates),
                            style = MaterialTheme.typography.titleMedium
                        )

                        CoordinatesPanel(
                            coordinates = coordinates,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.similar_noses),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp)
                )

                // Без IntrinsicSize.Max — задаём размер картинок явно
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    similarCows.forEach { (url, cow) ->
                        ResultsPanel(Modifier.weight(1f)) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                GlideImage(
                                    imageModel = { url },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(R.string.similarity),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Text(
                                        text = "${(cow.similarity * 100).roundToInt()}%",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                }

                ResultsPanel {
                    ExpandableDetailsPanel(
                        databaseSize = noseSearchResult.databaseSize,
                        embeddingSize = noseSearchResult.embeddingSize,
                        searchAlgorithm = noseSearchResult.searchAlgorithm
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val style = MaterialTheme.typography.titleMedium
                Text(text = stringResource(id = R.string.date), style = style)
                Text(
                    text = noseSearchResult.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                    style = style
                )
            }

            Button(
                onClick = { showDialog.value = true },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.delete_record_button),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (showDialog.value)
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text(text = stringResource(R.string.delete_record)) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDialog.value = false
                                historyViewModel.deleteNoseSearchResult(
                                    id = noseSearchResult.id,
                                    imageFilepath = noseSearchResult.imageFilepath
                                )
                            }
                        ) { Text(text = stringResource(R.string.confirm)) }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog.value = false }) {
                            Text(text = stringResource(R.string.cancel))
                        }
                    }
                )
        } else Text(text = "Image filepath is null!")
    }
}