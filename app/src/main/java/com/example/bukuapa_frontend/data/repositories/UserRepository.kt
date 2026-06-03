package com.example.bukuapa_frontend.data.repositories

import com.example.bukuapa_frontend.data.api.ApiClient
import com.example.bukuapa_frontend.data.models.User

class UserRepository {
    suspend fun getCurrentUser(token: String): Result<User> {
        return try {
            val response = ApiClient.instance.getCurrentUser("Bearer $token")
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
