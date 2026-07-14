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

    private val _streakCelebrationEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val streakCelebrationEvent = _streakCelebrationEvent.asSharedFlow()

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
        val state = _uiState.value as? QuizUiState.Playing ?: return
        if (state.showAnswer) return

        val question = state.currentQuestion
        val correct = index == question.correctAnswerIndex
        val newStreak = if (correct) state.streak + 1 else 0
        val newLongest = maxOf(state.longestStreak, newStreak)

        _uiState.value = state.copy(
            selectedAnswerIndex = index,
            showAnswer = true,
            score = if (correct) state.score + 1 else state.score,
            streak = newStreak,
            longestStreak = newLongest
        )

        if (correct && newStreak >= 3 && newStreak % 3 == 0) {
            _streakCelebrationEvent.tryEmit(newStreak)
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
        val state = _uiState.value as? QuizUiState.Playing ?: return
        if (state.showAnswer) return
        _uiState.value = state.copy(skipped = state.skipped + 1, streak = 0)
        advanceToNext()
    }

    /**
     * Advances to the next question or finishes the quiz if there are no more questions.
     */
    private fun advanceToNext() {
        val state = _uiState.value as? QuizUiState.Playing ?: return
        if (state.currentQuestionIndex < state.questions.size - 1) {
            _uiState.value = state.copy(
                currentQuestionIndex = state.currentQuestionIndex + 1,
                selectedAnswerIndex = null,
                showAnswer = false
            )
        } else {
            _uiState.value = QuizUiState.Finished(
                score = state.score,
                totalQuestions = state.totalQuestions,
                skipped = state.skipped,
                longestStreak = state.longestStreak
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
