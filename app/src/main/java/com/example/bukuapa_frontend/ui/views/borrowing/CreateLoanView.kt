package com.example.bukuapa_frontend.ui.views.borrowing

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
import com.example.bukuapa_frontend.ui.viewmodels.borrowing.BorrowingViewModel
import com.example.bukuapa_frontend.ui.views.components.CustomTextField
import com.example.bukuapa_frontend.ui.views.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLoanView(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: BorrowingViewModel = viewModel()
) {
    var isbn by remember { mutableStateOf("") }
    
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Peminjaman Baru", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
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
                "ISBN Buku",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            CustomTextField(
                value = isbn,
                onValueChange = { isbn = it },
                label = "Masukkan ISBN Buku"
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Simpan Peminjaman",
                onClick = {
                    if (isbn.isNotBlank()) {
                        viewModel.createLoanByIsbn(isbn, 
                            onSuccess = { onSaveSuccess() },
                            onError = { error ->
                                errorMessage = error
                                showError = true
                            }
                        )
                    } else {
                        errorMessage = "ISBN Buku tidak boleh kosong"
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
