package com.example.bukuapa_frontend.ui.views.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bukuapa_frontend.ui.views.components.TopNavigatorBar

@Composable
fun AccountView(
    userRole: String,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = { TopNavigatorBar(title = "Akun Saya") }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF8F9FA)) // Latar abu-abu sangat muda sesuai gaya Katalog
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🌟 FOTO PROFIL
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFF1158C4), CircleShape), // Biru khas BukuApa
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🌟 INFO PENGGUNA
            Text(
                "Pengguna BukuApa",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Label Role (Pill)
            Box(
                modifier = Modifier
                    .background(
                        color = if (userRole == "STAFF") Color(0xFF1158C4).copy(alpha = 0.1f) else Color.LightGray.copy(
                            alpha = 0.3f
                        ),
                        shape = CircleShape
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (userRole == "STAFF") "Staf Perpustakaan" else "Anggota",
                    color = if (userRole == "STAFF") Color(0xFF1158C4) else Color.DarkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 🌟 KARTU INFORMASI (Gaya mirip ManageBookView)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Pengaturan Aplikasi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Versi Aplikasi: 1.0.0", fontSize = 14.sp, color = Color.Gray)
                    Divider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color(0xFFE0E0E0)
                    )
                    Text("Syarat & Ketentuan", fontSize = 14.sp, color = Color.Gray)
                }
            }

            // Mendorong tombol ke bagian paling bawah layar
            Spacer(modifier = Modifier.weight(1f))

            // 🌟 TOMBOL LOGOUT (Pill Shape merah tegas)
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp), // Sama dengan tinggi tombol Register/Login
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F), // Merah peringatan
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Keluar")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Keluar dari Akun", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}