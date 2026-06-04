package com.example.bukuapa_frontend.ui.viewmodels.borrowing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bukuapa_frontend.data.models.Loan
import com.example.bukuapa_frontend.data.repositories.BorrowingRepository
import com.example.bukuapa_frontend.data.repositories.CatalogRepository
import com.example.bukuapa_frontend.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BorrowingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BorrowingRepository()
    private val catalogRepository = CatalogRepository()
    private val tokenManager = TokenManager(application)

    private val _loans = MutableStateFlow<List<Loan>>(emptyList())
    val loans: StateFlow<List<Loan>> = _loans

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadLoans()
    }

    fun loadLoans() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val token = tokenManager.getToken().first() ?: return@launch

            repository.getLoans(token).onSuccess {
                _loans.value = it
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Gagal memuat riwayat peminjaman."
            }
            _isLoading.value = false
        }
    }

    fun extendLoan(loanId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = tokenManager.getToken().first() ?: return@launch

            repository.extendLoan(token, loanId).onSuccess {
                loadLoans() // Refresh list
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Gagal memperpanjang pinjaman."
            }
            _isLoading.value = false
        }
    }

    fun createLoanByIsbn(isbn: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = tokenManager.getToken().first() ?: return@launch

            // 1. Cari buku berdasarkan ISBN
            catalogRepository.getBooks(token, search = isbn).onSuccess { books ->
                val book = books.find { it.isbn == isbn }
                if (book != null) {
                    // 2. Jika ketemu, buat peminjaman pake bookId
                    repository.createLoan(token, book.id).onSuccess {
                        _isLoading.value = false
                        loadLoans()
                        onSuccess()
                    }.onFailure { error ->
                        _isLoading.value = false
                        onError(error.message ?: "Gagal membuat peminjaman")
                    }
                } else {
                    _isLoading.value = false
                    onError("Buku dengan ISBN tersebut tidak ditemukan.")
                }
            }.onFailure { error ->
                _isLoading.value = false
                onError(error.message ?: "Gagal mencari buku.")
            }
        }
    }
}
