package com.example.bukuapa_frontend.data.api

import com.example.bukuapa_frontend.data.models.Book
import com.example.bukuapa_frontend.data.models.Category
import com.example.bukuapa_frontend.data.models.Loan
import com.example.bukuapa_frontend.data.models.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

data class ApiResponse<T>(val data: T, val message: String? = null)

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: Map<String, String>): ApiResponse<Any>

    @POST("login")
    suspend fun login(@Body request: Map<String, String>): ApiResponse<Map<String, String>>

    @GET("users/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): ApiResponse<User>

    @GET("categories")
    suspend fun getCategories(@Header("Authorization") token: String): ApiResponse<List<Category>>

    @GET("books")
    suspend fun getBooks(
        @Header("Authorization") token: String,
        @Query("search") search: String? = null,
        @Query("category") categoryId: Int? = null
    ): ApiResponse<List<Book>>

    @POST("books")
    suspend fun createBook(
        @Header("Authorization") token: String,
        @Body book: Book
    ): ApiResponse<Book>

    @PUT("books/{bookId}")
    suspend fun updateBook(
        @Header("Authorization") token: String,
        @Path("bookId") bookId: Int,
        @Body book: Book
    ): ApiResponse<Book>

    @DELETE("books/{bookId}")
    suspend fun deleteBook(
        @Header("Authorization") token: String,
        @Path("bookId") bookId: Int
    ): ApiResponse<Any>

    @GET("categories")
    suspend fun getCategories(@Header("Authorization") token: String): ApiResponse<List<Category>>

    @GET("loans")
    suspend fun getLoans(@Header("Authorization") token: String): ApiResponse<List<Loan>>

    @POST("loans/{loanId}/extend")
    suspend fun extendLoan(
        @Header("Authorization") token: String,
        @Path("loanId") loanId: Int
    ): ApiResponse<Loan>
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