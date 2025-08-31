package ru.mishgan325.cownose.ui.nfc

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mishgan325.cownose.ui.common.AppHeader

@Composable
fun NFCScreen(modifier: Modifier = Modifier) {

    AppHeader("NFC", null, null, Modifier.padding(bottom = 10.dp))


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Здесь будет NFC сканер")
    }
}

@Composable
@Preview(
    backgroundColor = 0xFFF8F4F7, device = "id:pixel_5", showSystemUi = false,
    showBackground = true
)
fun NFCScreenPreview(modifier: Modifier = Modifier) {
    NFCScreen(modifier = modifier)
}
