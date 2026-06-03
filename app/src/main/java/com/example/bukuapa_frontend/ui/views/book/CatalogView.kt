package com.example.bukuapa_frontend.ui.views.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bukuapa_frontend.ui.viewmodels.book.CatalogViewModel
import com.example.bukuapa_frontend.ui.views.components.BookCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogView(
    onNavigate: (String) -> Unit,
    role: String = "USER",
    viewModel: CatalogViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableIntStateOf(0) }

    val books by viewModel.books.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // --- 1. HEADER (Profil & Notif) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF0D47A1),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        val initial = currentUser?.name?.firstOrNull()?.toString()?.uppercase() ?: "P"
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
            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier.size(40.dp),
                shadowElevation = 2.dp
            ) {
                IconButton(onClick = { /* TODO Notif */ }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifikasi", tint = Color(0xFF475569))
                }
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
                onValueChange = { 
                    searchQuery = it
                    viewModel.loadBooks(searchQuery, selectedCategoryId)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cari judul buku atau penulis...", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B)) },
                trailingIcon = { 
                    IconButton(onClick = { /* TODO Sort/Filter */ }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_sort_by_size), 
                            contentDescription = "Sort", 
                            tint = Color(0xFF64748B)
                        )
                    }
                },
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

        Spacer(modifier = Modifier.height(20.dp))

        // --- 3. CATEGORY CHIPS ---
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(categories) { category ->
                val isSelected = selectedCategoryId == category.id
                FilterChip(
                    selected = isSelected,
                    onClick = { 
                        selectedCategoryId = category.id
                        viewModel.loadBooks(searchQuery, selectedCategoryId)
                    },
                    label = { 
                        Text(
                            category.name, 
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        ) 
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF0D47A1),
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = Color(0xFF64748B)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    border = if (!isSelected) FilterChipDefaults.filterChipBorder(
                        borderColor = Color(0xFFE2E8F0),
                        enabled = true,
                        selected = false
                    ) else null
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 4. TITLE AREA ---
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
            TextButton(onClick = { /* TODO Lihat Semua */ }) {
                Text("Lihat semua", color = Color(0xFF0D47A1), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- 5. GRID BUKU ---
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF0D47A1))
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(books) { book ->
                    BookCard(book = book)
                }
            }
        }
    }
}
