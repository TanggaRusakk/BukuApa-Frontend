package com.example.bukuapa_frontend.ui.views.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, isLoading: Boolean = false) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), // Tinggi sesuai gambar
        shape = CircleShape, // 🌟 Membuatnya berbentuk pil seperti di gambar
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1158C4), // 🌟 Warna biru persis Figma
            contentColor = Color.White
        )
    ) {
        Text(
            text = if (isLoading) "Memuat..." else text,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}