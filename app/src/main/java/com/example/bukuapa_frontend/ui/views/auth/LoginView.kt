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
import com.example.bukuapa_frontend.ui.viewmodels.auth.LoginViewModel
import com.example.bukuapa_frontend.ui.views.components.CustomTextField
import com.example.bukuapa_frontend.ui.views.components.PrimaryButton

@Composable
fun LoginView(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val token by viewModel.loginToken.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    LaunchedEffect(token) { token?.let { onLoginSuccess(it) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))

        // 🌟 LOGO BUKUAPA
        Image(
            painter = painterResource(R.drawable.bukuapa),
            contentDescription = "Logo BukuApa",
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(Modifier.height(16.dp))
        Text("BukuApa", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1158C4))

        Spacer(Modifier.height(40.dp))

        // 🌟 TEKS SELAMAT DATANG (Rata Kiri Sesuai Gambar)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Selamat Datang",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(Modifier.height(4.dp))
            Text("Masuk untuk meminjam buku favoritmu", fontSize = 14.sp, color = Color.Gray)
        }

        Spacer(Modifier.height(24.dp))

        // 🌟 TEXTFIELDS
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email"
        )

        Spacer(Modifier.height(16.dp))

        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = "Kata Sandi",
            isPassword = true
        )

        Spacer(Modifier.height(16.dp))

        if (error != null) {
            Text(text = error!!, color = Color.Red, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
        }

        PrimaryButton(
            text = "Masuk",
            onClick = { viewModel.login(email, password) },
            isLoading = isLoading
        )

        // 🌟 MENDORONG TEKS KE PALING BAWAH
        Spacer(Modifier.weight(1f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Belum punya akun? ", color = Color.Gray, fontSize = 14.sp)
            TextButton(onClick = onNavigateToRegister, contentPadding = PaddingValues(0.dp)) {
                Text(
                    "Daftar",
                    color = Color(0xFF1158C4),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}