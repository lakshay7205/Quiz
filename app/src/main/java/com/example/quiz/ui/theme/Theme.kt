package com.example.quiz.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val QuizColorScheme = darkColorScheme(
    primary = QuizPrimary,
    onPrimary = Color.Black,
    secondary = QuizSecondary,
    onSecondary = Color.White,
    background = QuizBackground,
    surface = QuizSurface,
    onBackground = Color.White,
    onSurface = Color.White,
    error = QuizIncorrect,
    onError = Color.White,
    surfaceVariant = QuizSurface,
    onSurfaceVariant = QuizOnSurfaceMuted
)

@Composable
fun QuizTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = QuizColorScheme,
        typography = Typography,
        content = content
    )
}
