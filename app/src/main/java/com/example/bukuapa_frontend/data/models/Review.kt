package com.example.bukuapa_frontend.data.models

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("id") val id: Int,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("user") val user: ReviewUser
)

data class ReviewUser(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class ReviewListResponse(
    @SerializedName("reviews") val reviews: List<Review>,
    @SerializedName("total") val total: Int,
    @SerializedName("averageRating") val averageRating: Double?,
    @SerializedName("pagination") val pagination: Pagination
)

data class ReviewListData(
    @SerializedName("reviews") val reviews: List<Review>,
    @SerializedName("total") val total: Int,
    @SerializedName("averageRating") val averageRating: Double?,
    @SerializedName("pagination") val pagination: Pagination
)

data class Pagination(
    @SerializedName("page") val page: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("totalPages") val totalPages: Int
)

data class CanReviewResponse(
    @SerializedName("canReview") val canReview: Boolean
)

data class CreateReviewRequest(
    @SerializedName("bookId") val bookId: Int,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String
)

data class UpdateReviewRequest(
    @SerializedName("rating") val rating: Int? = null,
    @SerializedName("comment") val comment: String? = null
)

data class CanReviewData(
    @SerializedName("canReview") val canReview: Boolean
)