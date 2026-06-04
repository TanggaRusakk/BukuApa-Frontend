package com.example.bukuapa_frontend.data.repositories

import com.example.bukuapa_frontend.data.api.ApiClient
import com.example.bukuapa_frontend.data.models.Loan
import com.example.bukuapa_frontend.utils.NetworkUtils

class BorrowingRepository {
    suspend fun getLoans(token: String): Result<List<Loan>> {
        return try {
            val response = ApiClient.instance.getLoans("Bearer $token")
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseErrorMessage(e, "Ambil data pinjaman")))
        }
    }

    suspend fun extendLoan(token: String, loanId: Int): Result<Loan> {
        return try {
            val response = ApiClient.instance.extendLoan("Bearer $token", loanId)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseErrorMessage(e, "Perpanjang pinjaman")))
        }
    }

    suspend fun returnLoan(token: String, loanId: Int): Result<Loan> {
        return try {
            // FIX: Tambahin "Bearer " biar token JWT-nya kebaca valid oleh backend
            val response = ApiClient.instance.returnLoan("Bearer $token", loanId)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseErrorMessage(e, "Mengembalikan buku")))
        }
    }

    suspend fun createLoanForUser(token: String, userId: Int, bookId: Int): Result<Loan> {
        return try {
            val response = ApiClient.instance.createLoan(
                "Bearer $token",
                mapOf("userId" to userId, "bookId" to bookId)
            )
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseErrorMessage(e, "Buat peminjaman")))
        }
    }
}