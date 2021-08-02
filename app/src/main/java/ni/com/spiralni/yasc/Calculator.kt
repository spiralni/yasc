package ni.com.spiralni.yasc

import net.objecthunter.exp4j.ExpressionBuilder
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class Calculator {
    private var isComputed: Boolean = false

    private var _display: String = "0"

    private fun formatOutput(d: BigDecimal): String {
        val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
        formatter.applyPattern("#,###.####")
        return formatter.format(d)
    }

    val display: String
        get() {
            if (isDefaultOrError) {
                return _display
            }

            if (isComputed) {
                val value = _display.toBigDecimal()
                return formatOutput(value)
            }

            return _display
        }

    val secondaryDisplay: String
        get() {
            if (isDefault || isError || isComputed) {
                return ""
            }

            val result = process(validExpression)

            return "= $result"
        }

    private val len: Int
        get() = _display.length

    private val isEmpty: Boolean
        get() = _display.isEmpty()

    private val isDefaultOrError: Boolean
        get() = isDefault || isError

    private val isDefault: Boolean
        get() = _display == "0"

    private val lastInput: String
        get() = if (isEmpty) "0" else _display[_display.lastIndex].toString()

    private val isLastInputNumeric: Boolean
        get() = isNumeric(lastInput)

    private val isLastInputDot: Boolean
        get() = "." == lastInput

    private val isError: Boolean
        get() = "Error" == _display

    private val validExpression: String
        get() {
            var clean = _display.replace("x", "*")

            if (!isLastInputNumeric) {
                clean = clean.substring(0 until clean.lastIndex)
            }

            return clean
        }

    private fun isNumeric(key: String): Boolean {
        return key.matches("\\d".toRegex())
    }

    fun handleInput(key: String) {
        if (isError) {
            clear()
            return
        }

        when (key) {
            "C"   -> deleteLast()
            "CC"  -> clear()
            "="   -> calculate()
            else  -> append(key)
        }
    }

    private fun canAppendDot(): Boolean {
        _display.split("").asReversed().forEach {
            if ("." == it) {
                return false
            }

            if (it in listOf("x", "/", "-", "+")) {
                return true
            }
        }

        return true
    }

    private fun append(key: String) {
        if (isComputed) {
            isComputed = false

            if ("." == key) {
                _display = "0."
                return
            }

            if (isNumeric(key)) {
                _display = key
                return
            }
        }

        if (isDefault) {
            when(key) {
                in listOf("0", "C", "/", "x", "+", "=") -> _display = "0"
                "." -> _display += "."
                else -> _display = key
            }

            return
        }

        if ("." == key) {
            if (canAppendDot()) {
                _display += if (isLastInputNumeric) {
                    key
                } else {
                    "0."
                }
            }

            return
        }

        if (isLastInputNumeric || isLastInputDot ||
            (!isLastInputNumeric && isNumeric(key))) {
            _display += key
            return
        }

        if (!isLastInputNumeric) {
            deleteLast()
            _display += key
            return
        }

        if ("=" == lastInput) {
            calculate()
        }
    }

    private fun calculate() {
        if (isDefault) {
            return
        }

        _display =  process(validExpression)
        isComputed = true
    }

    private fun process(expr: String): String {
        if (expr.isEmpty()) {
            return expr
        }

        val expression = ExpressionBuilder(expr).build()

        return try {
            val result = expression.evaluate().toBigDecimal()
            result.toString().removeSuffix(".0")
        } catch (ex: ArithmeticException) {
            "Error"
        }
    }

    private fun deleteLast() {
        if (isDefault) {
            return
        }

        if (isComputed) {
            clear()
            isComputed = false
        }

        if (len < 2) {
            clear()
            return
        }

        _display = _display.substring(0 until _display.lastIndex)
    }

    private fun clear() {
        _display = "0"
    }
}