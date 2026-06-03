package com.example.bukuapa_frontend.ui.viewmodels.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bukuapa_frontend.data.models.*
import com.example.bukuapa_frontend.domain.protocols.ReviewServiceProtocol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val reviewService: ReviewServiceProtocol
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReviewUiState>(ReviewUiState.Loading)
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    private val _canReview = MutableStateFlow(false)
    val canReview: StateFlow<Boolean> = _canReview.asStateFlow()

    private var currentBookId: Int? = null

    sealed class ReviewUiState {
        object Loading : ReviewUiState()
        data class Success(val data: ReviewListResponse) : ReviewUiState()
        data class Error(val message: String) : ReviewUiState()
        object Empty : ReviewUiState()
    }

    fun loadReviews(bookId: Int, page: Int = 1, limit: Int = 20) {
        currentBookId = bookId
        viewModelScope.launch {
            _uiState.value = ReviewUiState.Loading
            reviewService.getReviews(bookId, page, limit)
                .onSuccess { data ->
                    _uiState.value = if (data.reviews.isEmpty()) ReviewUiState.Empty else ReviewUiState.Success(data)
                }
                .onFailure { error ->
                    _uiState.value = ReviewUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun createReview(request: CreateReviewRequest) {
        viewModelScope.launch {
            _uiState.value = ReviewUiState.Loading
            reviewService.createReview(request)
                .onSuccess {
                    currentBookId?.let { bookId -> loadReviews(bookId, 1, 20) }
                    _canReview.value = false
                }
                .onFailure { error ->
                    _uiState.value = ReviewUiState.Error(error.message ?: "Failed to create review")
                }
        }
    }

    fun updateReview(reviewId: Int, request: UpdateReviewRequest) {
        viewModelScope.launch {
            _uiState.value = ReviewUiState.Loading
            reviewService.updateReview(reviewId, request)
                .onSuccess {
                    currentBookId?.let { bookId -> loadReviews(bookId, 1, 20) }
                }
                .onFailure { error ->
                    _uiState.value = ReviewUiState.Error(error.message ?: "Failed to update review")
                }
        }
    }

    fun deleteReview(reviewId: Int) {
        viewModelScope.launch {
            _uiState.value = ReviewUiState.Loading
            reviewService.deleteReview(reviewId)
                .onSuccess {
                    currentBookId?.let { bookId -> loadReviews(bookId, 1, 20) }
                    _canReview.value = true
                }
                .onFailure { error ->
                    _uiState.value = ReviewUiState.Error(error.message ?: "Failed to delete review")
                }
        }
    }

    fun checkCanReview(bookId: Int) {
        viewModelScope.launch {
            reviewService.canReview(bookId)
                .onSuccess { canReview -> _canReview.value = canReview }
                .onFailure { _canReview.value = false }
        }
    }

    fun formatDate(isoDate: String): String {
        return try {
            val sdfInput = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
            val date = sdfInput.parse(isoDate)
            java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(date ?: isoDate.take(10))
        } catch (e: Exception) {
            isoDate.take(10)
        }
    }
}