package com.cihat.egitim.composecursorquizapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cihat.egitim.composecursorquizapp.viewmodel.QuizViewModel
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(viewModel: QuizViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            viewModel.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            viewModel.errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = viewModel.errorMessage ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.restartQuiz() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Tekrar Dene")
                    }
                }
            }
            viewModel.isQuizFinished -> {
                QuizFinishedScreen(
                    score = viewModel.score,
                    totalQuestions = 10,
                    onRestartQuiz = { viewModel.restartQuiz() }
                )
            }
            else -> {
                QuizContent(viewModel)
            }
        }
    }
}

@Composable
fun QuizContent(viewModel: QuizViewModel) {
    val currentQuestion = viewModel.getCurrentQuestion()
    var countdown by remember { mutableStateOf<Int?>(null) }
    
    // Geri sayım efekti
    LaunchedEffect(viewModel.isAnswerChecked) {
        if (viewModel.isAnswerChecked) {
            countdown = 3
            repeat(3) {
                delay(1000)
                countdown = countdown?.minus(1)
            }
            viewModel.moveToNextQuestion()
            countdown = null
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Enhanced Progress indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(viewModel.getTotalProgress())
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        
        // Question number and progress text with countdown
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (viewModel.isReviewMode) 
                    "Tekrar: Yanlış ${viewModel.currentQuestionIndex + 1}" 
                else 
                    "Soru ${viewModel.correctQuestionsCount + 1}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            if (countdown != null) {
                Text(
                    text = countdown.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "Kalan: ${viewModel.getRemainingQuestions()}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Question
        Text(
            text = currentQuestion.question,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Options in a Column with less spacing
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            currentQuestion.options.forEachIndexed { index, option ->
                val isSelected = viewModel.selectedAnswer == index
                val isCorrectAnswer = index == currentQuestion.correctAnswer
                
                Button(
                    onClick = { 
                        if (!viewModel.isAnswerChecked) {
                            viewModel.selectAnswer(index)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            !viewModel.isAnswerChecked -> MaterialTheme.colorScheme.primary
                            isSelected && isCorrectAnswer -> Color(0xFF4CAF50) // Seçilen doğruysa yeşil
                            isSelected && !isCorrectAnswer -> Color(0xFFE53935) // Seçilen yanlışsa kırmızı
                            isCorrectAnswer -> Color(0xFF4CAF50) // Doğru cevabı göster
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }
        }

        // Feedback text with review mode indicator
        if (viewModel.isAnswerChecked) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (viewModel.isCurrentAnswerCorrect) "Doğru!" else "Yanlış!",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (viewModel.isCurrentAnswerCorrect) Color(0xFF4CAF50) else Color(0xFFE53935),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                if (viewModel.isReviewMode) {
                    Text(
                        text = "Bu soru sorulmuştu",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun QuizFinishedScreen(
    score: Int,
    totalQuestions: Int,
    onRestartQuiz: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Test Tamamlandı!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Puanınız: $score / $totalQuestions",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Add percentage score
        Text(
            text = "Başarı: ${(score.toFloat() / totalQuestions * 100).toInt()}%",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onRestartQuiz,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Yeniden Başla")
        }
    }
} 