package com.example.quiz.data.remote

import com.example.quiz.data.model.QuestionDto
import com.example.quiz.data.util.ApiResult
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface RemoteQuizDataSource {
    suspend fun getQuestions(): ApiResult<List<QuestionDto>>
}

class RemoteQuizDataSourceImpl @Inject constructor(
    private val api: QuizApi
) : RemoteQuizDataSource {

    override suspend fun getQuestions(): ApiResult<List<QuestionDto>> {
        return try {
            val response = api.getQuestions()
            ApiResult.Success(response)
        } catch (e: IOException) {
            ApiResult.Error.Network(e)
        } catch (e: HttpException) {
            ApiResult.Error.Http(e.code(), e.message(), e)
        } catch (e: Exception) {
            ApiResult.Error.Unknown(e)
        }
    }
}
