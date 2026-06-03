package com.example.bukuapa_frontend.data.repositories

import com.example.bukuapa_frontend.data.api.ApiClient
import com.example.bukuapa_frontend.data.models.*
import com.example.bukuapa_frontend.domain.protocols.ReviewServiceProtocol
import com.example.bukuapa_frontend.utils.TokenManager
import com.example.bukuapa_frontend.data.models.ReviewListResponse
import kotlinx.coroutines.flow.first

/**
 * Implements ReviewServiceProtocol (SC-16)
 * Mengelola komunikasi dengan ApiClient dan menangani pengambilan token.
 */
class ReviewRepository(
    private val tokenManager: TokenManager
) : ReviewServiceProtocol {

    private val apiService = ApiClient.instance

    override suspend fun getReviews(bookId: Int, page: Int, limit: Int): Result<ReviewListResponse> {
        return try {
            val response = apiService.getReviews(bookId, page, limit)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createReview(request: CreateReviewRequest): Result<Review> {
        return try {
            val token = tokenManager.getToken().first()
            val authHeader = "Bearer ${token ?: return Result.failure(Exception("Token tidak ditemukan"))}"
            val response = apiService.createReview(authHeader, request)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateReview(reviewId: Int, request: UpdateReviewRequest): Result<Review> {
        return try {
            val token = tokenManager.getToken().first()
            val authHeader = "Bearer ${token ?: return Result.failure(Exception("Token tidak ditemukan"))}"
            val response = apiService.updateReview(authHeader, reviewId, request)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteReview(reviewId: Int): Result<Unit> {
        return try {
            val token = tokenManager.getToken().first()
            val authHeader = "Bearer ${token ?: return Result.failure(Exception("Token tidak ditemukan"))}"
            apiService.deleteReview(authHeader, reviewId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun canReview(bookId: Int): Result<Boolean> {
        return try {
            val token = tokenManager.getToken().first()
            val authHeader = "Bearer ${token ?: return Result.failure(Exception("Token tidak ditemukan"))}"
            val response = apiService.canReview(authHeader, bookId)
            Result.success(response.data.canReview)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}