package com.example.bukuapa_frontend.domain.protocols

import com.example.bukuapa_frontend.data.models.*

interface ReviewServiceProtocol {
    suspend fun getReviews(bookId: Int, page: Int, limit: Int): Result<ReviewListResponse>
    suspend fun createReview(request: CreateReviewRequest): Result<Review>
    suspend fun updateReview(reviewId: Int, request: UpdateReviewRequest): Result<Review>
    suspend fun deleteReview(reviewId: Int): Result<Unit>
    suspend fun canReview(bookId: Int): Result<Boolean>
}