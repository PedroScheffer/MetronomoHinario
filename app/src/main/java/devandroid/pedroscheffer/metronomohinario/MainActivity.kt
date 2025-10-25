package devandroid.pedroscheffer.metronomohinario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import devandroid.pedroscheffer.metronomohinario.core.MetronomeEngine
import devandroid.pedroscheffer.metronomohinario.ui.MainScreen
import devandroid.pedroscheffer.metronomohinario.viewmodel.MetronomeViewModel
import devandroid.pedroscheffer.metronomohinario.ui.theme.MetronomoHinarioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val engine = MetronomeEngine(this)
        val viewModel = MetronomeViewModel(engine)

        setContent {
            MetronomoHinarioTheme {
                MainScreen(viewModel)
            }
        }
    }
}