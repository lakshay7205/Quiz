package com.example.quiz.presentation.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quiz.ui.theme.QuizCorrect
import com.example.quiz.ui.theme.QuizIncorrect

@Composable
fun AnswerOption(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showAnswer: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(
        targetValue = when {
            showAnswer && isCorrect -> QuizCorrect
            showAnswer && isSelected && !isCorrect -> QuizIncorrect
            isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
            else -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 400),
        label = "backgroundColor"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            showAnswer && (isCorrect || isSelected) -> Color.White
            isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
            else -> MaterialTheme.colorScheme.onSurface
        },
        label = "contentColor"
    )
    
    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.96f
            isSelected -> 1.02f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        contentColor = contentColor,
        interactionSource = interactionSource,
        tonalElevation = if (isSelected) 8.dp else 2.dp,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected && !showAnswer) 
                MaterialTheme.colorScheme.primary 
            else Color.White.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            AnimatedContent(
                targetState = when {
                    showAnswer && isCorrect -> "correct"
                    showAnswer && isSelected && !isCorrect -> "wrong"
                    isSelected -> "selected"
                    else -> "idle"
                },
                transitionSpec = {
                    scaleIn() togetherWith scaleOut()
                },
                label = "iconTransition"
            ) { state ->
                when (state) {
                    "correct" -> Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(24.dp))
                    "wrong" -> Icon(Icons.Default.Cancel, null, tint = Color.White, modifier = Modifier.size(24.dp))
                    "selected" -> Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.size(8.dp).background(Color.White, CircleShape))
                    }
                    else -> Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.White.copy(alpha = 0.1f), CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
