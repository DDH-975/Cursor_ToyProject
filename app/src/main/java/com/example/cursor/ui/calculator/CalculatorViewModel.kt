package com.example.cursor.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cursor.domain.CalculatorExpressionEvaluator

class CalculatorViewModel : ViewModel() {

    private val _expression = MutableLiveData("")
    val expression: LiveData<String> = _expression

    private val _result = MutableLiveData(DEFAULT_RESULT)
    val result: LiveData<String> = _result

    private var justEvaluated = false

    fun onInput(input: CalculatorInput) {
        when (input) {
            is CalculatorInput.Digit -> appendDigit(input.value)
            is CalculatorInput.Operator -> appendOperator(input.symbol)
            CalculatorInput.Clear -> clear()
            CalculatorInput.Backspace -> backspace()
            CalculatorInput.Equals -> evaluate()
        }
        updatePreviewResult()
    }

    private fun appendDigit(digit: Int) {
        val current = currentExpression()
        val next = if (justEvaluated) {
            justEvaluated = false
            digit.toString()
        } else {
            current + digit
        }
        _expression.value = next
    }

    private fun appendOperator(symbol: Char) {
        justEvaluated = false
        var current = currentExpression()
        if (current.isEmpty()) {
            if (symbol == '-') {
                _expression.value = "-"
            }
            return
        }
        val last = current.last()
        if (last in OPERATORS) {
            current = current.dropLast(1)
        }
        _expression.value = current + symbol
    }

    private fun clear() {
        justEvaluated = false
        _expression.value = ""
        _result.value = DEFAULT_RESULT
    }

    private fun backspace() {
        justEvaluated = false
        val current = currentExpression()
        if (current.isEmpty()) return
        _expression.value = current.dropLast(1)
    }

    private fun evaluate() {
        val current = currentExpression()
        if (current.isEmpty()) return

        val evaluated = CalculatorExpressionEvaluator.evaluate(current)
        if (evaluated != null) {
            _expression.value = evaluated
            _result.value = evaluated
            justEvaluated = true
        } else {
            _result.value = ERROR_RESULT
        }
    }

    private fun updatePreviewResult() {
        if (justEvaluated) return
        val current = currentExpression()
        if (current.isEmpty()) {
            _result.value = DEFAULT_RESULT
            return
        }
        val previewExpression = current.trimEnd { it in OPERATORS }
        if (previewExpression.isEmpty()) {
            _result.value = DEFAULT_RESULT
            return
        }
        val preview = CalculatorExpressionEvaluator.evaluate(previewExpression)
        _result.value = preview ?: DEFAULT_RESULT
    }

    private fun currentExpression(): String = _expression.value.orEmpty()

    companion object {
        private const val DEFAULT_RESULT = "0"
        private const val ERROR_RESULT = "Error"
        private val OPERATORS = setOf('+', '-', '*', '/')
    }
}
