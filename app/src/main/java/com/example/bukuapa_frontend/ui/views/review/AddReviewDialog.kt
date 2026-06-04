package com.example.bukuapa_frontend.ui.views.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bukuapa_frontend.data.models.CreateReviewRequest
import com.example.bukuapa_frontend.domain.protocols.ReviewServiceProtocol
import kotlinx.coroutines.launch

@Composable
fun AddReviewDialog(
    bookId: Int,
    service: ReviewServiceProtocol,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tulis Ulasan") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Rating", style = MaterialTheme.typography.labelLarge)
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(5) { index ->
                        val currentStar = index + 1
                        Icon(
                            imageVector = if (currentStar <= rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = if (currentStar <= rating) Color(0xFF0D47A1) else Color.Gray,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { rating = currentStar }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Komentar") },
                    placeholder = { Text("Ceritakan pengalaman Anda membaca buku ini...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isSubmitting = true
                    errorMessage = null
                    scope.launch {
                        val result = service.createReview(
                            CreateReviewRequest(bookId, rating, comment)
                        )
                        isSubmitting = false
                        if (result.isSuccess) {
                            onSuccess()
                        } else {
                            errorMessage = result.exceptionOrNull()?.message ?: "Gagal mengirim ulasan"
                        }
                    }
                },
                enabled = !isSubmitting && comment.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0D47A1),
                    contentColor = Color.White
                )
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Kirim")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSubmitting) {
                Text("Batal")
            }
        }
    )
}
