package com.example.bukuapa_frontend.ui.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bukuapa_frontend.data.models.Book

@Composable
fun BookCard(
    book: Book,
    // Dummy rating
    rating: Double = 4.8,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp), // Jarak antar card di dalam grid
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Efek bayangan tipis ala desain
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Kotak abu-abu buat Placeholder Gambar Cover
            // (Nanti bisa diganti Coil AsyncImage kalau udah nyambung backend beneran)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0E0E0))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Judul Buku
            Text(
                text = book.title.ifEmpty { "Judul Buku" },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Penulis
            Text(
                text = book.author.ifEmpty { "Nama Penulis" },
                color = Color.Gray,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Baris Bawah: Rating & Stok
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rating (Bintang + Angka)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107), // Kuning Emas
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = rating.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }

                // Status Stok
                Text(
                    text = "Stok ${book.stock}",
                    // Hijau kalau ada stok, Merah kalau kosong
                    color = if (book.stock > 0) Color(0xFF4CAF50) else Color(0xFFF44336),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
        }
    }
}