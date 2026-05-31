package com.example.bukuapa_frontend.ui.views.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigatorBar(title: String, onLogoutClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = title, color = MaterialTheme.colorScheme.onPrimary) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        actions = {
            // Tombol Logout di pojok kanan atas
            IconButton(onClick = onLogoutClick) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Keluar Aplikasi",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}