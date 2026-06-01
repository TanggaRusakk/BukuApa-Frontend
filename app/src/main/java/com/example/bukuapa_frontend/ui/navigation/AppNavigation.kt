package com.example.bukuapa_frontend.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.auth0.android.jwt.JWT
import com.example.bukuapa_frontend.ui.views.auth.AccountView
import com.example.bukuapa_frontend.ui.views.auth.LoginView
import com.example.bukuapa_frontend.ui.views.auth.RegisterView
import com.example.bukuapa_frontend.ui.views.book.ManageBookView
import com.example.bukuapa_frontend.ui.views.components.BottomNavigatorBar
import com.example.bukuapa_frontend.ui.views.components.TopNavigatorBar
import com.example.bukuapa_frontend.utils.TokenManager
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Persiapan membaca data lokal & coroutine
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val tokenManager = remember { TokenManager(context) }

    // Memantau keberadaan token di dalam HP
    val token by tokenManager.getToken().collectAsState(initial = null)

    // Membaca role secara reaktif dan instan (tanpa delay)
    val userRole = remember(token) {
        if (token != null) {
            try {
                // Menggunakan uppercase() agar kebal terhadap penulisan "staff" atau "STAFF"
                JWT(token!!).getClaim("role").asString()?.uppercase() ?: "MEMBER"
            } catch (e: Exception) {
                "MEMBER"
            }
        } else {
            "MEMBER"
        }
    }

    // Fungsi logout dengan deklarasi tipe yang eksplisit agar tidak error
    val performLogout: () -> Unit = {
        coroutineScope.launch {
            tokenManager.clearToken() // Hapus token dari HP
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true } // Bersihkan seluruh riwayat halaman
            }
        }
    }

    val hideBottomBarRoutes = listOf(Screen.Login.route, Screen.Register.route)
    val showBottomBar = currentRoute != null && currentRoute !in hideBottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigatorBar(
                    currentRoute = currentRoute ?: "",
                    role = userRole,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginView(
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                    onLoginSuccess = { tokenString ->
                        try {
                            // Pengecekan saat login berhasil
                            val role = JWT(tokenString).getClaim("role").asString()?.uppercase()

                            if (role == "STAFF") {
                                navController.navigate(Screen.ManageBook.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            } else {
                                navController.navigate(Screen.Catalog.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                )
            }

            composable(Screen.Register.route) {
                RegisterView(onNavigateToLogin = { navController.navigate(Screen.Login.route) })
            }

            composable(Screen.ManageBook.route) {
                // Mengoper fungsi logout ke Navbar di halaman Kelola Buku
                ManageBookView()
            }

            composable(Screen.Catalog.route) {
                Scaffold(
                    topBar = {
                        TopNavigatorBar(
                            title = "Katalog Buku"
                        )
                    }
                ) { padding ->
                    Text(
                        "Halaman Katalog (Member)",
                        modifier = Modifier
                            .padding(padding)
                            .padding(16.dp)
                    )
                }
            }

            composable(Screen.Borrowing.route) {
                Scaffold(
                    topBar = {
                        TopNavigatorBar(
                            title = "Peminjaman",
                        )
                    }
                ) { padding ->
                    Text(
                        "Halaman Peminjaman",
                        modifier = Modifier
                            .padding(padding)
                            .padding(16.dp)
                    )
                }
            }

            composable(Screen.Account.route) {
                AccountView(
                    userRole = userRole, // Untuk membedakan tampilan profil Staf/Member
                    onLogout = performLogout // 🌟 Tombol logout sekarang ada di sini!
                )
            }
        }
    }
}