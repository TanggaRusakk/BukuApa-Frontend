package com.example.bukuapa_frontend.ui.views.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bukuapa_frontend.data.models.Book
import com.example.bukuapa_frontend.ui.viewmodels.book.ManageBookViewModel
import com.example.bukuapa_frontend.ui.views.components.CustomTextField
import com.example.bukuapa_frontend.ui.views.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUpdateBookView(
    book: Book?,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: ManageBookViewModel = viewModel()
) {
    var isbn by remember { mutableStateOf(book?.isbn ?: "") }
    var title by remember { mutableStateOf(book?.title ?: "") }
    var author by remember { mutableStateOf(book?.author ?: "") }
    var publisher by remember { mutableStateOf(book?.publisher ?: "") }
    var publishedYear by remember { mutableStateOf(book?.publishedYear?.toString() ?: "") }
    var totalPages by remember { mutableStateOf(book?.totalPages?.toString() ?: "") }
    var stock by remember { mutableStateOf(book?.stock?.toString() ?: "") }
    
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val isEditMode = book != null
    val pageTitle = if (isEditMode) "Edit Buku" else "Tambah Buku Baru"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pageTitle, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (showError) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Text(
                        errorMessage,
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        color = Color(0xFFC62828),
                        fontSize = 12.sp
                    )
                }
            }

            Text(
                "ISBN",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            CustomTextField(
                value = isbn,
                onValueChange = { isbn = it },
                label = "ISBN Buku"
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Judul",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            CustomTextField(
                value = title,
                onValueChange = { title = it },
                label = "Judul Buku"
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Pengarang",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            CustomTextField(
                value = author,
                onValueChange = { author = it },
                label = "Nama Pengarang"
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Penerbit",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            CustomTextField(
                value = publisher,
                onValueChange = { publisher = it },
                label = "Nama Penerbit"
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Tahun Terbit",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    CustomTextField(
                        value = publishedYear,
                        onValueChange = { publishedYear = it },
                        label = "Tahun"
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Total Halaman",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    CustomTextField(
                        value = totalPages,
                        onValueChange = { totalPages = it },
                        label = "Halaman"
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Stok",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            CustomTextField(
                value = stock,
                onValueChange = { stock = it },
                label = "Jumlah Stok"
            )
            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = if (isEditMode) "Perbarui Buku" else "Simpan Buku",
                onClick = {
                    if (validateForm(title, author, publisher, isbn)) {
                        val newBook = Book(
                            id = book?.id ?: 0,
                            isbn = isbn,
                            title = title,
                            author = author,
                            publisher = publisher,
                            publishedYear = publishedYear.toIntOrNull() ?: 0,
                            totalPages = totalPages.toIntOrNull() ?: 0,
                            stock = stock.toIntOrNull() ?: 0
                        )

                        if (isEditMode && book != null) {
                            viewModel.updateBook(book.id, newBook,
                                onSuccess = { onSaveSuccess() },
                                onError = { error ->
                                    errorMessage = error
                                    showError = true
                                }
                            )
                        } else {
                            viewModel.createBook(newBook,
                                onSuccess = { onSaveSuccess() },
                                onError = { error ->
                                    errorMessage = error
                                    showError = true
                                }
                            )
                        }
                    } else {
                        errorMessage = "Harap isi semua field yang diperlukan"
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isLoading = isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun validateForm(title: String, author: String, publisher: String, isbn: String): Boolean {
    return title.isNotBlank() && author.isNotBlank() && publisher.isNotBlank() && isbn.isNotBlank()
}
