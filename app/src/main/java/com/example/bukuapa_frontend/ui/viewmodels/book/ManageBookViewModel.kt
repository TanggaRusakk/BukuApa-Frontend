package com.example.bukuapa_frontend.ui.viewmodels.book

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bukuapa_frontend.data.models.Book
import com.example.bukuapa_frontend.data.repositories.BookRepository
import com.example.bukuapa_frontend.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ManageBookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BookRepository()
    private val tokenManager = TokenManager(application)

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isLoadingBooks = MutableStateFlow(false)
    val isLoadingBooks: StateFlow<Boolean> = _isLoadingBooks

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchBooks() {
        viewModelScope.launch {
            _isLoadingBooks.value = true
            val token = tokenManager.getToken().firstOrNull()
            if (token == null) {
                _isLoadingBooks.value = false
                return@launch
            }
            
            repository.getBooks(token).onSuccess { bookList ->
                _books.value = bookList
            }
            _isLoadingBooks.value = false
        }
    }

    fun createBook(book: Book, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = tokenManager.getToken().firstOrNull()
            if (token == null) {
                _errorMessage.value = "Token tidak ditemukan"
                onError("Token tidak ditemukan")
                _isLoading.value = false
                return@launch
            }
            
            repository.createBook(token, book).onSuccess {
                fetchBooks()
                onSuccess()
                _errorMessage.value = null
            }.onFailure { e ->
                _errorMessage.value = e.message ?: "Gagal membuat buku"
                onError(e.message ?: "Gagal membuat buku")
            }
            _isLoading.value = false
        }
    }

    fun updateBook(bookId: Int, book: Book, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = tokenManager.getToken().firstOrNull()
            if (token == null) {
                _errorMessage.value = "Token tidak ditemukan"
                onError("Token tidak ditemukan")
                _isLoading.value = false
                return@launch
            }

            repository.updateBook(token, bookId, book).onSuccess {
                fetchBooks()
                onSuccess()
                _errorMessage.value = null
            }.onFailure { e ->
                _errorMessage.value = e.message ?: "Gagal mengubah buku"
                onError(e.message ?: "Gagal mengubah buku")
            }
            _isLoading.value = false
        }
    }

    fun deleteBook(bookId: Int) {
        viewModelScope.launch {
            val token = tokenManager.getToken().firstOrNull() ?: return@launch
            if (repository.deleteBook(token, bookId).isSuccess) {
                fetchBooks()
            }
        }
    }
}