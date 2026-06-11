package com.example.bukuapa_frontend.domain.protocols

import com.example.bukuapa_frontend.data.models.Book
import com.example.bukuapa_frontend.data.models.Category

interface CatalogServiceProtocol {
    suspend fun getBooks(search: String? = null, categoryId: Int? = null): Result<List<Book>>
    suspend fun getBookById(bookId: Int): Result<Book>
    suspend fun getCategories(): Result<List<Category>>
}
