package com.example.quiz.di

import com.example.quiz.data.remote.RemoteQuizDataSource
import com.example.quiz.data.remote.RemoteQuizDataSourceImpl
import com.example.quiz.data.repository.QuizRepositoryImpl
import com.example.quiz.domain.repository.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRemoteQuizDataSource(
        remoteQuizDataSourceImpl: RemoteQuizDataSourceImpl
    ): RemoteQuizDataSource

    @Binds
    @Singleton
    abstract fun bindQuizRepository(
        quizRepositoryImpl: QuizRepositoryImpl
    ): QuizRepository
}
