package devandroid.pedroscheffer.metronomohinario.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import devandroid.pedroscheffer.metronomohinario.viewmodel.MetronomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MetronomeViewModel) {
    val uiState = viewModel.uiState.collectAsState().value

    // estado local para escolha de velocidade: "MEDIUM" | "MIN" | "MAX"
    val speedSelection = remember { mutableStateOf("MEDIUM") }

    // sempre que o hino mudar, resetar para MEDIUM (velocidade média)
    LaunchedEffect(key1 = uiState.hymnNumberInput) {
        speedSelection.value = "MEDIUM"
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Metrônomo Hinário 5")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onPlayPauseClicked() }) {
                Text(if (uiState.isPlaying) "Parar" else "Iniciar")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = uiState.hymnNumberInput,
                onValueChange = { viewModel.onHymnNumberChanged(it) },
                label = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Hino")
                    }
                },
                singleLine = true,
                modifier = Modifier.width(120.dp),
                textStyle = TextStyle(textAlign = TextAlign.Center)
            )

            Spacer(Modifier.height(12.dp))

            // Grupo de seleção: Mínimo / Médio / Máximo (ordem alterada conforme solicitado)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mínimo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = speedSelection.value == "MIN",
                        onClick = {
                            speedSelection.value = "MIN"
                            // aplica bpm mínimo
                            viewModel.onBpmSliderChanged(uiState.minBpm)
                        }
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Mínimo")
                }

                Spacer(Modifier.width(16.dp))

                // Médio
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = speedSelection.value == "MEDIUM",
                        onClick = {
                            speedSelection.value = "MEDIUM"
                            // restaurar média: recarrega o hino (ViewModel aplica a média ao carregar)
                            viewModel.onHymnNumberChanged(uiState.hymnNumberInput)
                        }
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Médio")
                }

                Spacer(Modifier.width(16.dp))

                // Máximo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = speedSelection.value == "MAX",
                        onClick = {
                            speedSelection.value = "MAX"
                            // aplica bpm máximo
                            viewModel.onBpmSliderChanged(uiState.maxBpm)
                        }
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Máximo")
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = uiState.hymnName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Compasso: ${uiState.timeSignature}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("BPM", style = MaterialTheme.typography.bodyMedium)
                        Text("${uiState.bpm}", style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Min", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Text("${uiState.minBpm}", style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.width(12.dp))
                        Text("Max", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Text("${uiState.maxBpm}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = uiState.accentEnabled,
                        onCheckedChange = { viewModel.onAccentToggleChanged(it) }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Acentuar batida forte")
                }

                Text(
                    text = if (uiState.isPlaying) "Executando" else "Parado",
                    color = if (uiState.isPlaying) Color(0xFF0F9D58) else Color.Gray
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Toque em Iniciar para ouvir o metrônomo",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}