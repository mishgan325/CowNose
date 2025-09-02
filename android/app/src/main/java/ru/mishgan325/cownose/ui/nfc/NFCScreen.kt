package ru.mishgan325.cownose.ui.nfc

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ru.mishgan325.cownose.ui.common.AppHeader
import java.nio.charset.Charset

@Composable
fun NFCScreen(modifier: Modifier = Modifier) {

    AppHeader("NFC", null, null, Modifier.padding(bottom = 10.dp))

    val context = LocalContext.current
    val activity = context as? Activity
    var nfcMessage by remember { mutableStateOf("Поднесите NFC-метку к телефону") }

    val nfcAdapter = remember { NfcAdapter.getDefaultAdapter(context) }

    DisposableEffect(Unit) {
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
                                        payload,
                                        langCodeLen + 1,
                                        payload.size - langCodeLen - 1,
                                        Charset.forName("UTF-8")
                                    )
                                } else null
                            }
                            ndef.close()
                        } catch (e: Exception) {
                            text = "Ошибка чтения: ${e.localizedMessage}"
                        }
                    }

                    if (text.isNullOrBlank()) {
                        text = "UID метки: " + it.id.joinToString(" ") { b ->
                            "%02X".format(b)
                        }
                    }

                    nfcMessage = text!!
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

            onDispose {
                nfcAdapter.disableReaderMode(activity)
            }
        } else {
            onDispose { }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(nfcMessage)
    }
}
