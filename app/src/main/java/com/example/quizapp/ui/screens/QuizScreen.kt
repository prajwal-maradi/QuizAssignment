package com.example.quizapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.data.model.Question
import com.example.quizapp.ui.components.OptionCard
import com.example.quizapp.ui.components.OptionState
import com.example.quizapp.ui.components.StreakBadge
import com.example.quizapp.ui.components.StreakCelebration
import com.example.quizapp.viewmodel.QuizUiState

private val CardShape = RoundedCornerShape(16.dp)

/**
 * A composable that represents the main quiz screen.
 */
@Composable
fun QuizScreen(
    state: QuizUiState.Playing,
    streakCelebration: Int?,
    onOptionSelected: (Int) -> Unit,
    onSkip: () -> Unit
) {
    val question = state.currentQuestion

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TopBar(state = state)

            // Question card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = CardShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Question ${state.currentQuestionIndex + 1}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    )
                    Text(
                        text = question.question,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Options
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(
                    items = question.options,
                    key = { index, _ -> index }
                ) { index, option ->
                    val optionState = when {
                        !state.showAnswer -> OptionState.NORMAL
                        index == question.correctAnswerIndex -> OptionState.CORRECT
                        index == state.selectedAnswerIndex -> OptionState.WRONG
                        else -> OptionState.NORMAL
                    }
                    OptionCard(
                        label = ('A' + index).toString(),
                        optionText = option,
                        state = optionState,
                        onClick = { onOptionSelected(index) }
                    )
                }
            }

            // Skip button
            if (!state.showAnswer) {
                OutlinedButton(
                    onClick = onSkip,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                ) {
                    Text(
                        text = "Skip",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Skip",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Streak celebration overlay
        AnimatedVisibility(
            visible = streakCelebration != null,
            enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            StreakCelebration(streak = streakCelebration ?: 0)
        }
    }
}

/**
 * A composable that represents the top bar of the quiz screen, showing the current question index,
 * total questions, streak badge, and a progress bar.
 */
@Composable
private fun TopBar(state: QuizUiState.Playing) {
    val animatedProgress by animateFloatAsState(
        targetValue = state.progress,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "progress"
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${state.currentQuestionIndex + 1} / ${state.totalQuestions}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            StreakBadge(streak = state.streak)
        }

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Preview
@Composable
private fun QuizScreenPreview() {
    val sampleState = QuizUiState.Playing(
        questions = listOf(
            Question(
                id = 1,
                question = "What is the capital of France?",
                options = listOf("Berlin", "Madrid", "Paris", "Rome"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 2,
                question = "What is the largest planet in our solar system?",
                options = listOf("Earth", "Jupiter", "Mars", "Saturn"),
                correctAnswerIndex = 1
            ),
        ),
        currentQuestionIndex = 1,
        score = 2,
        skipped = 0,
        streak = 1,
        longestStreak = 1,
        selectedAnswerIndex = 1,
        showAnswer = true
    )
    QuizScreen(
        state = sampleState,
        streakCelebration = null,
        onOptionSelected = {},
        onSkip = {}
    )
}

@Preview
@Composable
private fun QuizScreenWithCelebrationPreview() {
    val sampleState = QuizUiState.Playing(
        questions = listOf(
            Question(
                id = 1,
                question = "What is the capital of France?",
                options = listOf("Berlin", "Madrid", "Paris", "Rome"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 2,
                question = "What is the largest planet in our solar system?",
                options = listOf("Earth", "Jupiter", "Mars", "Saturn"),
                correctAnswerIndex = 1
            ),
            Question(
                id = 3,
                question = "What is the chemical symbol for water?",
                options = listOf("H2O", "O2", "CO2", "NaCl"),
                correctAnswerIndex = 0
            )
        ),
        currentQuestionIndex = 2,
        score = 3,
        skipped = 0,
        streak = 3,
        longestStreak = 3,
        selectedAnswerIndex = 0,
        showAnswer = true
    )
    QuizScreen(
        state = sampleState,
        streakCelebration = 3,
        onOptionSelected = {},
        onSkip = {}
    )
}