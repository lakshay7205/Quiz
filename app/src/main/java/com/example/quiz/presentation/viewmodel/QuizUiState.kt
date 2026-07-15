package com.example.quiz.presentation.viewmodel

import com.example.quiz.domain.model.Question

data class QuizUiState(
    val isLoading: Boolean = true,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val showAnswer: Boolean = false,
    val error: String? = null,
    val isQuizFinished: Boolean = false,
    val streakCount: Int = 0,
    val correctCount: Int = 0,
    val skippedCount: Int = 0,
    val bestStreak: Int = 0
)
