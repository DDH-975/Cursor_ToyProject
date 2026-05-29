package com.example.cursor.model

data class GpaCourseItem(
    val id: String,
    val subjectName: String = "",
    val creditIndex: Int = 2,
    val gradeIndex: Int = 0
)
