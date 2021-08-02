package ni.com.spiralni.yasc

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import ni.com.spiralni.yasc.ui.theme.YASCTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<CalculatorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        darkStatusBar()

        setContent {
            YASCTheme {
                MainActivityScreen(viewModel)
            }
        }
    }

    fun darkStatusBar() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = this.resources.getColor(R.color.black)
    }

    @Composable
    fun MainActivityScreen(viewModel: CalculatorViewModel) {
        val display: Display by viewModel.display.observeAsState(Display())
        CalculatorScreen(display) { viewModel.handleInput(it) }
    }
}
