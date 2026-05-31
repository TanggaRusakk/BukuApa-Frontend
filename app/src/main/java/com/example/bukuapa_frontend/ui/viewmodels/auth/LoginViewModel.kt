package com.example.bukuapa_frontend.ui.viewmodels.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bukuapa_frontend.data.repositories.AuthRepository
import com.example.bukuapa_frontend.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository()
    private val tokenManager = TokenManager(application)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loginToken = MutableStateFlow<String?>(null)
    val loginToken: StateFlow<String?> = _loginToken

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login(email: String, sandi: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.login(email, sandi)
            result.onSuccess { token ->
                tokenManager.saveToken(token) // Simpan ke HP
                _loginToken.value = token
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Login gagal."
            }
            _isLoading.value = false
        }
    }
}