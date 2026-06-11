package com.example.bukuapa_frontend.domain.protocols

import com.example.bukuapa_frontend.data.models.Loan

interface BorrowingServiceProtocol {
    suspend fun getLoans(): Result<List<Loan>>
    suspend fun createLoan(userId: Int, bookId: Int): Result<Loan>
    suspend fun returnLoan(loanId: Int): Result<Loan>
    suspend fun extendLoan(loanId: Int): Result<Loan>
}
