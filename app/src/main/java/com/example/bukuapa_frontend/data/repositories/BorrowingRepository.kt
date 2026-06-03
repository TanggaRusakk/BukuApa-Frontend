package com.example.bukuapa_frontend.data.repositories

import com.example.bukuapa_frontend.data.api.ApiClient
import com.example.bukuapa_frontend.data.models.Loan

class BorrowingRepository {
    suspend fun getLoans(token: String): Result<List<Loan>> {
        return try {
            val response = ApiClient.instance.getLoans("Bearer $token")
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun extendLoan(token: String, loanId: Int): Result<Loan> {
        return try {
            val response = ApiClient.instance.extendLoan("Bearer $token", loanId)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
