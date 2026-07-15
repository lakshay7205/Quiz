package com.example.quiz.data.mapper

import com.example.quiz.data.model.QuestionDto
import com.example.quiz.domain.model.Question

fun QuestionDto.toDomain(): Question {
    return Question(
        id = id,
        question = question,
        options = options,
        correctOptionIndex = correctOptionIndex
    )
}

fun List<QuestionDto>.toDomainList(): List<Question> {
    return map { it.toDomain() }
}
