package com.example.cursor.ui.bmi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cursor.domain.BmiCalculator
import com.example.cursor.model.BmiResult

class BmiViewModel : ViewModel() {

    private val _result = MutableLiveData<BmiResult?>()
    val result: LiveData<BmiResult?> = _result

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    fun calculate(heightText: String, weightText: String) {
        val height = heightText.trim().toDoubleOrNull()
        val weight = weightText.trim().toDoubleOrNull()

        if (height == null || weight == null) {
            _result.value = null
            _error.value = true
            return
        }

        val bmiResult = BmiCalculator.calculate(height, weight)
        if (bmiResult == null) {
            _result.value = null
            _error.value = true
        } else {
            _result.value = bmiResult
            _error.value = false
        }
    }
}
