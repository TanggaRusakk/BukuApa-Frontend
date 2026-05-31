package com.example.bukuapa_frontend.domain.protocols

import com.example.bukuapa_frontend.data.models.Book

interface BookRepositoryProtocol {
    suspend fun getBooks(token: String): Result<List<Book>>
    suspend fun deleteBook(token: String, bookId: Int): Result<Boolean>
}