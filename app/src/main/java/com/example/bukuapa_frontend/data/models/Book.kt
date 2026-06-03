package com.example.bukuapa_frontend.data.models

data class Book(
    val id: Int = 0,
    val isbn: String = "",
    val title: String = "",
    val author: String = "",
    val publisher: String = "",
    val publishedYear: Int = 0,
    val totalPages: Int = 0,
    val stock: Int = 0,
    val categories: List<Category>? = null,
    val categoryIds: List<Int>? = null,
    val rating: Double = 0.0,
    val coverUrl: String? = null
)