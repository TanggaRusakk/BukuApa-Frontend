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

    fun fetchBooks() {
        viewModelScope.launch {
            val token = tokenManager.getToken().firstOrNull() ?: return@launch
            repository.getBooks(token).onSuccess { bookList ->
                _books.value = bookList
            }
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