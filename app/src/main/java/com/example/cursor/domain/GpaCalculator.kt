package com.example.cursor.domain

import com.example.cursor.model.GpaCourseItem

object GpaCalculator {

    private val CREDITS = intArrayOf(1, 2, 3)
    private val GRADE_POINTS = doubleArrayOf(4.5, 4.0, 3.5, 3.0, 2.5, 2.0, 1.5, 1.0, 0.0)

    fun calculate(courses: List<GpaCourseItem>): Double? {
        if (courses.isEmpty()) return null

        var totalPoints = 0.0
        var totalCredits = 0

        for (course in courses) {
            val credit = CREDITS.getOrNull(course.creditIndex) ?: continue
            val gradePoint = GRADE_POINTS.getOrNull(course.gradeIndex) ?: continue
            totalPoints += gradePoint * credit
            totalCredits += credit
        }

        if (totalCredits == 0) return null
        return totalPoints / totalCredits
    }
}
