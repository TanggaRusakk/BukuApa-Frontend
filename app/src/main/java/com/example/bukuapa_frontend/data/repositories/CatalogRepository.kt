package com.example.bukuapa_frontend.data.repositories

import com.example.bukuapa_frontend.data.api.ApiClient
import com.example.bukuapa_frontend.data.models.Book
import com.example.bukuapa_frontend.data.models.Category
import com.example.bukuapa_frontend.utils.NetworkUtils

class CatalogRepository {
    suspend fun getCategories(token: String): Result<List<Category>> {
        return try {
            val response = ApiClient.instance.getCategories("Bearer $token")
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseErrorMessage(e, "Ambil kategori")))
        }
    }

    suspend fun getBooks(token: String, search: String? = null, categoryId: Int? = null): Result<List<Book>> {
        return try {
            val response = ApiClient.instance.getBooks("Bearer $token", search, categoryId)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseErrorMessage(e, "Ambil buku")))
        }
    }
}
