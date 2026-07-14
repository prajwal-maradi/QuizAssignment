package com.example.quizapp.data.model

import androidx.compose.runtime.Immutable

/**
 * Represents a quiz question with its options and the index of the correct answer.
 */
@Immutable
data class Question(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)