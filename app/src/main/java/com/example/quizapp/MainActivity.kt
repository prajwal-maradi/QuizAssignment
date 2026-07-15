package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quizapp.ui.screens.QuizScreen
import com.example.quizapp.ui.screens.ResultScreen
import com.example.quizapp.ui.screens.SplashScreen
import com.example.quizapp.ui.theme.QuizAppTheme
import com.example.quizapp.viewmodel.QuizUiState
import com.example.quizapp.viewmodel.QuizViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                QuizApp()
            }
        }
    }
}

@Composable
fun QuizApp() {
    val viewModel: QuizViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val celebrationStreak by viewModel.streakCelebrationEvent.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        when (val uiState = state) {
            QuizUiState.Loading ->
                SplashScreen()

            is QuizUiState.Playing ->
                QuizScreen(
                    state = uiState,
                    streakCelebration = celebrationStreak,
                    onOptionSelected = viewModel::selectOption,
                    onSkip = viewModel::skipQuestion
                )

            is QuizUiState.Finished ->
                ResultScreen(
                    state = uiState,
                    onRestart = viewModel::restartQuiz
                )
        }
    }
}