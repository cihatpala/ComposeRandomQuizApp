package com.cihat.egitim.composecursorquizapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.cihat.egitim.composecursorquizapp.data.api.QuizApi
import com.cihat.egitim.composecursorquizapp.data.model.QuizQuestion
import com.cihat.egitim.composecursorquizapp.model.Question
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class QuizViewModel : ViewModel() {
    private var allQuestions = listOf<Question>()
    private var wrongQuestions = mutableListOf<Question>()
    private var currentQuestions: List<Question> = listOf()
    
    var currentQuestionIndex by mutableStateOf(0)
        private set
    
    var score by mutableStateOf(0)
        private set
    
    var isQuizFinished by mutableStateOf(false)
        private set
    
    var selectedAnswer by mutableStateOf<Int?>(null)
        private set

    var isAnswerChecked by mutableStateOf(false)
        private set

    var isCurrentAnswerCorrect by mutableStateOf(false)
        private set

    var isReviewMode by mutableStateOf(false)
        private set

    var correctQuestionsCount by mutableStateOf(0)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadQuizQuestions()
    }

    private fun loadQuizQuestions() {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                val response = QuizApi.service.getQuizQuestions()
                allQuestions = response.results.mapIndexed { index, quizQuestion ->
                    convertToQuestion(index, quizQuestion)
                }
                currentQuestions = allQuestions
                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Quiz yüklenirken bir hata oluştu: ${e.message}"
                isLoading = false
            }
        }
    }

    private fun convertToQuestion(id: Int, apiQuestion: QuizQuestion): Question {
        val allAnswers = (apiQuestion.incorrectAnswers + apiQuestion.correctAnswer).shuffled()
        return Question(
            id = id,
            question = decodeHtml(apiQuestion.question),
            options = allAnswers.map { decodeHtml(it) },
            correctAnswer = allAnswers.indexOf(apiQuestion.correctAnswer)
        )
    }

    private fun decodeHtml(html: String): String {
        return URLDecoder.decode(html, StandardCharsets.UTF_8.name())
            .replace("&quot;", "\"")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&#039;", "'")
    }

    fun getCurrentQuestion(): Question = currentQuestions[currentQuestionIndex]

    fun selectAnswer(index: Int) {
        if (!isAnswerChecked) {
            selectedAnswer = index
            checkAnswer()
        }
    }

    private fun checkAnswer() {
        selectedAnswer?.let { selected ->
            isAnswerChecked = true
            isCurrentAnswerCorrect = selected == getCurrentQuestion().correctAnswer
            if (isCurrentAnswerCorrect) {
                if (!isReviewMode) {
                    correctQuestionsCount++
                } else {
                    wrongQuestions.remove(getCurrentQuestion())
                    correctQuestionsCount++
                }
                score++
            } else if (!isReviewMode) {
                wrongQuestions.add(getCurrentQuestion())
            }
        }
    }

    fun moveToNextQuestion() {
        if (currentQuestionIndex < currentQuestions.size - 1) {
            currentQuestionIndex++
            resetQuestionState()
        } else {
            if (!isReviewMode && wrongQuestions.isNotEmpty()) {
                startReviewMode()
            } else {
                isQuizFinished = true
            }
        }
    }

    private fun startReviewMode() {
        isReviewMode = true
        currentQuestions = wrongQuestions.toList()
        currentQuestionIndex = 0
        resetQuestionState()
    }

    private fun resetQuestionState() {
        selectedAnswer = null
        isAnswerChecked = false
        isCurrentAnswerCorrect = false
    }

    fun restartQuiz() {
        loadQuizQuestions()
        wrongQuestions.clear()
        currentQuestionIndex = 0
        score = 0
        isQuizFinished = false
        isReviewMode = false
        correctQuestionsCount = 0
        resetQuestionState()
    }

    fun getTotalProgress(): Float {
        return correctQuestionsCount.toFloat() / allQuestions.size
    }

    fun getRemainingQuestions(): Int {
        return if (isReviewMode) {
            wrongQuestions.size
        } else {
            allQuestions.size - correctQuestionsCount
        }
    }
} 