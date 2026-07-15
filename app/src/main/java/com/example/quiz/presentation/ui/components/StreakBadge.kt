package com.example.quiz.presentation.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.quiz.R
import com.example.quiz.ui.theme.QuizSecondaryVariant

@Composable
fun StreakBadge(
    streakCount: Int,
    modifier: Modifier = Modifier
) {
    val isSuperStreak = streakCount >= 3

    val infiniteTransition = rememberInfiniteTransition(label = "streak_pulse")
    val pulseScale by if (isSuperStreak) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse"
        )
    } else {
        remember { mutableStateOf(1f) }
    }

    AnimatedVisibility(
        visible = streakCount > 0,
        enter = fadeIn() + scaleIn(initialScale = 0.8f),
        exit = fadeOut() + scaleOut(targetScale = 0.8f),
        modifier = modifier
    ) {
        val backgroundColor = if (isSuperStreak) {
            Brush.horizontalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.secondary,
                    QuizSecondaryVariant
                )
            )
        } else {
            null
        }

        val contentColor = if (isSuperStreak) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

        Surface(
            modifier = Modifier
                .scale(pulseScale)
                .clip(CircleShape),
            shape = CircleShape,
            color = Color.Transparent,
            border = if (!isSuperStreak) BorderStroke(1.dp, contentColor.copy(alpha = 0.3f)) else null
        ) {
            Box(
                modifier = Modifier
                    .then(
                        if (isSuperStreak) Modifier.background(backgroundColor!!)
                        else Modifier.background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = stringResource(R.string.on_fire),
                        tint = if (isSuperStreak) Color.White else MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    val badgeText = when {
                        streakCount >= 5 -> stringResource(R.string.master_streak, streakCount)
                        streakCount >= 3 -> stringResource(R.string.on_fire_multiplier, streakCount)
                        else -> streakCount.toString()
                    }
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = contentColor
                    )
                }
            }
        }
    }
}
