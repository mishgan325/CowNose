package ru.mishgan325.cownose.ui.results

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SaveAndRetryPanel(
    onSaveClick: () -> Unit,
    onTryAgainClick: () -> Unit,
    showSavedMessage: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {


        Button(
            onClick = onSaveClick,

//            {
//            viewModel.insertNose(state.nose, state.imageUri)
////                            onNavigateToHistory()
//        }

            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                "Сохранить результат",
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (showSavedMessage) {
            Text("Сохранено", modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        OutlinedButton(
            onClick = onTryAgainClick,
//            {
//            onNavigateToUpload()
//        }

            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                "Попробовать еще раз",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}