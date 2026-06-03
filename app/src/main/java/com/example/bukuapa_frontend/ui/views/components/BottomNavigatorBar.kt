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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigatorBar(currentRoute: String, role: String, onNavigate: (String) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFF64748B),
        tonalElevation = 8.dp
    ) {
        // Biru Utama Premium
        val primaryBlue = Color(0xFF0D47A1)
        val inactiveGray = Color(0xFF94A3B8)

        // --- 1. TAB BUKU / KATALOG ---
        val isCatalog = currentRoute == "catalog"
        NavigationBarItem(
            icon = { Icon(Icons.Default.MenuBook, contentDescription = "Buku") },
            label = {
                Text(
                    "Buku",
                    fontWeight = if (isCatalog) FontWeight.ExtraBold else FontWeight.Medium,
                    fontSize = 12.sp
                )
            },
            selected = isCatalog,
            onClick = { onNavigate("catalog") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = primaryBlue,
                selectedTextColor = primaryBlue,
                unselectedIconColor = inactiveGray,
                unselectedTextColor = inactiveGray,
                indicatorColor = primaryBlue.copy(alpha = 0.1f)
            )
        )

        // --- 2. TAB PEMINJAMAN ---
        val isBorrowing = currentRoute == "borrowing"
        NavigationBarItem(
            icon = { Icon(Icons.Default.Assignment, contentDescription = "Peminjaman") },
            label = {
                Text(
                    "Peminjaman",
                    fontWeight = if (isBorrowing) FontWeight.ExtraBold else FontWeight.Medium,
                    fontSize = 11.sp
                )
            },
            selected = isBorrowing,
            onClick = { onNavigate("borrowing") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = primaryBlue,
                selectedTextColor = primaryBlue,
                unselectedIconColor = inactiveGray,
                unselectedTextColor = inactiveGray,
                indicatorColor = primaryBlue.copy(alpha = 0.1f)
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
                        fontWeight = if (isManage) FontWeight.ExtraBold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                },
                selected = isManage,
                onClick = { onNavigate("manage_book") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = primaryBlue,
                    selectedTextColor = primaryBlue,
                    unselectedIconColor = inactiveGray,
                    unselectedTextColor = inactiveGray,
                    indicatorColor = primaryBlue.copy(alpha = 0.1f)
                )
            )
        }

        // --- 4. TAB AKUN ---
        val isAccount = currentRoute == "akun"
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Akun") },
            label = {
                Text(
                    "Akun",
                    fontWeight = if (isAccount) FontWeight.ExtraBold else FontWeight.Medium,
                    fontSize = 12.sp
                )
            },
            selected = isAccount,
            onClick = { onNavigate("akun") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = primaryBlue,
                selectedTextColor = primaryBlue,
                unselectedIconColor = inactiveGray,
                unselectedTextColor = inactiveGray,
                indicatorColor = primaryBlue.copy(alpha = 0.1f)
            )
        )
    }
}
