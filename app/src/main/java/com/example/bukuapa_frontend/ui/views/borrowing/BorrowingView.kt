package com.example.bukuapa_frontend.ui.views.borrowing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bukuapa_frontend.ui.views.components.BottomNavigatorBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorrowingView(
    onNavigate: (String) -> Unit,
    role: String = "USER"
) {
    var selectedTab by remember { mutableStateOf("Populated") }

    // Data dummy biar UI-nya persis kayak desain Figma
    val dummyLoans = listOf(
        LoanDummy("Laskar Pelangi", "Andrea Hirata", "20 Mei 2026", "03 Jun 2026", "Sedang Dipinjam", 0),
        LoanDummy("Bumi Manusia", "Pramoedya A. Toer", "10 Mei 2026", "24 Mei 2026", "Terlambat", 2),
        LoanDummy("Filosofi Teras", "Henry Manampiring", "01 Apr 2026", "15 Apr 2026", "Dikembalikan", 0)
    )

    Scaffold(
        bottomBar = {
            BottomNavigatorBar(
                currentRoute = "borrowing",
                role = role,
                onNavigate = onNavigate
            )
        },
        containerColor = Color(0xFFF5F7FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header: Tombol Back & Judul
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { /* TODO Aksi Back */ }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Riwayat Peminjaman", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Toggle Button (Populated / Empty)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedTab == "Populated",
                    onClick = { selectedTab = "Populated" },
                    label = { Text("Populated") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF1158C4),
                        selectedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                FilterChip(
                    selected = selectedTab == "Empty",
                    onClick = { selectedTab = "Empty" },
                    label = { Text("Empty") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.White,
                        selectedLabelColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(20.dp),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = Color.LightGray,
                        enabled = true,
                        selected = false
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Area List Buku
            if (selectedTab == "Populated") {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(dummyLoans) { loan ->
                        BorrowingItemCard(loan)
                    }
                }
            } else {
                // Tampilan State Kosong
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada riwayat peminjaman", color = Color.Gray)
                }
            }
        }
    }
}

// --- Komponen Internal Khusus Card Peminjaman ---
data class LoanDummy(
    val title: String, val author: String, val borrowDate: String, val dueDate: String, val status: String, val extensionCount: Int
)

@Composable
fun BorrowingItemCard(loan: LoanDummy) {
    val statusColor = when (loan.status) {
        "Sedang Dipinjam" -> Color(0xFF4CAF50) // Hijau
        "Terlambat" -> Color(0xFFF44336)       // Merah
        else -> Color.Gray                     // Abu-abu
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Cover Buku Placeholder
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(90.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E0E0))
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Detail Buku & Status
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = loan.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(text = loan.author, color = Color.Gray, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        // Label Status
                        Text(
                            text = loan.status,
                            color = statusColor,
                            fontSize = 10.sp,
                            modifier = Modifier
                                .background(statusColor.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tanggal Pinjam & Tenggat
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Pinjam: ${loan.borrowDate}", color = Color.Gray, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Tenggat: ${loan.dueDate}", color = Color.Gray, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0)) // Garis pemisah tipis
            Spacer(modifier = Modifier.height(8.dp))

            // Bagian Bawah: Info Perpanjangan & Tombol
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Perpanjangan ${loan.extensionCount}/2", color = Color.Gray, fontSize = 12.sp)

                OutlinedButton(
                    onClick = { /* TODO Aksi Perpanjang */ },
                    // Tombol nyala cuma kalau status Sedang Dipinjam dan jatah belum habis
                    enabled = loan.status == "Sedang Dipinjam" && loan.extensionCount < 2,
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Perpanjang", fontSize = 12.sp)
                }
            }
        }
    }
}