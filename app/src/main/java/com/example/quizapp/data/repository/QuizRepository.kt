package com.example.quizapp.data.repository

import com.example.quizapp.data.model.Question

/**
 * A repository interface for fetching quiz questions.
 */
fun interface QuizRepository {
    suspend fun getQuestions(): List<Question>
}