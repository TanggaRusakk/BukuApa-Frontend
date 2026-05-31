package com.example.bukuapa_frontend.ui.views.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigatorBar(currentRoute: String, role: String, onNavigate: (String) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Book, contentDescription = "Katalog") },
            label = { Text("Katalog") },
            selected = currentRoute == "catalog",
            onClick = { onNavigate("catalog") }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Pinjam") },
            label = { Text("Pinjam") },
            selected = currentRoute == "borrowing",
            onClick = { onNavigate("borrowing") }
        )

        // 🌟 MENU INI HANYA MUNCUL JIKA LOGIN SEBAGAI STAFF
        if (role == "STAFF") {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Settings, contentDescription = "Kelola") },
                label = { Text("Kelola") },
                selected = currentRoute == "manage_book",
                onClick = { onNavigate("manage_book") }
            )
        }
    }
}