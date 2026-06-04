package com.example.bukuapa_frontend.ui.views.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bukuapa_frontend.ui.viewmodels.book.CatalogViewModel
import com.example.bukuapa_frontend.ui.views.components.BookCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogView(
    onNavigate: (String) -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    role: String = "USER",
    viewModel: CatalogViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }

    val books by viewModel.books.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Logika filter lokal untuk menangani space/case insensitive sesuai permintaan
    val filteredBooks = remember(books, searchQuery) {
        if (searchQuery.isEmpty()) books else {
            val cleanQuery = searchQuery.replace(" ", "").lowercase()
            books.filter { 
                it.title.replace(" ", "").lowercase().contains(cleanQuery) ||
                it.author.replace(" ", "").lowercase().contains(cleanQuery)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // --- 1. HEADER (Profil Tanpa Notif/Menu) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF0D47A1),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    val initial = currentUser?.name?.trim()?.firstOrNull()?.toString()?.uppercase() ?: "P"
                    Text(initial, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text("Halo,", color = Color(0xFF64748B), fontSize = 13.sp)
                Text(
                    currentUser?.name ?: "Pengguna", 
                    fontWeight = FontWeight.ExtraBold, 
                    fontSize = 18.sp,
                    color = Color(0xFF1E293B)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 2. SEARCH BAR ---
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cari judul buku atau penulis...", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B)) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color(0xFF0D47A1)
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. TITLE AREA ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Katalog Buku", 
                fontWeight = FontWeight.ExtraBold, 
                fontSize = 20.sp,
                color = Color(0xFF1E293B)
            )
            TextButton(onClick = { searchQuery = "" }) {
                Text("Lihat semua", color = Color(0xFF0D47A1), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- 4. GRID BUKU ---
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF0D47A1))
            }
        } else if (filteredBooks.isEmpty() && searchQuery.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "tidak dapat menemukan buku", 
                    color = Color(0xFF64748B), 
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredBooks) { book ->
                    BookCard(
                        book = book,
                        onClick = { onNavigateToDetail(book.id) }
                    )
                }
            }
        }
    }
}
