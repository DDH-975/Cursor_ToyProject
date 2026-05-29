package com.example.cursor.domain

object CalculatorExpressionEvaluator {

    fun evaluate(expression: String): String? {
        val trimmed = expression.trim()
        if (trimmed.isEmpty()) return null

        return try {
            val tokens = tokenize(trimmed) ?: return null
            if (tokens.last() is Token.Operator) return null
            val result = evaluateTokens(tokens)
            formatResult(result)
        } catch (_: ArithmeticException) {
            null
        } catch (_: IllegalArgumentException) {
            null
        } catch (_: IndexOutOfBoundsException) {
            null
        }
    }

    private fun tokenize(expression: String): List<Token>? {
        val tokens = mutableListOf<Token>()
        var index = 0
        while (index < expression.length) {
            val char = expression[index]
            when {
                char.isWhitespace() -> index++
                char.isDigit() || char == '.' -> {
                    val start = index
                    while (index < expression.length &&
                        (expression[index].isDigit() || expression[index] == '.')
                    ) {
                        index++
                    }
                    tokens.add(Token.Number(expression.substring(start, index).toDouble()))
                }
                char in OPERATORS -> {
                    if (tokens.isNotEmpty() && tokens.last() is Token.Operator && char != '-') {
                        tokens[tokens.lastIndex] = Token.Operator(char)
                    } else {
                        tokens.add(Token.Operator(char))
                    }
                    index++
                }
                else -> return null
            }
        }
        if (tokens.isEmpty()) return null
        if (tokens.first() is Token.Operator && (tokens.first() as Token.Operator).symbol != '-') {
            return null
        }
        return tokens
    }

    private fun evaluateTokens(tokens: List<Token>): Double {
        val numbers = ArrayDeque<Double>()
        val operators = ArrayDeque<Char>()

        var index = 0
        if (tokens[index] is Token.Operator) {
            numbers.addLast(0.0)
        } else {
            numbers.addLast((tokens[index] as Token.Number).value)
            index++
        }

        while (index < tokens.size) {
            if (tokens[index] !is Token.Operator) return numbers.last()
            val operator = (tokens[index] as Token.Operator).symbol
            index++
            if (index >= tokens.size || tokens[index] !is Token.Number) {
                break
            }
            val number = (tokens[index] as Token.Number).value
            index++

            while (operators.isNotEmpty() && precedence(operators.last()) >= precedence(operator)) {
                applyTopOperator(numbers, operators.removeLast())
            }
            operators.addLast(operator)
            numbers.addLast(number)
        }

        while (operators.isNotEmpty()) {
            applyTopOperator(numbers, operators.removeLast())
        }
        return numbers.last()
    }

    private fun applyTopOperator(numbers: ArrayDeque<Double>, operator: Char) {
        val right = numbers.removeLast()
        val left = numbers.removeLast()
        val value = when (operator) {
            '+' -> left + right
            '-' -> left - right
            '*' -> left * right
            '/' -> {
                if (right == 0.0) throw ArithmeticException("Division by zero")
                left / right
            }
            else -> throw IllegalArgumentException("Unknown operator")
        }
        numbers.addLast(value)
    }

    private fun precedence(operator: Char): Int = when (operator) {
        '+', '-' -> 1
        '*', '/' -> 2
        else -> 0
    }

    private fun formatResult(value: Double): String {
        return if (value.isFinite() && value % 1.0 == 0.0) {
            value.toLong().toString()
        } else {
            value.toString()
        }
    }

    private sealed interface Token {
        data class Number(val value: Double) : Token
        data class Operator(val symbol: Char) : Token
    }

    private val OPERATORS = setOf('+', '-', '*', '/')
}
