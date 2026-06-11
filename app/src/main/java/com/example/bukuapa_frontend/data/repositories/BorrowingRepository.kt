package com.example.bukuapa_frontend.data.repositories

import com.example.bukuapa_frontend.data.api.ApiClient
import com.example.bukuapa_frontend.data.models.Loan
import com.example.bukuapa_frontend.domain.protocols.BorrowingServiceProtocol
import com.example.bukuapa_frontend.utils.NetworkUtils
import com.example.bukuapa_frontend.utils.TokenManager
import kotlinx.coroutines.flow.first

class BorrowingRepository(
    private val tokenManager: TokenManager
) : BorrowingServiceProtocol {

    private val apiService = ApiClient.instance

    override suspend fun getLoans(): Result<List<Loan>> {
        return try {
            val token = tokenManager.getToken().first()
            val authHeader = "Bearer ${token ?: return Result.failure(Exception("Token tidak ditemukan"))}"
            val response = apiService.getLoans(authHeader)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseError(e, "Ambil data pinjaman")))
        }
    }

    override suspend fun extendLoan(loanId: Int): Result<Loan> {
        return try {
            val token = tokenManager.getToken().first()
            val authHeader = "Bearer ${token ?: return Result.failure(Exception("Token tidak ditemukan"))}"
            val response = apiService.extendLoan(authHeader, loanId)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseError(e, "Perpanjang pinjaman")))
        }
    }

    override suspend fun returnLoan(loanId: Int): Result<Loan> {
        return try {
            val token = tokenManager.getToken().first()
            val authHeader = "Bearer ${token ?: return Result.failure(Exception("Token tidak ditemukan"))}"
            val response = apiService.returnLoan(authHeader, loanId)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseError(e, "Mengembalikan buku")))
        }
    }

    override suspend fun createLoan(userId: Int, bookId: Int): Result<Loan> {
        return try {
            val token = tokenManager.getToken().first()
            val authHeader = "Bearer ${token ?: return Result.failure(Exception("Token tidak ditemukan"))}"
            val response = apiService.createLoan(
                authHeader,
                mapOf("userId" to userId, "bookId" to bookId)
            )
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkUtils.parseError(e, "Buat peminjaman")))
        }
    }
}
