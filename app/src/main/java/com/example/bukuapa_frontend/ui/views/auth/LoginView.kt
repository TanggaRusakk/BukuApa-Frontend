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
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Spacer(Modifier.height(16.dp))
        Text(
            "BukuApa", 
            fontSize = 32.sp, 
            fontWeight = FontWeight.ExtraBold, 
            color = Color(0xFF0D47A1)
        )
        Text(
            "Perpustakaan digital di genggaman", 
            fontSize = 14.sp, 
            color = Color(0xFF64748B)
        )

        Spacer(Modifier.height(48.dp))

        // 🌟 TEKS SELAMAT DATANG
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Selamat Datang",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1E293B)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Masuk untuk meminjam buku favoritmu", 
                fontSize = 15.sp, 
                color = Color(0xFF64748B)
            )
        }

        Spacer(Modifier.height(32.dp))

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

        Spacer(Modifier.height(24.dp))

        if (error != null) {
            Surface(
                color = Color(0xFFFFEBEE),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = error!!, 
                    color = Color(0xFFC62828), 
                    fontSize = 13.sp,
                    modifier = Modifier.padding(12.dp),
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        PrimaryButton(
            text = "Masuk",
            onClick = { viewModel.login(email, password) },
            isLoading = isLoading
        )

        // 🌟 MENDORONG TEKS KE PALING BAWAH
        Spacer(Modifier.weight(1f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Belum punya akun? ", color = Color(0xFF64748B), fontSize = 14.sp)
            TextButton(onClick = onNavigateToRegister, contentPadding = PaddingValues(0.dp)) {
                Text(
                    "Daftar",
                    color = Color(0xFF0D47A1),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
