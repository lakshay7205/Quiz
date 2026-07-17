package com.example.quiz.presentation.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quiz.R
import com.example.quiz.presentation.ui.components.ResultStatCard
import com.example.quiz.ui.theme.QuizCorrect
import kotlinx.coroutines.delay

@Composable
fun ResultsScreen(
    correctCount: Int,
    totalCount: Int,
    skippedCount: Int,
    bestStreak: Int,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val percentage = if (totalCount > 0) (correctCount.toFloat() / totalCount * 100).toInt() else 0
    
    var animatedScore by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        delay(300)
        animate(0f, percentage.toFloat(), animationSpec = tween(1500, easing = FastOutSlowInEasing)) { value, _ ->
            animatedScore = value.toInt()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        IconButton(
            onClick = onRestart,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.quiz_results),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Text(
            text = stringResource(R.string.quiz_results),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 12.dp,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            CircularProgressIndicator(
                progress = { animatedScore / 100f },
                modifier = Modifier.fillMaxSize(),
                color = if (percentage >= 70) QuizCorrect else MaterialTheme.colorScheme.secondary,
                strokeWidth = 12.dp,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$animatedScore%",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.score_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 2.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = when {
                percentage >= 90 -> stringResource(R.string.performance_outstanding)
                percentage >= 70 -> stringResource(R.string.performance_great)
                percentage >= 50 -> stringResource(R.string.performance_well)
                else -> stringResource(R.string.performance_keep_practicing)
            },
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        val statsVisible = remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            delay(500)
            statsVisible.value = true
        }

        AnimatedVisibility(
            visible = statsVisible.value,
            enter = slideInVertically { it / 2 } + fadeIn(tween(600))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ResultStatCard(
                    label = stringResource(R.string.correct_answers),
                    value = "$correctCount/$totalCount",
                    icon = Icons.Default.CheckCircle,
                    iconColor = QuizCorrect,
                    modifier = Modifier.weight(1f)
                )
                ResultStatCard(
                    label = stringResource(R.string.best_streak),
                    value = "🔥 $bestStreak",
                    icon = Icons.Default.LocalFireDepartment,
                    iconColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AnimatedVisibility(
            visible = statsVisible.value,
            enter = slideInVertically { it / 2 } + fadeIn(tween(600, delayMillis = 200))
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.SkipNext, null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(stringResource(R.string.skipped_questions), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    Text(skippedCount.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onRestart,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(stringResource(R.string.restart_quiz), fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
