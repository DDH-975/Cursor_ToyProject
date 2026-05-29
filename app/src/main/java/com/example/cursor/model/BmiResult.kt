package com.example.cursor.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.cursor.R

enum class BmiCategory(
    @StringRes val labelRes: Int,
    @ColorRes val colorRes: Int
) {
    UNDERWEIGHT(R.string.bmi_status_underweight, R.color.bmi_underweight),
    NORMAL(R.string.bmi_status_normal, R.color.bmi_normal),
    OVERWEIGHT(R.string.bmi_status_overweight, R.color.bmi_overweight),
    OBESE(R.string.bmi_status_obese, R.color.bmi_obese)
}

data class BmiResult(
    val bmi: Double,
    val category: BmiCategory
)
