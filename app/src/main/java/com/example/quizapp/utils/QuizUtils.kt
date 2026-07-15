package com.example.quizapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.quizapp.viewmodel.QuizUiState

/**
 * Represents the state of an option in a quiz.
 */
enum class OptionState {
    NORMAL,
    CORRECT,
    WRONG
}

/**
 * Returns the appropriate performance icon based on the quiz performance state.
 *
 */
fun getPerformanceIcon(state: QuizUiState.Finished): ImageVector {
    return when (state.performance) {
        Performance.EXCELLENT -> Icons.Default.EmojiEvents
        Performance.GREAT -> Icons.Default.Stars
        Performance.GOOD -> Icons.Default.ThumbUp
        Performance.PRACTICE -> Icons.AutoMirrored.Filled.MenuBook
    }
}

/**
 * Represents the performance level based on the user's score percentage.
 */
enum class Performance(val label: String) {
    EXCELLENT("Excellent!"),
    GREAT("Great Job!"),
    GOOD("Good Effort!"),
    PRACTICE("Keep Practicing!");

    companion object {
        fun evaluatePerformance(percentage: Float): Performance = when {
            percentage >= 80 -> EXCELLENT
            percentage >= 60 -> GREAT
            percentage >= 40 -> GOOD
            else -> PRACTICE
        }
    }
}