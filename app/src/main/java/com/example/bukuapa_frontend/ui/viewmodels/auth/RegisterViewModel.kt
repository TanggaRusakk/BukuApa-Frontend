package com.example.bukuapa_frontend.ui.viewmodels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bukuapa_frontend.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun register(nama: String, email: String, sandi: String) {
        if (nama.length < 3) {
            _errorMessage.value = "Nama minimal 3 karakter."
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = "Format email tidak valid."
            return
        }
        if (sandi.length < 8) {
            _errorMessage.value = "Kata sandi minimal 8 karakter."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.register(nama, email, sandi)
            result.onSuccess {
                _isSuccess.value = true
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Daftar gagal."
            }
            _isLoading.value = false
        }
    }
}