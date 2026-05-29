package com.example.cursor.ui.gpa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cursor.domain.GpaCalculator
import com.example.cursor.model.GpaCourseItem
import java.util.UUID

class GpaViewModel : ViewModel() {

    private val _courses = MutableLiveData<List<GpaCourseItem>>(emptyList())
    val courses: LiveData<List<GpaCourseItem>> = _courses

    private val _gpaResult = MutableLiveData<Double?>()
    val gpaResult: LiveData<Double?> = _gpaResult

    private val _gpaError = MutableLiveData<Boolean>()
    val gpaError: LiveData<Boolean> = _gpaError

    fun addCourse() {
        val newCourse = GpaCourseItem(id = UUID.randomUUID().toString())
        _courses.value = _courses.value.orEmpty() + newCourse
    }

    fun updateCourse(updated: GpaCourseItem) {
        _courses.value = _courses.value.orEmpty().map { course ->
            if (course.id == updated.id) updated else course
        }
    }

    fun calculateGpa() {
        val courseList = _courses.value.orEmpty()
        if (courseList.isEmpty()) {
            _gpaResult.value = null
            _gpaError.value = true
            return
        }

        val gpa = GpaCalculator.calculate(courseList)
        if (gpa == null) {
            _gpaResult.value = null
            _gpaError.value = true
        } else {
            _gpaResult.value = gpa
            _gpaError.value = false
        }
    }
}
