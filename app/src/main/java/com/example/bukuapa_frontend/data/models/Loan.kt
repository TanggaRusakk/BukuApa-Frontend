package com.example.bukuapa_frontend.data.models

data class Loan(
    val id: Int = 0,
    val bookId: Int = 0,
    val bookTitle: String = "",
    val bookAuthor: String = "",
    val bookCoverUrl: String? = null,
    val borrowDate: String = "",
    val dueDate: String = "",
    val returnDate: String? = null,
    val status: String = "", // e.g., "BORROWED", "OVERDUE", "RETURNED"
    val extensionCount: Int = 0
)
