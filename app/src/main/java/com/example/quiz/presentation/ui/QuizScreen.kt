package com.example.quiz.presentation.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.quiz.R
import com.example.quiz.domain.model.Question
import com.example.quiz.presentation.ui.components.*
import com.example.quiz.presentation.viewmodel.QuizUiState
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun QuizScreen(
    uiState: QuizUiState,
    onOptionSelected: (Int) -> Unit,
    onSkip: () -> Unit,
    onRetry: () -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AnimatedContent(
                targetState = when {
                    uiState.error != null -> "error"
                    uiState.isLoading -> "loading"
                    uiState.questions.isEmpty() -> "empty"
                    uiState.isQuizFinished -> "finished"
                    else -> "quiz"
                },
                transitionSpec = {
                    fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
                },
                label = "state_transition"
            ) { state ->
                when (state) {
                    "loading" -> SplashScreen(modifier = Modifier.fillMaxSize())
                    "error" -> ErrorView(
                        message = uiState.error ?: stringResource(R.string.oops),
                        onRetry = onRetry,
                        modifier = Modifier.fillMaxSize()
                    )
                    "empty" -> EmptyView(
                        onRetry = onRetry,
                        modifier = Modifier.fillMaxSize()
                    )
                    "finished" -> ResultsScreen(
                        correctCount = uiState.correctCount,
                        totalCount = uiState.questions.size,
                        skippedCount = uiState.skippedCount,
                        bestStreak = uiState.bestStreak,
                        onRestart = onRestart,
                        modifier = Modifier.fillMaxSize()
                    )
                    "quiz" -> QuizContent(
                        uiState = uiState,
                        onOptionSelected = onOptionSelected,
                        onSkip = onSkip,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizContent(
    uiState: QuizUiState,
    onOptionSelected: (Int) -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.quiz_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            
            StreakBadge(streakCount = uiState.streakCount)
        }
        
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            ProgressHeader(
                currentIndex = uiState.currentQuestionIndex,
                totalCount = uiState.questions.size
            )
        }

        var offsetX by remember { mutableFloatStateOf(0f) }
        
        AnimatedContent(
            targetState = uiState.currentQuestionIndex,
            transitionSpec = {
                (slideInHorizontally { it } + fadeIn()) togetherWith
                        (slideOutHorizontally { -it } + fadeOut())
            },
            label = "question_slide",
            modifier = Modifier
                .weight(1f)
                .pointerInput(uiState.showAnswer) {
                    if (!uiState.showAnswer) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (offsetX < -200) {
                                    onSkip()
                                }
                                offsetX = 0f
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                offsetX += dragAmount
                            }
                        )
                    }
                }
                .offset { IntOffset(offsetX.roundToInt(), 0) }
        ) { index ->
            QuestionCard(
                question = uiState.questions[index],
                selectedOptionIndex = uiState.selectedOptionIndex,
                showAnswer = uiState.showAnswer,
                streakCount = uiState.streakCount,
                onOptionSelected = onOptionSelected,
                onSkip = onSkip
            )
        }
    }
}

@Composable
private fun QuestionCard(
    question: Question,
    selectedOptionIndex: Int?,
    showAnswer: Boolean,
    streakCount: Int,
    onOptionSelected: (Int) -> Unit,
    onSkip: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = question.question,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(48.dp))

        var itemsVisible by remember(question) { mutableIntStateOf(0) }
        LaunchedEffect(question) {
            for (i in 1..question.options.size) {
                delay(100L)
                itemsVisible = i
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            question.options.forEachIndexed { index, option ->
                AnimatedVisibility(
                    visible = itemsVisible > index,
                    enter = slideInHorizontally { it / 2 } + fadeIn(tween(400)),
                    exit = fadeOut()
                ) {
                    AnswerOption(
                        text = option,
                        isSelected = selectedOptionIndex == index,
                        isCorrect = index == question.correctOptionIndex,
                        showAnswer = showAnswer,
                        enabled = !showAnswer,
                        onClick = { onOptionSelected(index) }
                    )
                }
            }
        }

        MotivationalMessage(streakCount = streakCount)

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = onSkip,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 16.dp),
            enabled = !showAnswer
        ) {
            Text(
                stringResource(R.string.skip_question),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
