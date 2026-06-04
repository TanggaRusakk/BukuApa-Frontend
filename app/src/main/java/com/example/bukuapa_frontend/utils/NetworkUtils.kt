package com.example.bukuapa_frontend.utils

import org.json.JSONObject
import retrofit2.HttpException

object NetworkUtils {
    fun parseErrorMessage(e: Exception, defaultAction: String): String {
        if (e is HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody == null) return "$defaultAction gagal: ${e.code()}"
            
            return try {
                val json = JSONObject(errorBody)
                if (json.has("message")) {
                    translateError(json.getString("message"))
                } else if (json.has("errors")) {
                    val errors = json.getString("errors")
                    if (errors.startsWith("Validation error:")) {
                        extractFirstValidationMessage(errors)
                    } else {
                        translateError(errors)
                    }
                } else {
                    "$defaultAction gagal: ${e.code()}"
                }
            } catch (ex: Exception) {
                errorBody
            }
        }
        return e.message ?: "$defaultAction gagal."
    }

    private fun extractFirstValidationMessage(rawError: String): String {
        return try {
            val jsonContent = rawError.removePrefix("Validation error:").trim()
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
            rawError
        } catch (e: Exception) {
            val messageRegex = "\"message\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            val match = messageRegex.find(rawError)
            if (match != null) translateError(match.groupValues[1]) else rawError
        }
    }

    private fun translateError(message: String): String {
        return when {
            message.contains("Email format is invalid", ignoreCase = true) -> "Format email tidak valid."
            message.contains("Password must contain more than or equal to 8 characters", ignoreCase = true) -> "Kata sandi minimal 8 karakter."
            message.contains("Invalid email or password", ignoreCase = true) -> "Email atau kata sandi salah."
            message.contains("Email already exists", ignoreCase = true) -> "Email sudah terdaftar."
            message.contains("Book not found", ignoreCase = true) -> "Buku tidak ditemukan."
            message.contains("Stock is not enough", ignoreCase = true) -> "Stok buku habis."
            else -> message
        }
    }
}
