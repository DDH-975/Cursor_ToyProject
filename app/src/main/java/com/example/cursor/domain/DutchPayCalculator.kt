package com.example.cursor.domain

object DutchPayCalculator {

    fun calculatePerPerson(totalAmount: Long, peopleCount: Int): Long? {
        if (totalAmount < 0 || peopleCount <= 0) return null
        return (totalAmount + peopleCount - 1) / peopleCount
    }
}
