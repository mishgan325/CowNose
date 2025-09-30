package ru.mishgan325.cownose.ui.screen.app.nfc

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mishgan325.cownose.R
import java.nio.charset.Charset

@Composable
fun NFCScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    val nfcMessage = remember { mutableStateOf(context.getString(R.string.nfc_hold_tag)) }
    val nfcAdapter = remember { NfcAdapter.getDefaultAdapter(context) }

    DisposableEffect(key1 = Unit) {
        if (nfcAdapter != null && activity != null) {
            val callback = NfcAdapter.ReaderCallback { tag: Tag? ->
                tag?.let {
                    var text: String? = null
                    val ndef = Ndef.get(it)

                    if (ndef != null) {
                        try {
                            ndef.connect()
                            val ndefMessage = ndef.ndefMessage
                            val record = ndefMessage?.records?.firstOrNull()
                            text = record?.payload?.let { payload ->
                                if (payload.isNotEmpty()) {
                                    val langCodeLen = payload[0].toInt() and 0x3F
                                    String(
                                        bytes = payload,
                                        offset = langCodeLen + 1,
                                        length = payload.size - langCodeLen - 1,
                                        Charset.forName("UTF-8")
                                    )
                                } else null
                            }
                            ndef.close()
                        } catch (e: Exception) {
                            text = context.getString(R.string.nfc_read_error, e.localizedMessage)
                        }
                    }

                    if (text.isNullOrBlank()) {
                        val uid = it.id.joinToString(separator = " ") { b -> "%02X".format(b) }
                        text = context.getString(R.string.nfc_uid, uid)
                        nfcMessage.value = text
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
        } else onDispose { }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.nfc_scanning),
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
                    .padding(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.message),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = nfcMessage.value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}