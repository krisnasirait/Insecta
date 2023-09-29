package com.insecta.edu.data.model

data class DetailLessonItem(
    val lessonId: String,
    val lessonTitle: String,
    val lessonDescription: String,
    val lessonImages: List<String>,
    val lessonAudio: String
)