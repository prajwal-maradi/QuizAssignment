package com.example.quizapp.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.viewmodel.QuizUiState

/**
 * A composable that represents the result screen after a quiz is finished.
 *
 * @param state The current state of the quiz, which should be of type [QuizUiState.Finished].
 * @param onRestart A callback to be invoked when the user wants to restart the quiz.
 */
@Composable
fun ResultScreen(
    state: QuizUiState.Finished,
    onRestart: () -> Unit
) {
    val performanceIcon = when (state.performance) {
        QuizUiState.Performance.EXCELLENT -> Icons.Default.EmojiEvents
        QuizUiState.Performance.GREAT -> Icons.Default.Stars
        QuizUiState.Performance.GOOD -> Icons.Default.ThumbUp
        QuizUiState.Performance.PRACTICE -> Icons.AutoMirrored.Filled.MenuBook
    }

    var animTarget by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = animTarget,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "score_anim"
    )

    LaunchedEffect(Unit) {
        animTarget = state.percentage / 100f
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Header(icon = performanceIcon, message = state.performance.label)

        ScoreCircle(state = state, animatedProgress = animatedProgress)

        SummaryCard(state = state)

        Spacer(modifier = Modifier.weight(1f))

        RestartButton(onClick = onRestart)
    }
}

/**
 * A composable that displays the header section of the result screen, including an icon and a message.
 *
 * @param icon The icon to be displayed.
 * @param message The message to be displayed below the icon.
 */
@Composable
private fun Header(icon: ImageVector, message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = message,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

/**
 * A composable that displays the score circle, showing the user's score and percentage.
 *
 * @param state The current state of the quiz, which should be of type [QuizUiState.Finished].
 * @param animatedProgress The animated progress value representing the user's score percentage.
 */
@Composable
private fun ScoreCircle(state: QuizUiState.Finished, animatedProgress: Float) {
    Box(
        modifier = Modifier
            .size(160.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${state.score}/${state.totalQuestions}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

/**
 * A composable that displays a summary card with the user's quiz statistics.
 *
 * @param state The current state of the quiz, which should be of type [QuizUiState.Finished].
 */
@Composable
private fun SummaryCard(state: QuizUiState.Finished) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Quiz Summary",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            StatRow(
                icon = Icons.Default.CheckCircle,
                label = "Correct Answers",
                value = "${state.score}"
            )
            StatRow(
                icon = Icons.Default.Cancel,
                label = "Wrong Answers",
                value = "${state.wrongAnswers}"
            )
            StatRow(icon = Icons.Default.SkipNext, label = "Skipped", value = "${state.skipped}")
            StatRow(
                icon = Icons.Default.LocalFireDepartment,
                label = "Longest Streak",
                value = "${state.longestStreak}"
            )
        }
    }
}

/**
 * A composable that displays a single row of statistics in the summary card.
 *
 * @param icon The icon to be displayed for the statistic.
 * @param label The label describing the statistic.
 * @param value The value of the statistic.
 */
@Composable
private fun StatRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun RestartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Restart"
            )
            Text(
                text = "Restart Quiz",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview
@Composable
private fun ResultScreenExcellentPreview() {
    ResultScreen(
        state = QuizUiState.Finished(
            score = 10,
            totalQuestions = 10,
            skipped = 0,
            longestStreak = 10,
        ),
        onRestart = {}
    )
}

@Preview
@Composable
private fun ResultScreenGreatEffortPreview() {
    ResultScreen(
        state = QuizUiState.Finished(
            score = 7,
            totalQuestions = 10,
            skipped = 3,
            longestStreak = 4,
        ),
        onRestart = {}
    )
}

@Preview
@Composable
private fun ResultScreenGreatJobPreview() {
    ResultScreen(
        state = QuizUiState.Finished(
            score = 5,
            totalQuestions = 10,
            skipped = 3,
            longestStreak = 4,
        ),
        onRestart = {}
    )
}

@Preview
@Composable
private fun ResultScreenKeepPracticePreview() {
    ResultScreen(
        state = QuizUiState.Finished(
            score = 2,
            totalQuestions = 10,
            skipped = 3,
            longestStreak = 4,
        ),
        onRestart = {}
    )
}
