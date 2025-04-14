package com.cihat.egitim.composecursorquizapp.model

data class Question(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int
)