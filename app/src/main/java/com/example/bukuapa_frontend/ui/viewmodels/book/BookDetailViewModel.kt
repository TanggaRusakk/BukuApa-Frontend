package com.example.bukuapa_frontend.ui.viewmodels.book

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bukuapa_frontend.data.models.Book
import com.example.bukuapa_frontend.data.repositories.BookRepository
import com.example.bukuapa_frontend.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BookDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BookRepository()
    private val tokenManager = TokenManager(application)

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchBookDetail(bookId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val token = tokenManager.getToken().first() ?: return@launch

            repository.getBookById(token, bookId).onSuccess {
                _book.value = it
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Gagal memuat detail buku."
            }
            _isLoading.value = false
        }
    }
}
