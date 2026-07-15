package com.example.quiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz.data.util.ApiResult
import com.example.quiz.domain.model.Question
import com.example.quiz.domain.usecase.GetQuestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
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

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var isTransitioning = false

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            _uiState.value = QuizUiState(isLoading = true)
            when (val result = getQuestionsUseCase()) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, questions = result.data) }
                }
                is ApiResult.Error -> {
                    val errorMessage = when (result) {
                        is ApiResult.Error.Network -> "Check your internet connection"
                        is ApiResult.Error.Http -> "Server error: ${result.code}"
                        is ApiResult.Error.Unknown -> "An unexpected error occurred"
                    }
                    _uiState.update { it.copy(isLoading = false, error = errorMessage) }
                }
            }
        }
    }

    fun onOptionSelected(index: Int) {
        if (_uiState.value.showAnswer || isTransitioning) return

        val currentQuestion = _uiState.value.questions.getOrNull(_uiState.value.currentQuestionIndex)
        val isCorrect = index == currentQuestion?.correctOptionIndex

        _uiState.update { state ->
            val newStreak = if (isCorrect) state.streakCount + 1 else 0
            state.copy(
                selectedOptionIndex = index,
                showAnswer = true,
                correctCount = if (isCorrect) state.correctCount + 1 else state.correctCount,
                streakCount = newStreak,
                bestStreak = max(state.bestStreak, newStreak)
            )
        }

        viewModelScope.launch {
            isTransitioning = true
            delay(2000)
            moveToNextQuestion()
            isTransitioning = false
        }
    }

    fun onSkip() {
        if (isTransitioning || _uiState.value.showAnswer) return
        _uiState.update { it.copy(skippedCount = it.skippedCount + 1, streakCount = 0) }
        moveToNextQuestion()
    }

    private fun moveToNextQuestion() {
        _uiState.update { state ->
            val nextIndex = state.currentQuestionIndex + 1
            if (nextIndex < state.questions.size) {
                state.copy(
                    currentQuestionIndex = nextIndex,
                    selectedOptionIndex = null,
                    showAnswer = false
                )
            } else {
                state.copy(isQuizFinished = true)
            }
        }
    }

    fun restartQuiz() {
        _uiState.update { state ->
            state.copy(
                currentQuestionIndex = 0,
                selectedOptionIndex = null,
                showAnswer = false,
                isQuizFinished = false,
                streakCount = 0,
                correctCount = 0,
                skippedCount = 0,
                bestStreak = 0
            )
        }
    }
}
