package com.example.bukuapa_frontend.ui.views.book

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bukuapa_frontend.ui.viewmodels.book.ManageBookViewModel
import com.example.bukuapa_frontend.ui.views.components.TopNavigatorBar

@Composable
fun ManageBookView(
    onLogout: () -> Unit, // 🌟 Tambahan parameter untuk aksi logout
    viewModel: ManageBookViewModel = viewModel()
) {
    val books by viewModel.books.collectAsState()

    LaunchedEffect(Unit) { viewModel.fetchBooks() }

    Scaffold(
        // 🌟 Memasang fungsi onLogout ke komponen TopNavigatorBar
        topBar = { TopNavigatorBar(title = "Kelola Inventaris", onLogoutClick = onLogout) },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Form Tambah Buku */ }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Buku")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(books) { book ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = book.title, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "ISBN: ${book.isbn}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Stok: ${book.stock} | Hal: ${book.totalPages}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        IconButton(onClick = { viewModel.deleteBook(book.id) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Hapus",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}