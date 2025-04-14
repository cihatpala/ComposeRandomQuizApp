package com.cihat.egitim.composecursorquizapp.data.api

import com.cihat.egitim.composecursorquizapp.data.model.QuizResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizApiService {
    @GET("api.php")
    suspend fun getQuizQuestions(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null,
        @Query("type") type: String = "multiple"
    ): QuizResponse
}

object QuizApi {
    private const val BASE_URL = "https://opentdb.com/"
    
    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()
    
    val service: QuizApiService = retrofit.create(QuizApiService::class.java)
} 