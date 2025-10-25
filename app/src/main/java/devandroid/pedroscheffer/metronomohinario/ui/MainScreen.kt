package devandroid.pedroscheffer.metronomohinario.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import devandroid.pedroscheffer.metronomohinario.viewmodel.MetronomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MetronomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Metrônomo Hinário 5") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentPadding = PaddingValues(16.dp)
            ) {
                Button(
                    onClick = { viewModel.onPlayPauseClicked() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = if (uiState.isPlaying) "Parar" else "Iniciar",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SelectionCard(
                hymnInput = uiState.hymnNumberInput,
                selectedSpeed = uiState.selectedSpeed,
                onHymnInputChange = { viewModel.onHymnNumberChanged(it) },
                onSpeedChange = { viewModel.onSpeedTierChanged(it) }
            )

            Spacer(Modifier.height(24.dp))

            MetronomeDashboard(
                hymnTitle = uiState.hymnName,
                timeSignature = uiState.timeSignature,
                currentBpm = uiState.bpm,
                minBpm = uiState.minBpm,
                maxBpm = uiState.maxBpm,
                statusText = if (uiState.isPlaying) "Executando" else "Parado"
            )

            Spacer(Modifier.weight(1f))

            AccentToggle(
                accentEnabled = uiState.accentEnabled,
                onAccentToggleChanged = { viewModel.onAccentToggleChanged(it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectionCard(
    hymnInput: String,
    selectedSpeed: SpeedTier,
    onHymnInputChange: (String) -> Unit,
    onSpeedChange: (SpeedTier) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Column(Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = hymnInput,
                onValueChange = { newText ->

                    if (newText.length <= 3) {
                        onHymnInputChange(newText)
                    }
                },
                label = { Text("Número do Hino (1-480)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            Text("Velocidade:", style = MaterialTheme.typography.labelMedium)

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = selectedSpeed == SpeedTier.MIN,
                    onClick = { onSpeedChange(SpeedTier.MIN) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
                ) { Text("Mínimo") }

                SegmentedButton(
                    selected = selectedSpeed == SpeedTier.MED,
                    onClick = { onSpeedChange(SpeedTier.MED) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
                ) { Text("Médio") }

                SegmentedButton(
                    selected = selectedSpeed == SpeedTier.MAX,
                    onClick = { onSpeedChange(SpeedTier.MAX) },
                    shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
                ) { Text("Máximo") }
            }
        }
    }
}

@Composable
private fun MetronomeDashboard(
    hymnTitle: String,
    timeSignature: String,
    currentBpm: Int,
    minBpm: Int,
    maxBpm: Int,
    statusText: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hymnTitle,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Compasso: $timeSignature",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(32.dp))

        Text(
            text = "$currentBpm",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "BPM",
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = statusText,
            style = MaterialTheme.typography.titleMedium,
            color = if (statusText == "Executando") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(0.6f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Min: $minBpm",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "Max: $maxBpm",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun AccentToggle(
    accentEnabled: Boolean,
    onAccentToggleChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Acentuar batida forte",
            style = MaterialTheme.typography.bodyLarge
        )
        Switch(
            checked = accentEnabled,
            onCheckedChange = onAccentToggleChanged
        )
    }
}