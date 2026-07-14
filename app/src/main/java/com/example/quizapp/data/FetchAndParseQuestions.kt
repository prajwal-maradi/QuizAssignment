package com.example.quizapp.data

import android.content.Context
import com.example.quizapp.data.model.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class FetchAndParseQuestions(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) {
    private val gson = Gson()
    private var cachedQuestions: List<Question>? = null
    private val mutex = Mutex()

    /**
     * Loads quiz questions from the local JSON file and caches them for future use.
     *
     * @return A list of [Question] objects parsed from the JSON file.
     */
    suspend fun loadQuestions(): List<Question> = mutex.withLock {
        cachedQuestions?.let { return it }

        val questions = withContext(ioDispatcher) {
            val json = context.assets
                .open("questions.json")
                .bufferedReader()
                .use { it.readText() }
            val type = object : TypeToken<List<Question>>() {}.type
            gson.fromJson<List<Question>>(json, type)
        }
        cachedQuestions = questions
        questions
    }
}