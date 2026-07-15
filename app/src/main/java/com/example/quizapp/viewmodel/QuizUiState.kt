package com.example.quizapp.viewmodel

import com.example.quizapp.Performance
import com.example.quizapp.data.model.Question

/**
 * Represents the different states of the quiz UI.
 */
sealed interface QuizUiState {

    data object Loading : QuizUiState

    data class Playing(
        val questions: List<Question>,
        val currentQuestionIndex: Int = 0,
        val score: Int = 0,
        val skipped: Int = 0,
        val streak: Int = 0,
        val longestStreak: Int = 0,
        val selectedAnswerIndex: Int? = null,
        val showAnswer: Boolean = false
    ) : QuizUiState {
        val totalQuestions: Int = questions.size
        val currentQuestion: Question = questions[currentQuestionIndex]
        val progress: Float = (currentQuestionIndex + 1f) / totalQuestions
    }

    data class Finished(
        val score: Int,
        val totalQuestions: Int,
        val skipped: Int,
        val longestStreak: Int
    ) : QuizUiState {
        val wrongAnswers: Int = totalQuestions - score - skipped
        val percentage: Float = if (totalQuestions > 0) score * 100f / totalQuestions else 0f
        val performance: Performance = Performance.evaluatePerformance(percentage)
    }
}
