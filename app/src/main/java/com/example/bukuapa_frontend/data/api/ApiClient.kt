package com.example.bukuapa_frontend.data.api

import com.example.bukuapa_frontend.data.models.Book
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

data class ApiResponse<T>(val data: T, val message: String? = null)

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: Map<String, String>): ApiResponse<Any>

    @POST("login")
    suspend fun login(@Body request: Map<String, String>): ApiResponse<Map<String, String>>

    @GET("books")
    suspend fun getBooks(@Header("Authorization") token: String): ApiResponse<List<Book>>

    @DELETE("books/{bookId}")
    suspend fun deleteBook(
        @Header("Authorization") token: String,
        @Path("bookId") bookId: Int
    ): ApiResponse<Any>
}

object ApiClient {
    private const val BASE_URL = "https://bukuapa-backend.up.railway.app/api/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}