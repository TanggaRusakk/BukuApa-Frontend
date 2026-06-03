package com.example.bukuapa_frontend.data.repositories

import com.example.bukuapa_frontend.data.api.ApiClient
import com.example.bukuapa_frontend.data.models.Book
import com.example.bukuapa_frontend.data.models.Category

class CatalogRepository {
    suspend fun getCategories(token: String): Result<List<Category>> {
        return try {
            val response = ApiClient.instance.getCategories("Bearer $token")
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBooks(token: String, search: String? = null, categoryId: Int? = null): Result<List<Book>> {
        return try {
            val response = ApiClient.instance.getBooks("Bearer $token", search, categoryId)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
