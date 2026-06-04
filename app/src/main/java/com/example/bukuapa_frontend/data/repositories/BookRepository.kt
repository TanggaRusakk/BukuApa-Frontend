package com.example.bukuapa_frontend.data.repositories

import com.example.bukuapa_frontend.data.api.ApiClient
import com.example.bukuapa_frontend.data.models.Book
import com.example.bukuapa_frontend.domain.protocols.BookRepositoryProtocol
import com.example.bukuapa_frontend.utils.NetworkUtils

class BookRepository : BookRepositoryProtocol {
    override suspend fun getBooks(token: String): Result<List<Book>> {
        return try {
            Result.success(ApiClient.instance.getBooks("Bearer $token").data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseErrorMessage(e, "Ambil buku")))
        }
    }

    override suspend fun createBook(token: String, book: Book): Result<Book> {
        return try {
            Result.success(ApiClient.instance.createBook("Bearer $token", book).data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseErrorMessage(e, "Tambah buku")))
        }
    }

    override suspend fun updateBook(token: String, bookId: Int, book: Book): Result<Book> {
        return try {
            Result.success(ApiClient.instance.updateBook("Bearer $token", bookId, book).data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseErrorMessage(e, "Update buku")))
        }
    }

    override suspend fun deleteBook(token: String, bookId: Int): Result<Boolean> {
        return try {
            ApiClient.instance.deleteBook("Bearer $token", bookId)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseErrorMessage(e, "Hapus buku")))
        }
    }
}
