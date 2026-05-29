package com.example.cursor.ui.gpa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cursor.domain.GpaCalculator
import com.example.cursor.model.GpaCourseItem
import java.util.UUID

class GpaViewModel : ViewModel() {

    private val coursesCache = mutableListOf<GpaCourseItem>()

    private val _courses = MutableLiveData<List<GpaCourseItem>>(emptyList())
    val courses: LiveData<List<GpaCourseItem>> = _courses

    private val _gpaResult = MutableLiveData<Double?>()
    val gpaResult: LiveData<Double?> = _gpaResult

    private val _gpaError = MutableLiveData<Boolean>()
    val gpaError: LiveData<Boolean> = _gpaError

    fun addCourse() {
        coursesCache.add(GpaCourseItem(id = UUID.randomUUID().toString()))
        publishCourses()
    }

    fun removeCourse(courseId: String) {
        val removed = coursesCache.removeAll { it.id == courseId }
        if (removed) {
            publishCourses()
        }
    }

    /** 학점/성적 변경 시에만 UI 리스트를 갱신한다. */
    fun updateCourseSelection(updated: GpaCourseItem) {
        val index = coursesCache.indexOfFirst { it.id == updated.id }
        if (index == -1) return

        coursesCache[index] = coursesCache[index].copy(
            creditIndex = updated.creditIndex,
            gradeIndex = updated.gradeIndex,
            subjectName = updated.subjectName
        )
        publishCourses()
    }

    /** 과목명 입력 시 캐시만 갱신하고 RecyclerView 재바인딩은 하지 않는다. */
    fun updateCourseSubjectName(courseId: String, subjectName: String) {
        val index = coursesCache.indexOfFirst { it.id == courseId }
        if (index == -1) return

        val current = coursesCache[index]
        if (current.subjectName == subjectName) return

        coursesCache[index] = current.copy(subjectName = subjectName)
    }

    fun calculateGpa() {
        if (coursesCache.isEmpty()) {
            _gpaResult.value = null
            _gpaError.value = true
            return
        }

        val gpa = GpaCalculator.calculate(coursesCache)
        if (gpa == null) {
            _gpaResult.value = null
            _gpaError.value = true
        } else {
            _gpaResult.value = gpa
            _gpaError.value = false
        }
    }

    private fun publishCourses() {
        _courses.value = coursesCache.toList()
    }
}
