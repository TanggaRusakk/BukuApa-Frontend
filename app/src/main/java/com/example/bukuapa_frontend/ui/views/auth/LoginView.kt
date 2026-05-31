package com.example.bukuapa_frontend.ui.views.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "BukuApa",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text("Silakan masuk untuk melanjutkan", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(32.dp))

        CustomTextField(value = email, onValueChange = { email = it }, label = "Email")
        Spacer(Modifier.height(16.dp))
        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = "Kata Sandi",
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(24.dp))

        if (error != null) {
            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error
            ); Spacer(Modifier.height(8.dp))
        }

        PrimaryButton(
            text = "Masuk",
            onClick = { viewModel.login(email, password) },
            isLoading = isLoading
        )
        TextButton(onClick = onNavigateToRegister) { Text("Belum punya akun? Daftar") }
    }
}