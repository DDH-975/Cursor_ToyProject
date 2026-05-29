package com.example.cursor.ui.dutch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cursor.domain.DutchPayCalculator

class DutchPayViewModel : ViewModel() {

    private val _peopleCount = MutableLiveData(DEFAULT_PEOPLE)
    val peopleCount: LiveData<Int> = _peopleCount

    private val _perPersonAmount = MutableLiveData<Long?>()
    val perPersonAmount: LiveData<Long?> = _perPersonAmount

    private val _error = MutableLiveData(false)
    val error: LiveData<Boolean> = _error

    fun increasePeople() {
        val current = _peopleCount.value ?: DEFAULT_PEOPLE
        if (current < MAX_PEOPLE) {
            _peopleCount.value = current + 1
        }
    }

    fun decreasePeople() {
        val current = _peopleCount.value ?: DEFAULT_PEOPLE
        if (current > MIN_PEOPLE) {
            _peopleCount.value = current - 1
        }
    }

    fun setPeopleCount(count: Int) {
        _peopleCount.value = count.coerceIn(MIN_PEOPLE, MAX_PEOPLE)
    }

    fun calculate(totalAmountText: String, peopleCountText: String) {
        val totalAmount = totalAmountText.trim().toLongOrNull()
        val people = peopleCountText.trim().toIntOrNull()
            ?: _peopleCount.value
            ?: DEFAULT_PEOPLE

        val perPerson = if (totalAmount != null) {
            DutchPayCalculator.calculatePerPerson(totalAmount, people)
        } else {
            null
        }

        if (perPerson == null) {
            _perPersonAmount.value = null
            _error.value = true
        } else {
            _perPersonAmount.value = perPerson
            _error.value = false
            _peopleCount.value = people
        }
    }

    companion object {
        const val MIN_PEOPLE = 1
        const val MAX_PEOPLE = 99
        const val DEFAULT_PEOPLE = 2
    }
}
