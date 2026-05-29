package com.example.cursor.ui.calculator

sealed class CalculatorInput {
    data class Digit(val value: Int) : CalculatorInput()
    data class Operator(val symbol: Char) : CalculatorInput()
    data object Clear : CalculatorInput()
    data object Backspace : CalculatorInput()
    data object Equals : CalculatorInput()
}
