package com.example.bukuapa_frontend.utils

import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException

object NetworkUtils {

    /**
     * Parses a [Throwable] from a network request into a user-friendly error message.
     * It handles [HttpException] specifically by parsing the error body JSON.
     */
    fun parseError(throwable: Throwable, defaultAction: String = "Operasi"): String {
        if (throwable !is HttpException) {
            return throwable.message ?: "$defaultAction gagal."
        }

        val errorBody = try {
            throwable.response()?.errorBody()?.string()
        } catch (e: Exception) {
            null
        }
        val statusCode = throwable.code()

        if (errorBody == null) return "$defaultAction gagal: $statusCode"

        return try {
            val json = JSONObject(errorBody)
            when {
                json.has("errors") -> {
                    val errors = json.get("errors")
                    if (errors is String) {
                        parseStringError(errors)
                    } else if (errors is JSONArray) {
                        if (errors.length() > 0) {
                            val firstError = errors.getJSONObject(0)
                            if (firstError.has("message")) {
                                translateError(firstError.getString("message"))
                            } else {
                                errors.toString()
                            }
                        } else {
                            "Terjadi kesalahan validasi."
                        }
                    } else {
                        errors.toString()
                    }
                }
                json.has("message") -> translateError(json.getString("message"))
                else -> "$defaultAction gagal: $statusCode"
            }
        } catch (e: Exception) {
            // Jika bukan JSON atau gagal parse, kembalikan string apa adanya (jika pendek) atau pesan default
            if (errorBody.length < 100 && !errorBody.contains("<html>")) {
                translateError(errorBody)
            } else {
                "$defaultAction gagal: $statusCode"
            }
        }
    }

    private fun parseStringError(errors: String): String {
        // Cek jika formatnya "Validation error: [...]"
        if (errors.startsWith("Validation error:")) {
            val jsonContent = errors.removePrefix("Validation error:").trim()
            return try {
                val cleanJson = if (jsonContent.startsWith("\"") && jsonContent.endsWith("\"")) {
                    jsonContent.substring(1, jsonContent.length - 1)
                        .replace("\\\"", "\"")
                        .replace("\\n", "\n")
                } else {
                    jsonContent
                }

                val errorArray = JSONArray(cleanJson)
                if (errorArray.length() > 0) {
                    val firstError = errorArray.getJSONObject(0)
                    if (firstError.has("message")) {
                        translateError(firstError.getString("message"))
                    } else {
                        errors
                    }
                } else {
                    errors
                }
            } catch (e: Exception) {
                extractMessageFromRaw(errors)
            }
        } else {
            return translateError(errors)
        }
    }

    private fun extractMessageFromRaw(rawError: String): String {
        val messageRegex = "\"message\"\\s*:\\s*\"([^\"]+)\"".toRegex()
        val match = messageRegex.find(rawError)
        return if (match != null) {
            translateError(match.groupValues[1])
        } else {
            translateError(rawError)
        }
    }

    private fun translateError(message: String): String {
        return when {
            message.contains("Email format is invalid", ignoreCase = true) -> "Format email tidak valid."
            message.contains("Password must contain more than or equal to 8 characters", ignoreCase = true) -> "Kata sandi minimal 8 karakter."
            message.contains("Invalid email or password", ignoreCase = true) -> "Email atau kata sandi salah."
            message.contains("Email already exists", ignoreCase = true) -> "Email sudah terdaftar."
            message.contains("Book not found", ignoreCase = true) -> "Buku tidak ditemukan."
            message.contains("Unauthorized", ignoreCase = true) -> "Sesi berakhir. Silakan login kembali."
            message.contains("Forbidden", ignoreCase = true) -> "Anda tidak memiliki akses."
            message.contains("User not found", ignoreCase = true) -> "Pengguna tidak ditemukan."
            else -> message
        }
    }
}
