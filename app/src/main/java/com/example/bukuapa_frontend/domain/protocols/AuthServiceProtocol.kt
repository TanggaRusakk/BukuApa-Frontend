package com.example.bukuapa_frontend.domain.protocols

interface AuthServiceProtocol {
    suspend fun login(email: String, sandi: String): Result<String>
    suspend fun register(nama: String, email: String, sandi: String): Result<Boolean>
}