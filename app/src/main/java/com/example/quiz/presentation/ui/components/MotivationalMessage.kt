package com.example.quiz.presentation.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.quiz.R

@Composable
fun MotivationalMessage(
    streakCount: Int,
    modifier: Modifier = Modifier
) {
    val message = when {
        streakCount > 0 && streakCount % 5 == 0 -> stringResource(R.string.perfect_moment)
        streakCount >= 6 -> stringResource(R.string.unstoppable)
        streakCount == 5 -> stringResource(R.string.quiz_master)
        streakCount == 4 -> stringResource(R.string.motivation_streak_4)
        streakCount == 3 -> stringResource(R.string.motivation_streak_3)
        streakCount == 2 -> stringResource(R.string.motivation_streak_2)
        streakCount == 1 -> stringResource(R.string.motivation_streak_1)
        else -> ""
    }

    AnimatedContent(
        targetState = message,
        transitionSpec = {
            (slideInVertically { height -> height } + fadeIn() togetherWith
                    slideOutVertically { height -> -height } + fadeOut())
                .using(SizeTransform(clip = false))
        },
        label = "MotivationalMessageAnimation"
    ) { targetMessage ->
        if (targetMessage.isNotEmpty()) {
            Text(
                text = targetMessage,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (streakCount >= 3) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                },
                modifier = modifier.padding(vertical = 8.dp)
            )
        }
    }
}
