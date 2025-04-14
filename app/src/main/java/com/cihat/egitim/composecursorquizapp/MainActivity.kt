package com.cihat.egitim.composecursorquizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.cihat.egitim.composecursorquizapp.ui.QuizScreen
import com.cihat.egitim.composecursorquizapp.ui.theme.ComposeCursorQuizAppTheme
import com.cihat.egitim.composecursorquizapp.viewmodel.QuizViewModel

class MainActivity : ComponentActivity() {
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCursorQuizAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuizScreen(viewModel = quizViewModel)
                }
            }
        }
    }
}