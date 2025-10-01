package ru.mishgan325.cownose.ui.screen.app.nfc

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import ru.mishgan325.cownose.R

@Composable
fun NFCScreen(viewModel: NFCScreenViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity
    val nfcAdapter = remember { NfcAdapter.getDefaultAdapter(context) }

    val status by viewModel.status.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val cowName by viewModel.cowName.collectAsState()
    val cowImageUrl by viewModel.cowImageUrl.collectAsState()
    val lastMilkingDate by viewModel.lastMilkingDate.collectAsState()
    val cowPen by viewModel.cowPen.collectAsState()

    DisposableEffect(Unit) {
        if (nfcAdapter != null && activity != null) {
            val callback = NfcAdapter.ReaderCallback { tag: Tag? ->
                tag?.let {
                    val uidHex = it.id.joinToString("") { b -> "%02X".format(b) }
                    activity.runOnUiThread {
                        viewModel.onNfcDetected(uidHex)
                    }
                }
            }

            nfcAdapter.enableReaderMode(
                activity,
                callback,
                NfcAdapter.FLAG_READER_NFC_A or
                        NfcAdapter.FLAG_READER_NFC_B or
                        NfcAdapter.FLAG_READER_NFC_F or
                        NfcAdapter.FLAG_READER_NFC_V,
                null
            )
            onDispose { nfcAdapter.disableReaderMode(activity) }
        } else {
            onDispose { }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.nfc_scan_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.nfc_status),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isLoading) stringResource(id = R.string.nfc_processing) else status,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            cowImageUrl?.let { url ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // займет максимально доступное место, сохраняя пропорции
                    shape = RoundedCornerShape(10.dp)
                ) {
                    GlideImage(
                        imageModel = { url },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                cowName?.let { name ->
                    Text(
                        text = stringResource(id = R.string.cow_name, name),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                lastMilkingDate?.let { date ->
                    Text(
                        text = stringResource(id = R.string.last_milking_date, date),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                cowPen?.let { pen ->
                    Text(
                        text = stringResource(id = R.string.cow_pen, pen),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}