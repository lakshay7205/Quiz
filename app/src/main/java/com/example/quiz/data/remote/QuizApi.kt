package com.example.quiz.data.remote

import com.example.quiz.data.model.QuestionDto
import retrofit2.http.GET

interface QuizApi {

    @GET("53846277a8fcb034e482906ccc0d12b2/raw")
    suspend fun getQuestions(): List<QuestionDto>

    companion object {
        const val BASE_URL = "https://gist.githubusercontent.com/dr-samrat/"
    }
}
