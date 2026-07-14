package com.example.quizapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val StreakGold = Color(0xFFFFD700)
private val StreakGoldBackground = StreakGold.copy(alpha = 0.15f)

/**
 * A composable that displays a streak badge.
 *
 * @param streak The current streak count.
 * @param modifier The modifier to be applied to the badge.
 */
@Composable
fun StreakBadge(streak: Int, modifier: Modifier = Modifier) {
    val isOnFire = streak >= 3

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                if (isOnFire) StreakGoldBackground
                else MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = if (isOnFire) Icons.Default.Whatshot else Icons.Default.AutoAwesome,
            contentDescription = if (isOnFire) "On fire" else "Streak",
            tint = if (isOnFire) StreakGold else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = "$streak streak",
            style = if (isOnFire) MaterialTheme.typography.labelLarge
            else MaterialTheme.typography.labelMedium,
            color = if (isOnFire) StreakGold else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun StreakCelebration(streak: Int) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Whatshot,
                contentDescription = "On fire",
                tint = StreakGold,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "$streak in a row!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "You're on fire! Keep going!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun StreakBadgePreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        StreakBadge(streak = 1)
        StreakBadge(streak = 2)
        StreakBadge(streak = 3)
        StreakBadge(streak = 5)
    }
}
