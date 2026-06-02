package com.example.bukuapa_frontend.ui.views.book

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bukuapa_frontend.data.models.Book
import com.example.bukuapa_frontend.ui.views.components.BookCard
import com.example.bukuapa_frontend.ui.views.components.BottomNavigatorBar
import com.example.bukuapa_frontend.ui.views.components.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogView(
    onNavigate: (String) -> Unit,
    role: String = "USER" // Default biar navigasinya aman
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    val categories = listOf("Semua", "Fiksi", "Sejarah", "Filsafat", "Anak")

    // Data bohongan (Dummy) biar layarnya bisa nampilin buku persis kayak di Figma
    val dummyBooks = listOf(
        Book(title = "Laskar Pelangi", author = "Andrea Hirata", stock = 5),
        Book(title = "Bumi Manusia", author = "Pramoedya A. Toer", stock = 2),
        Book(title = "Filosofi Teras", author = "Henry Manampiring", stock = 0),
        Book(title = "The Fine Print", author = "Lauren Asher", stock = 3)
    )

    Scaffold(
        bottomBar = {
            BottomNavigatorBar(
                currentRoute = "catalog",
                role = role,
                onNavigate = onNavigate
            )
        },
        containerColor = Color(0xFFF5F7FA) // Warna background abu-abu kebiruan tipis
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header: Foto Profil (Huruf P) & Notifikasi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF1158C4),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("P", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Halo,", color = Color.Gray, fontSize = 12.sp)
                        Text("Pengguna", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                IconButton(onClick = { /* TODO Notif */ }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifikasi", tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar buatan Angga
            CustomTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = "Cari judul buku atau penulis..."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Kategori (Semua, Fiksi, dll)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF1158C4),
                            selectedLabelColor = Color.White
                        ),
                        shape = CircleShape
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Judul "Katalog Buku" & Tombol "Lihat semua"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Katalog Buku", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                TextButton(onClick = { /* TODO Lihat Semua */ }) {
                    Text("Lihat semua", color = Color(0xFF1158C4), fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Daftar Grid Bukunya
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(dummyBooks) { book ->
                    BookCard(book = book)
                }
            }
        }
    }
}