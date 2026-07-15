package com.example.quizapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.repository.QuizRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val repository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val _streakCelebrationEvent = MutableStateFlow<Int?>(null)
    val streakCelebrationEvent = _streakCelebrationEvent.asStateFlow()

    init {
        loadQuiz()
    }

    /**
     * Loads the quiz questions from the repository and updates the UI state to Playing.
     */
    private fun loadQuiz() {
        viewModelScope.launch {
            val questions = repository.getQuestions().shuffled()
            // delay(1000) // Simulate loading delay to show the splash screen
            _uiState.value = QuizUiState.Playing(questions = questions)
        }
    }

    /**
     * Handles the selection of an answer option by the user.
     *
     * @param index The index of the selected answer option.
     */
    fun selectOption(index: Int) {
        val quizUiStatePlaying = _uiState.value as? QuizUiState.Playing ?: return
        if (quizUiStatePlaying.showAnswer) return

        val question = quizUiStatePlaying.currentQuestion
        val correct = index == question.correctAnswerIndex
        val newStreak = if (correct) quizUiStatePlaying.streak + 1 else 0
        val newLongest = maxOf(quizUiStatePlaying.longestStreak, newStreak)

        _uiState.value = quizUiStatePlaying.copy(
            selectedAnswerIndex = index,
            showAnswer = true,
            score = if (correct) quizUiStatePlaying.score + 1 else quizUiStatePlaying.score,
            streak = newStreak,
            longestStreak = newLongest
        )

        if (correct && newStreak >= 3 && newStreak % 3 == 0) {
            _streakCelebrationEvent.value = newStreak
            viewModelScope.launch {
                delay(2000)
                _streakCelebrationEvent.value = null
            }
        }

        viewModelScope.launch {
            delay(2000)
            advanceToNext()
        }
    }

    /**
     * Skips the current question and advances to the next one.
     */
    fun skipQuestion() {
        val quizUiStatePlaying = _uiState.value as? QuizUiState.Playing ?: return
        if (quizUiStatePlaying.showAnswer) return
        _uiState.value = quizUiStatePlaying.copy(skipped = quizUiStatePlaying.skipped + 1, streak = 0)
        advanceToNext()
    }

    /**
     * Advances to the next question or finishes the quiz if there are no more questions.
     */
    private fun advanceToNext() {
        val quizUiStatePlaying = _uiState.value as? QuizUiState.Playing ?: return
        if (quizUiStatePlaying.currentQuestionIndex < quizUiStatePlaying.questions.size - 1) {
            _uiState.value = quizUiStatePlaying.copy(
                currentQuestionIndex = quizUiStatePlaying.currentQuestionIndex + 1,
                selectedAnswerIndex = null,
                showAnswer = false
            )
        } else {
            _uiState.value = QuizUiState.Finished(
                score = quizUiStatePlaying.score,
                totalQuestions = quizUiStatePlaying.totalQuestions,
                skipped = quizUiStatePlaying.skipped,
                longestStreak = quizUiStatePlaying.longestStreak
            )
        }
    }

    /**
     * Restarts the quiz by resetting the UI state to Loading and reloading the questions.
     */
    fun restartQuiz() {
        _uiState.value = QuizUiState.Loading
        loadQuiz()
    }
}
