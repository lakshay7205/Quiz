package com.example.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.quiz.presentation.ui.QuizScreen
import com.example.quiz.presentation.viewmodel.QuizViewModel
import com.example.quiz.ui.theme.QuizTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the native splash screen transition
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep the splash screen on-screen until the ViewModel has finished loading questions
        splashScreen.setKeepOnScreenCondition {
            viewModel.uiState.value.isLoading
        }

        enableEdgeToEdge()
        setContent {
            QuizTheme {
                val uiState by viewModel.uiState.collectAsState()

                QuizScreen(
                    uiState = uiState,
                    onOptionSelected = { viewModel.onOptionSelected(it) },
                    onSkip = { viewModel.onSkip() },
                    onRetry = { viewModel.loadQuestions() },
                    onRestart = { viewModel.restartQuiz() }
                )
            }
        }
    }
}
