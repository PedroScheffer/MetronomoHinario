package devandroid.pedroscheffer.metronomohinario.core

import android.content.Context
import android.media.SoundPool
import devandroid.pedroscheffer.metronomohinario.R
import kotlinx.coroutines.*
import kotlin.math.roundToInt

class MetronomeEngine(private val context: Context) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var metronomeJob: Job? = null

    private val soundPool: SoundPool = SoundPool.Builder().setMaxStreams(2).build()
    private val tickSoundId: Int
    private val tockSoundId: Int

    var isRunning: Boolean = false
        private set
    private var bpm: Int = 60
    private var beatsPerMeasure: Int = 4
    private var accentEnabled: Boolean = true

    init {
        tickSoundId = soundPool.load(context, R.raw.tick, 1)
        tockSoundId = soundPool.load(context, R.raw.tock, 1)
    }

    fun setBpm(newBpm: Int) {
        bpm = newBpm
    }

    fun setTimeSignature(signature: String) {
        val beats = signature.split("/").firstOrNull()?.toIntOrNull() ?: 4
        beatsPerMeasure = beats
    }

    fun setAccent(enabled: Boolean) {
        accentEnabled = enabled
    }

    fun start() {
        if (isRunning) return
        isRunning = true

        val delayMs = (60_000L / bpm.toDouble()).roundToInt()
        var currentBeat = 1

        metronomeJob = coroutineScope.launch {
            while (isRunning) {
                playSound(currentBeat)
                delay(delayMs.toLong())
                currentBeat = (currentBeat % beatsPerMeasure) + 1
            }
        }
    }

    fun stop() {
        isRunning = false
        metronomeJob?.cancel()
    }

    private fun playSound(beat: Int) {
        if (beat == 1 && accentEnabled) {
            soundPool.play(tockSoundId, 1f, 1f, 1, 0, 1f)
        } else {
            soundPool.play(tickSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun release() {
        soundPool.release()
    }
}