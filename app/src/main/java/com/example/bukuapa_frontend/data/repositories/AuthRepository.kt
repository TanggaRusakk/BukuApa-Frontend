package com.example.bukuapa_frontend.data.repositories

import com.example.bukuapa_frontend.data.api.ApiClient
import com.example.bukuapa_frontend.domain.protocols.AuthServiceProtocol
import org.json.JSONObject
import retrofit2.HttpException

class AuthRepository : AuthServiceProtocol {

    private fun parseErrorMessage(errorBody: String?, statusCode: Int, defaultAction: String): String {
        if (errorBody == null) return "$defaultAction gagal: $statusCode"
        
        return try {
            val json = JSONObject(errorBody)
            if (json.has("message")) {
                json.getString("message")
            } else if (json.has("errors")) {
                val errors = json.getString("errors")
                
                // Cek jika formatnya "Validation error: [...]"
                if (errors.startsWith("Validation error:")) {
                    val jsonContent = errors.removePrefix("Validation error:").trim()
                    try {
                        // Hilangkan tanda kutip di awal dan akhir jika ada (karena kadang ter-escape)
                        val cleanJson = if (jsonContent.startsWith("\"") && jsonContent.endsWith("\"")) {
                            jsonContent.substring(1, jsonContent.length - 1)
                                .replace("\\\"", "\"")
                                .replace("\\n", "\n")
                        } else {
                            jsonContent
                        }
                        
                        val errorArray = org.json.JSONArray(cleanJson)
                        if (errorArray.length() > 0) {
                            val firstError = errorArray.getJSONObject(0)
                            if (firstError.has("message")) {
                                return translateError(firstError.getString("message"))
                            }
                        }
                        errors
                    } catch (e: Exception) {
                        // Jika gagal parse array, cari string message manual
                        extractMessage(errors)
                    }
                } else {
                    translateError(errors)
                }
            } else {
                "$defaultAction gagal: $statusCode"
            }
        } catch (e: Exception) {
            errorBody
        }
    }

    private fun extractMessage(rawError: String): String {
        val messageRegex = "\"message\"\\s*:\\s*\"([^\"]+)\"".toRegex()
        val match = messageRegex.find(rawError)
        return if (match != null) {
            translateError(match.groupValues[1])
        } else {
            rawError
        }
    }

    private fun translateError(message: String): String {
        return when {
            message.contains("Email format is invalid", ignoreCase = true) -> "Format email tidak valid."
            message.contains("Password must contain more than or equal to 8 characters", ignoreCase = true) -> "Kata sandi minimal 8 karakter."
            message.contains("Invalid email or password", ignoreCase = true) -> "Email atau kata sandi salah."
            message.contains("Email already exists", ignoreCase = true) -> "Email sudah terdaftar."
            else -> message
        }
    }

    override suspend fun login(email: String, sandi: String): Result<String> {
        return try {
            val response = ApiClient.instance.login(mapOf("email" to email, "password" to sandi))
            Result.success(response.data["token"] ?: throw Exception("Token tidak valid"))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Result.failure(Exception(parseErrorMessage(errorBody, e.code(), "Login")))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(nama: String, email: String, sandi: String): Result<Boolean> {
        return try {
            ApiClient.instance.register(
                mapOf(
                    "name" to nama,
                    "email" to email,
                    "password" to sandi,
                    "role" to "MEMBER"
                )
            )
            Result.success(true)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Result.failure(Exception(parseErrorMessage(errorBody, e.code(), "Daftar")))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
