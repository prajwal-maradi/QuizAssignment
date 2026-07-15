package com.example.quizapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.utils.OptionState

private val CorrectAnswerCardColor = Color(0xFF4CAF50)
private val WrongAnswerCardColor = Color(0xFFE53935)

/**
 * A composable that represents a single option card in a quiz.
 *
 * @param label The label for the option (e.g., "A", "B", "C", "D").
 * @param optionText The text of the option.
 * @param state The current state of the option (NORMAL, CORRECT, WRONG).
 * @param onClick A callback to be invoked when the option is clicked.
 */
@Composable
fun OptionCard(
    label: String,
    optionText: String,
    state: OptionState,
    onClick: () -> Unit
) {
    val showCorrect = state == OptionState.CORRECT
    val showWrong = state == OptionState.WRONG
    val isRevealed = showCorrect || showWrong
    val shape = RoundedCornerShape(12.dp)

    val backgroundColor = when {
        showCorrect -> CorrectAnswerCardColor.copy(alpha = 0.12f)
        showWrong -> WrongAnswerCardColor.copy(alpha = 0.12f)
        else -> MaterialTheme.colorScheme.surface
    }

    val borderColor = when {
        showCorrect -> CorrectAnswerCardColor
        showWrong -> WrongAnswerCardColor
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    val labelBgColor = when {
        showCorrect -> CorrectAnswerCardColor
        showWrong -> WrongAnswerCardColor
        else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    }

    val labelTextColor = when {
        isRevealed -> Color.White
        else -> MaterialTheme.colorScheme.primary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundColor)
            .border(1.5.dp, borderColor, shape)
            .clickable(
                enabled = !isRevealed,
                onClick = onClick
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(labelBgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = labelTextColor
            )
        }

        Text(
            text = optionText,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        if (showCorrect) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Correct",
                tint = CorrectAnswerCardColor
            )
        } else if (showWrong) {
            Icon(
                imageVector = Icons.Default.Cancel,
                contentDescription = "Wrong",
                tint = WrongAnswerCardColor
            )
        }
    }
}

@Preview
@Composable
private fun OptionCardPreview() {
    OptionCard(
        label = "A",
        optionText = "Sample Option Text",
        state = OptionState.NORMAL,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun OptionCardCorrectPreview() {
    OptionCard(
        label = "D",
        optionText = "Sample Option Text 2",
        state = OptionState.CORRECT,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun OptionCardWrongPreview() {
    OptionCard(
        label = "B",
        optionText = "Sample Option Text 3",
        state = OptionState.WRONG,
        onClick = {}
    )
}
