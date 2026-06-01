package com.example.bukuapa_frontend.ui.views.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bukuapa_frontend.R
import com.example.bukuapa_frontend.ui.viewmodels.auth.RegisterViewModel
import com.example.bukuapa_frontend.ui.views.components.CustomTextField
import com.example.bukuapa_frontend.ui.views.components.PrimaryButton

@Composable
fun RegisterView(
    viewModel: RegisterViewModel = viewModel(),
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    LaunchedEffect(isSuccess) { if (isSuccess) onNavigateToLogin() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))

        // 🌟 LOGO BUKUAPA SESUAI GAMBAR
        Image(
            painter = painterResource(R.drawable.bukuapa),
            contentDescription = "Logo BukuApa",
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(Modifier.height(16.dp))
        Text("BukuApa", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1158C4))
        Spacer(Modifier.height(8.dp))
        Text("Perpustakaan digital di genggaman", fontSize = 14.sp, color = Color(0xFF5A5A5A))

        Spacer(Modifier.height(40.dp))

        // 🌟 TEXTFIELDS
        CustomTextField(value = name, onValueChange = { name = it }, label = "Nama Lengkap")
        Spacer(Modifier.height(16.dp))
        CustomTextField(value = email, onValueChange = { email = it }, label = "Email")
        Spacer(Modifier.height(16.dp))
        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = "Kata Sandi",
            isPassword = true
        )

        Spacer(Modifier.height(32.dp))
        if (error != null) {
            Text(text = error!!, color = Color.Red, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
        }

        PrimaryButton(
            text = "Daftar",
            onClick = { viewModel.register(name, email, password) },
            isLoading = isLoading
        )

        // 🌟 MENDORONG TEKS KE PALING BAWAH
        Spacer(Modifier.weight(1f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Sudah punya akun? ", color = Color.Gray, fontSize = 14.sp)
            TextButton(onClick = onNavigateToLogin, contentPadding = PaddingValues(0.dp)) {
                Text(
                    "Masuk",
                    color = Color(0xFF1158C4),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}