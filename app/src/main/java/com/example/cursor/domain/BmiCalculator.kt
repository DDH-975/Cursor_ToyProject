package com.example.cursor.domain

import com.example.cursor.model.BmiCategory
import com.example.cursor.model.BmiResult

object BmiCalculator {

    fun calculate(heightCm: Double, weightKg: Double): BmiResult? {
        if (heightCm <= 0 || weightKg <= 0) return null

        val heightM = heightCm / 100.0
        val bmi = weightKg / (heightM * heightM)
        val category = when {
            bmi < 18.5 -> BmiCategory.UNDERWEIGHT
            bmi < 23.0 -> BmiCategory.NORMAL
            bmi < 25.0 -> BmiCategory.OVERWEIGHT
            else -> BmiCategory.OBESE
        }
        return BmiResult(bmi, category)
    }
}
