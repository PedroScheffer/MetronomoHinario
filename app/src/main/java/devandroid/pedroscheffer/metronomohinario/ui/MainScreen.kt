package devandroid.pedroscheffer.metronomohinario.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import devandroid.pedroscheffer.metronomohinario.viewmodel.MetronomeViewModel

@Composable
fun MainScreen(viewModel: MetronomeViewModel) {
    val uiState = viewModel.uiState.collectAsState().value

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = uiState.hymnNumberInput,
            onValueChange = { viewModel.onHymnNumberChanged(it) },
            label = { Text("Número do Hino (1-480)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(16.dp))

        Text(text = "Compasso: ${uiState.timeSignature}", style = MaterialTheme.typography.titleMedium)
        Text(text = "BPM: ${uiState.bpm}", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(32.dp))

        Button(onClick = { viewModel.onPlayPauseClicked() }) {
            Text(if (uiState.isPlaying) "Parar" else "Iniciar")
        }

        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = uiState.accentEnabled,
                onCheckedChange = { viewModel.onAccentToggleChanged(it) }
            )
            Text("Acentuar batida forte")
        }
    }
}