package com.example.bukuapa_frontend.data.api

import com.example.bukuapa_frontend.data.models.Book
import com.example.bukuapa_frontend.data.models.CanReviewResponse
import com.example.bukuapa_frontend.data.models.Category
import com.example.bukuapa_frontend.data.models.CreateReviewRequest
import com.example.bukuapa_frontend.data.models.Loan
import com.example.bukuapa_frontend.data.models.Review
import com.example.bukuapa_frontend.data.models.ReviewListResponse
import com.example.bukuapa_frontend.data.models.UpdateReviewRequest
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

    @GET("books/{bookId}")
    suspend fun getBookById(
        @Header("Authorization") token: String,
        @Path("bookId") bookId: Int
    ): ApiResponse<Book>

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

    @GET("borrowings")
    suspend fun getLoans(@Header("Authorization") token: String): ApiResponse<List<Loan>>

    @POST("borrowings")
    suspend fun createLoan(
        @Header("Authorization") token: String,
        @Body request: Map<String, Int>
    ): ApiResponse<Loan>

    @PATCH("borrowings/{loanId}/return")
    suspend fun returnLoan(
        @Header("Authorization") token: String,
        @Path("loanId") loanId: Int,
        @Body emptyBody: Map<String, String> = emptyMap()
    ): ApiResponse<Loan>

    @POST("borrowings/{loanId}/extend")
    suspend fun extendLoan(
        @Header("Authorization") token: String,
        @Path("loanId") loanId: Int
    ): ApiResponse<Loan>

    @GET("books/{bookId}/reviews")
    suspend fun getReviews(
        @Path("bookId") bookId: Int,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): ApiResponse<ReviewListResponse>

    @POST("books/{bookId}/reviews")
    suspend fun createReview(
        @Header("Authorization") token: String,
        @Path("bookId") bookId: Int,
        @Body request: CreateReviewRequest
    ): ApiResponse<Review>

    @PUT("reviews/{reviewId}")
    suspend fun updateReview(
        @Header("Authorization") token: String,
        @Path("reviewId") reviewId: Int,
        @Body request: UpdateReviewRequest
    ): ApiResponse<Review>

    @DELETE("reviews/{reviewId}")
    suspend fun deleteReview(
        @Header("Authorization") token: String,
        @Path("reviewId") reviewId: Int
    ): ApiResponse<Any>

    @GET("books/{bookId}/can-review")
    suspend fun canReview(
        @Header("Authorization") token: String,
        @Path("bookId") bookId: Int
    ): ApiResponse<CanReviewResponse>
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
