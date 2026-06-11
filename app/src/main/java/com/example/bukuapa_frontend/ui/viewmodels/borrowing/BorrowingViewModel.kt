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
import kotlinx.coroutines.launch

class BorrowingViewModel(application: Application) : AndroidViewModel(application) {
    private val tokenManager = TokenManager(application)
    private val repository = BorrowingRepository(tokenManager)
    private val catalogRepository = CatalogRepository(tokenManager)

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

            // Backend sudah mengatur filter role (Staff vs Member) di endpoint GET /borrowings
            repository.getLoans().onSuccess {
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

            repository.extendLoan(loanId).onSuccess {
                loadLoans()
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Gagal memperpanjang pinjaman."
            }
            _isLoading.value = false
        }
    }

    fun returnLoan(loanId: Int) {
        viewModelScope.launch {
            _isLoading.value = true

            repository.returnLoan(loanId).onSuccess {
                loadLoans() // Refresh data setelah berhasil
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Gagal mengembalikan buku."
            }
            _isLoading.value = false
        }
    }

    fun createLoanByIsbnForUser(userId: Int, isbn: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            catalogRepository.getBooks(search = isbn).onSuccess { books ->
                val book = books.find { it.isbn.replace("-", "").trim() == isbn.replace("-", "").trim() }
                if (book != null) {
                    repository.createLoan(userId, book.id).onSuccess {
                        loadLoans()
                        onSuccess()
                    }.onFailure { error ->
                        _isLoading.value = false
                        onError(error.message ?: "Gagal membuat peminjaman")
                    }
                } else {
                    _isLoading.value = false
                    onError("Buku dengan ISBN $isbn tidak ditemukan.")
                }
            }.onFailure { error ->
                _isLoading.value = false
                onError(error.message ?: "Gagal mencari buku.")
            }
        }
    }
}
