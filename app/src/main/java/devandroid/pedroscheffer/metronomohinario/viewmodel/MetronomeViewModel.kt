package devandroid.pedroscheffer.metronomohinario.viewmodel

import androidx.lifecycle.ViewModel
import devandroid.pedroscheffer.metronomohinario.core.MetronomeEngine
import devandroid.pedroscheffer.metronomohinario.data.HymnRepository
import devandroid.pedroscheffer.metronomohinario.ui.SpeedTier // <-- IMPORT NOVO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class MetronomeUiState(
    val hymnNumberInput: String = "1",
    val hymnName: String = "...",
    val timeSignature: String = "...",
    val bpm: Int = 0,
    val minBpm: Int = 40,
    val maxBpm: Int = 200,
    val isPlaying: Boolean = false,
    val accentEnabled: Boolean = true,
    val selectedSpeed: SpeedTier = SpeedTier.MED // <-- CAMPO NOVO
)

class MetronomeViewModel(private val engine: MetronomeEngine) : ViewModel() {

    private val _uiState = MutableStateFlow(MetronomeUiState())
    val uiState: StateFlow<MetronomeUiState> = _uiState

    private val repository = HymnRepository

    init {
        onHymnNumberChanged("1")
    }

    fun onHymnNumberChanged(input: String) {
        val number = input.toIntOrNull()

        if (number != null && number in 1..480) {
            repository.getHymnData(number)?.let { data ->
                _uiState.update {
                    it.copy(
                        hymnNumberInput = input,
                        hymnName = data.name,
                        timeSignature = data.timeSignature,
                        bpm = data.averageBpm,
                        minBpm = data.minBpm,
                        maxBpm = data.maxBpm,
                        selectedSpeed = SpeedTier.MED
                    )
                }
                engine.setBpm(data.averageBpm)
                engine.setTimeSignature(data.timeSignature)
            }
        } else {

            _uiState.update { it.copy(hymnNumberInput = input) }
        }
    }

    fun onSpeedTierChanged(tier: SpeedTier) {

        val currentHymn = _uiState.value.hymnNumberInput.toIntOrNull()?.let {
            repository.getHymnData(it)
        } ?: return

        val newBpm = when (tier) {
            SpeedTier.MIN -> currentHymn.minBpm
            SpeedTier.MED -> currentHymn.averageBpm
            SpeedTier.MAX -> currentHymn.maxBpm
        }

        _uiState.update {
            it.copy(
                selectedSpeed = tier,
                bpm = newBpm
            )
        }

        engine.setBpm(newBpm)
    }

    fun onPlayPauseClicked() {
        val isCurrentlyPlaying = _uiState.value.isPlaying
        if (isCurrentlyPlaying) {
            engine.stop()
        } else {
            engine.start()
        }
        _uiState.update { it.copy(isPlaying = !isCurrentlyPlaying) }
    }

    fun onAccentToggleChanged(isEnabled: Boolean) {
        _uiState.update { it.copy(accentEnabled = isEnabled) }
        engine.setAccent(isEnabled)
    }
}