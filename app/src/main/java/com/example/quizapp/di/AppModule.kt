package com.example.quizapp.di

import com.example.quizapp.data.FetchAndParseQuestions
import com.example.quizapp.data.repository.QuizRepository
import com.example.quizapp.data.repository.QuizRepositoryImpl
import com.example.quizapp.viewmodel.QuizViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { FetchAndParseQuestions(androidContext(), Dispatchers.IO) }

    single<QuizRepository> { QuizRepositoryImpl(get()) }

    viewModel { QuizViewModel(get()) }
}
