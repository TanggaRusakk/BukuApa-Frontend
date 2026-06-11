package com.example.bukuapa_frontend.data.api

data class ApiResponse<T>(val data: T, val message: String? = null)