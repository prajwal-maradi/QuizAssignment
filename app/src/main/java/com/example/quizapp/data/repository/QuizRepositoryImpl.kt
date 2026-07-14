package com.example.quizapp.data.repository

import com.example.quizapp.data.FetchAndParseQuestions
import com.example.quizapp.data.model.Question

/**
 * Implementation of the [QuizRepository] interface that fetches and parses quiz questions.
 *
 * @property fetchAndParseQuestions The use case responsible for loading and parsing questions.
 */
class QuizRepositoryImpl(
    private val fetchAndParseQuestions: FetchAndParseQuestions
) : QuizRepository {

    override suspend fun getQuestions(): List<Question> {
        return fetchAndParseQuestions.loadQuestions()
    }
}