package com.example.bukuapa_frontend.ui.views.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun BottomNavigatorBar(currentRoute: String, role: String, onNavigate: (String) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        // --- 1. TAB BUKU / KATALOG (Semua Role) ---
        val isCatalog = currentRoute == "catalog"
        NavigationBarItem(
            icon = { Icon(Icons.Default.MenuBook, contentDescription = "Buku") },
            label = {
                Text(
                    "Buku",
                    fontWeight = if (isCatalog) FontWeight.Bold else FontWeight.Normal
                )
            },
            selected = isCatalog,
            onClick = { onNavigate("catalog") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1158C4),
                selectedTextColor = Color(0xFF1158C4),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        // --- 2. TAB PEMINJAMAN (Semua Role) ---
        val isBorrowing = currentRoute == "borrowing"
        NavigationBarItem(
            icon = { Icon(Icons.Default.Assignment, contentDescription = "Peminjaman") },
            label = {
                Text(
                    "Peminjaman",
                    fontWeight = if (isBorrowing) FontWeight.Bold else FontWeight.Normal
                )
            },
            selected = isBorrowing,
            onClick = { onNavigate("borrowing") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1158C4),
                selectedTextColor = Color(0xFF1158C4),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        // --- 3. TAB KELOLA (Hanya STAFF) ---
        if (role == "STAFF") {
            val isManage = currentRoute == "manage_book"
            NavigationBarItem(
                icon = { Icon(Icons.Default.Settings, contentDescription = "Kelola") },
                label = {
                    Text(
                        "Kelola",
                        fontWeight = if (isManage) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = isManage,
                onClick = { onNavigate("manage_book") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF1158C4),
                    selectedTextColor = Color(0xFF1158C4),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }

        // --- 4. TAB AKUN (Semua Role) ---
        val isAccount = currentRoute == "akun"
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Akun") },
            label = {
                Text(
                    "Akun",
                    fontWeight = if (isAccount) FontWeight.Bold else FontWeight.Normal
                )
            },
            selected = isAccount,
            onClick = { onNavigate("akun") }, // Pastikan rute "akun" ditambahkan di AppNavigation nanti
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1158C4),
                selectedTextColor = Color(0xFF1158C4),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}