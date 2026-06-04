package com.example.bukuapa_frontend.ui.views.borrowing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bukuapa_frontend.data.models.Loan
import com.example.bukuapa_frontend.ui.viewmodels.borrowing.BorrowingViewModel
import com.example.bukuapa_frontend.ui.views.components.TopNavigatorBar

@Composable
fun BorrowingView(
    onNavigateToCreate: () -> Unit,
    role: String = "USER",
    viewModel: BorrowingViewModel = viewModel()
) {
    val loans by viewModel.loans.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Force refresh setiap kali masuk ke screen ini
    LaunchedEffect(Unit) {
        viewModel.loadLoans()
    }

    Scaffold(
        topBar = { TopNavigatorBar(title = "Riwayat Peminjaman") },
        floatingActionButton = {
            if (role == "STAFF") {
                FloatingActionButton(
                    onClick = onNavigateToCreate,
                    containerColor = Color(0xFF0D47A1),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Peminjaman")
                }
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            // Tampilan error (jika ada)
            if (errorMessage != null) {
                val displayError = if (errorMessage!!.contains("<!DOCTYPE html>")) {
                    "Endpoint API belum di-deploy. Silahkan tunggu."
                } else {
                    errorMessage
                }
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                    Text(text = "Error: $errorMessage", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }

            if (isLoading && loans.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF0D47A1))
                }
            } else if (loans.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Belum ada riwayat peminjaman",
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
                ) {
                    items(loans) { loan ->
                        BorrowingItemCard(
                            loan = loan,
                            role = role,
                            onExtend = { viewModel.extendLoan(it) },
                            onReturn = { viewModel.returnLoan(it) } // Aksi return disambungin ke ViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BorrowingItemCard(loan: Loan, role: String, onExtend: (Int) -> Unit, onReturn: (Int) -> Unit) {
    // Mengambil data dari nested object 'book' dan 'user' hasil include di backend
    val bookTitle = loan.book?.title ?: "Buku ID: ${loan.bookId}"
    val bookAuthor = loan.book?.author ?: "Penulis tidak diketahui"
    val bookCoverUrl = loan.book?.coverUrl
    val borrowerName = loan.user?.name ?: "User ID: ${loan.userId}"

    val statusText = when (loan.status) {
        "BORROWED" -> "Sedang Dipinjam"
        "OVERDUE" -> "Terlambat"
        "RETURNED" -> "Dikembalikan"
        else -> loan.status
    }

    val statusColor = when (loan.status) {
        "BORROWED" -> Color(0xFF2E7D32)
        "OVERDUE" -> Color(0xFFC62828)
        else -> Color(0xFF64748B)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Placeholder Cover Buku
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F5F9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = bookTitle,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 17.sp,
                                color = Color(0xFF1E293B),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            // FIX TEKS VERTIKAL: Pake Modifier.weight biar kepotong rapi
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = bookAuthor,
                                    color = Color(0xFF64748B),
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                // Kalau Staff, tampilin nama Peminjamnya
                                if (role == "STAFF") {
                                    Text(text = " • ", color = Color.LightGray)
                                    Text(
                                        text = borrowerName,
                                        color = Color(0xFF0D47A1),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = statusColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = statusText,
                                color = statusColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // Bersihkan format tanggal jika perlu
                        val displayBorrowDate = loan.borrowDate.split("T").firstOrNull() ?: loan.borrowDate
                        Text(text = "Pinjam: $displayBorrowDate", color = Color(0xFF475569), fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        val displayDueDate = loan.dueDate.split("T").firstOrNull() ?: loan.dueDate
                        Text(text = "Tenggat: $displayDueDate", color = Color(0xFF475569), fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // FIX TOMBOL BAWAH
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Perpanjangan ${loan.extensionCount}/2",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                // Kumpulan Tombol di sebelah kanan
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Tombol Kembalikan (Khusus Staff & Kalau statusnya masih dipinjam/terlambat)
                    if (role == "STAFF" && loan.status != "RETURNED") {
                        OutlinedButton(
                            onClick = { onReturn(loan.id) },
                            shape = RoundedCornerShape(24.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Kembalikan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Tombol Perpanjang (Khusus User & Jatah masih ada)
                    if (role != "STAFF") {
                        Button(
                            onClick = { onExtend(loan.id) },
                            enabled = loan.status == "BORROWED" && loan.extensionCount < 2,
                            shape = RoundedCornerShape(24.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            modifier = Modifier.height(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0D47A1),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFFE2E8F0),
                                disabledContentColor = Color(0xFF94A3B8)
                            )
                        ) {
                            Text("Perpanjang", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}