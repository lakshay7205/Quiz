package com.example.quiz.data.repository

import com.example.quiz.data.mapper.toDomainList
import com.example.quiz.data.remote.RemoteQuizDataSource
import com.example.quiz.data.util.ApiResult
import com.example.quiz.domain.model.Question
import com.example.quiz.domain.repository.QuizRepository
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteQuizDataSource
) : QuizRepository {

    override suspend fun getQuestions(): ApiResult<List<Question>> {
        return when (val result = remoteDataSource.getQuestions()) {
            is ApiResult.Success -> {
                ApiResult.Success(result.data.toDomainList())
            }
            is ApiResult.Error -> result
        }
    }
}
