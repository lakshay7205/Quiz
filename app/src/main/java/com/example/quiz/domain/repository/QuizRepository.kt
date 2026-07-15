package com.example.quiz.domain.repository

import com.example.quiz.data.util.ApiResult
import com.example.quiz.domain.model.Question

interface QuizRepository {
    suspend fun getQuestions(): ApiResult<List<Question>>
}
