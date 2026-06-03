package com.example.bukuapa_frontend.ui.views.borrowing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bukuapa_frontend.data.models.Loan
import com.example.bukuapa_frontend.ui.viewmodels.borrowing.BorrowingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorrowingView(
    onNavigate: (String) -> Unit,
    role: String = "USER",
    viewModel: BorrowingViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf("Populated") }
    val loans by viewModel.loans.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // --- 1. HEADER ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier.size(40.dp),
                shadowElevation = 2.dp
            ) {
                IconButton(onClick = { onNavigate("catalog") }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color(0xFF1E293B))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                "Riwayat Peminjaman", 
                fontWeight = FontWeight.ExtraBold, 
                fontSize = 22.sp,
                color = Color(0xFF1E293B)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 2. TOGGLE CHIPS ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterChip(
                selected = selectedTab == "Populated",
                onClick = { selectedTab = "Populated" },
                label = { Text("Populated", fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF0D47A1),
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color(0xFF64748B)
                ),
                shape = RoundedCornerShape(24.dp),
                border = if (selectedTab != "Populated") FilterChipDefaults.filterChipBorder(
                    borderColor = Color(0xFFE2E8F0),
                    enabled = true,
                    selected = false
                ) else null
            )
            FilterChip(
                selected = selectedTab == "Empty",
                onClick = { selectedTab = "Empty" },
                label = { Text("Empty", fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF0D47A1),
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color(0xFF64748B)
                ),
                shape = RoundedCornerShape(24.dp),
                border = if (selectedTab != "Empty") FilterChipDefaults.filterChipBorder(
                    borderColor = Color(0xFFE2E8F0),
                    enabled = true,
                    selected = false
                ) else null
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. CONTENT AREA ---
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF0D47A1))
            }
        } else if (selectedTab == "Empty" || loans.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Belum ada riwayat peminjaman", color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(loans) { loan ->
                    BorrowingItemCard(loan, onExtend = { viewModel.extendLoan(it) })
                }
            }
        }
    }
}

@Composable
fun BorrowingItemCard(loan: Loan, onExtend: (Int) -> Unit) {
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
                // Cover Buku
                AsyncImage(
                    model = loan.bookCoverUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .width(70.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF0F2F5)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = loan.bookTitle,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 17.sp,
                                color = Color(0xFF1E293B),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(text = loan.bookAuthor, color = Color(0xFF64748B), fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = statusColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = statusText,
                                color = statusColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
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
                        Text(text = "Pinjam: ${loan.borrowDate}", color = Color(0xFF475569), fontSize = 12.sp)
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
                        Text(text = "Tenggat: ${loan.dueDate}", color = Color(0xFF475569), fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

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

                Button(
                    onClick = { onExtend(loan.id) },
                    enabled = loan.status == "BORROWED" && loan.extensionCount < 2,
                    shape = RoundedCornerShape(24.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    modifier = Modifier.height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0D47A1),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFE2E8F0),
                        disabledContentColor = Color(0xFF94A3B8)
                    )
                ) {
                    Text("Perpanjang", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}
