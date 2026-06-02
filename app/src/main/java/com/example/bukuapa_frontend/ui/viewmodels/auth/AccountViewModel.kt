package com.example.bukuapa_frontend.ui.viewmodels.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bukuapa_frontend.data.api.ApiClient
import com.example.bukuapa_frontend.data.models.User
import com.example.bukuapa_frontend.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val tokenManager = TokenManager(application)

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchUserInfo() {
        viewModelScope.launch {
            _isLoading.value = true
            val token = tokenManager.getToken().firstOrNull()
            if (token != null) {
                try {
                    val response = ApiClient.instance.getCurrentUser("Bearer $token")
                    _user.value = response.data
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            _isLoading.value = false
        }
    }
}
