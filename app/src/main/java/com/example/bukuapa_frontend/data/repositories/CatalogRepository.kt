package com.example.bukuapa_frontend.data.repositories

import com.example.bukuapa_frontend.data.api.ApiClient
import com.example.bukuapa_frontend.data.models.Book
import com.example.bukuapa_frontend.data.models.Category
import com.example.bukuapa_frontend.domain.protocols.CatalogServiceProtocol
import com.example.bukuapa_frontend.utils.NetworkUtils
import com.example.bukuapa_frontend.utils.TokenManager
import kotlinx.coroutines.flow.first

class CatalogRepository(
    private val tokenManager: TokenManager
) : CatalogServiceProtocol {

    private val apiService = ApiClient.instance

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val token = tokenManager.getToken().first()
            val authHeader = "Bearer ${token ?: return Result.failure(Exception("Token tidak ditemukan"))}"
            val response = apiService.getCategories(authHeader)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseError(e, "Ambil kategori")))
        }
    }

    override suspend fun getBooks(search: String?, categoryId: Int?): Result<List<Book>> {
        return try {
            val token = tokenManager.getToken().first()
            val authHeader = "Bearer ${token ?: return Result.failure(Exception("Token tidak ditemukan"))}"
            val response = apiService.getBooks(authHeader, search, categoryId)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseError(e, "Ambil katalog buku")))
        }
    }

    override suspend fun getBookById(bookId: Int): Result<Book> {
        return try {
            val token = tokenManager.getToken().first()
            val authHeader = "Bearer ${token ?: return Result.failure(Exception("Token tidak ditemukan"))}"
            val response = apiService.getBookById(authHeader, bookId)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseError(e, "Ambil detail buku")))
        }
    }
}
