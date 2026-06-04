package com.example.bukuapa_frontend.ui.views.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bukuapa_frontend.ui.viewmodels.auth.AccountViewModel
import com.example.bukuapa_frontend.ui.views.components.TopNavigatorBar

@Composable
fun AccountView(
    userRole: String,
    onLogout: () -> Unit,
    viewModel: AccountViewModel = viewModel()
) {
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUserInfo()
    }

    Scaffold(
        topBar = { TopNavigatorBar(title = "Akun Saya") }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color(0xFFF8FAFC)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF0D47A1))
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color(0xFFF8FAFC))
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // PROFILE PICTURE
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color(0xFF0D47A1), CircleShape),
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

                // USER NAME
                Text(
                    user?.name ?: "Pengguna BukuApa",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))

                // USER ID
                Text(
                    "ID: ${user?.id ?: "-"}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))

                // ROLE PILL
                Box(
                    modifier = Modifier
                        .background(
                            color = if (userRole == "STAFF") Color(0xFF0D47A1).copy(alpha = 0.1f) else Color.LightGray.copy(
                                alpha = 0.3f
                            ),
                            shape = CircleShape
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (userRole == "STAFF") "Staf Perpustakaan" else "Anggota",
                        color = if (userRole == "STAFF") Color(0xFF0D47A1) else Color.DarkGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // PROFILE INFO CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Data Diri",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow("Nama", user?.name ?: "-")
                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color(0xFFE0E0E0)
                        )

                        InfoRow("Email", user?.email ?: "-")
                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color(0xFFE0E0E0)
                        )

                        InfoRow("Role", if (userRole == "STAFF") "Staf Perpustakaan" else "Anggota")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // APP INFO CARD
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
                        Text("Versi Aplikasi: 1.0", fontSize = 14.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // LOGOUT BUTTON
                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC62828),
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Keluar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Keluar dari Akun", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            fontSize = 13.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            value,
            fontSize = 13.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
    }
}