package com.example.bukuapa_frontend.ui.views.borrowing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
    var userId by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Atur Peminjaman User", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color(0xFF1E293B))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            if (showError) {
                Surface(
                    color = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                ) {
                    Text(
                        errorMessage,
                        modifier = Modifier.padding(16.dp),
                        color = Color(0xFFC62828),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Text(
                "ID User",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF475569),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            CustomTextField(
                value = userId,
                onValueChange = { userId = it },
                label = "Masukkan ID User (contoh: 123)"
            )
            
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "ISBN Buku",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF475569),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            CustomTextField(
                value = isbn,
                onValueChange = { isbn = it },
                label = "Masukkan ISBN Buku"
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Simpan Peminjaman",
                onClick = {
                    val uId = userId.toIntOrNull()
                    if (uId == null || uId <= 0) {
                        errorMessage = "ID User harus berupa angka positif"
                        showError = true
                        return@PrimaryButton
                    }
                    if (isbn.isBlank()) {
                        errorMessage = "ISBN Buku tidak boleh kosong"
                        showError = true
                        return@PrimaryButton
                    }

                    viewModel.createLoanByIsbnForUser(uId, isbn, 
                        onSuccess = { onSaveSuccess() },
                        onError = { error ->
                            errorMessage = error
                            showError = true
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                isLoading = isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
