package com.example.bukuapa_frontend.ui.viewmodels.book

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bukuapa_frontend.data.models.Book
import com.example.bukuapa_frontend.data.models.Category
import com.example.bukuapa_frontend.data.models.User
import com.example.bukuapa_frontend.data.repositories.CatalogRepository
import com.example.bukuapa_frontend.data.repositories.UserRepository
import com.example.bukuapa_frontend.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CatalogViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CatalogRepository()
    private val userRepository = UserRepository()
    private val tokenManager = TokenManager(application)

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    private val _categories = MutableStateFlow<List<Category>>(listOf(Category(0, "Semua")))
    val categories: StateFlow<List<Category>> = _categories

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            val token = tokenManager.getToken().first() ?: return@launch
            
            // Fetch User
            userRepository.getCurrentUser(token).onSuccess {
                _currentUser.value = it
            }

            // Fetch Categories
            repository.getCategories(token).onSuccess {
                _categories.value = listOf(Category(0, "Semua")) + it
            }

            // Fetch Books
            loadBooks()
            _isLoading.value = false
        }
    }

    fun loadBooks(search: String? = null, categoryId: Int? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = tokenManager.getToken().first() ?: return@launch
            
            val filterCategoryId = if (categoryId == 0) null else categoryId
            
            repository.getBooks(token, search, filterCategoryId).onSuccess {
                _books.value = it
            }.onFailure {
                _errorMessage.value = "Gagal memuat buku."
            }
            _isLoading.value = false
        }
    }
}
