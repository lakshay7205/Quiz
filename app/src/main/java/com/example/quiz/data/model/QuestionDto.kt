package com.example.quiz.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    @SerialName("id")
    val id: Int,
    @SerialName("question")
    val question: String,
    @SerialName("options")
    val options: List<String>,
    @SerialName("correctOptionIndex")
    val correctOptionIndex: Int
)