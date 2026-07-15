package com.example.quiz.domain.usecase

import com.example.quiz.data.util.ApiResult
import com.example.quiz.domain.model.Question
import com.example.quiz.domain.repository.QuizRepository
import javax.inject.Inject

/**
 * Use Case to fetch quiz questions from the repository.
 * Follows the Single Responsibility Principle.
 */
class GetQuestionsUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    suspend operator fun invoke(): ApiResult<List<Question>> {
        return repository.getQuestions()
    }
}
