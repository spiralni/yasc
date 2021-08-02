package ni.com.spiralni.yasc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel: ViewModel() {
    private val calculator = Calculator()

    private var _display = MutableLiveData(Display())
    val display: LiveData<Display> = _display

    fun handleInput(key: String) {
        calculator.handleInput(key)
        _display.value = Display(calculator.display, calculator.secondaryDisplay)
    }
}
