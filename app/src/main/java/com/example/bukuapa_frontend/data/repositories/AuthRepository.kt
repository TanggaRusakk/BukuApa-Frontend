package com.example.bukuapa_frontend.data.repositories

import com.example.bukuapa_frontend.data.api.ApiClient
import com.example.bukuapa_frontend.domain.protocols.AuthServiceProtocol
import com.example.bukuapa_frontend.utils.NetworkUtils

class AuthRepository : AuthServiceProtocol {

    override suspend fun login(email: String, sandi: String): Result<String> {
        return try {
            val response = ApiClient.instance.login(mapOf("email" to email, "password" to sandi))
            Result.success(response.data["token"] ?: throw Exception("Token tidak valid"))
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseError(e, "Login")))
        }
    }

    override suspend fun register(nama: String, email: String, sandi: String): Result<Boolean> {
        return try {
            ApiClient.instance.register(
                mapOf(
                    "name" to nama,
                    "email" to email,
                    "password" to sandi,
                    "role" to "MEMBER"
                )
            )
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseError(e, "Daftar")))
        }
    }
}
