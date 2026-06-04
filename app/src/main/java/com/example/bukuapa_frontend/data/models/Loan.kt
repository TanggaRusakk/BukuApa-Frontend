package com.example.bukuapa_frontend.data.models

data class Loan(
    val id: Int = 0,
    val userId: Int = 0,
    val bookId: Int = 0,
    val borrowDate: String = "",
    val dueDate: String = "",
    val returnDate: String? = null,
    val status: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    // Nested objects from backend include
    val book: Book? = null,
    val user: User? = null,
    
    // UI Helper (legacy compatibility)
    val extensionCount: Int = 0
)
