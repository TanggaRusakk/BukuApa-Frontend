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