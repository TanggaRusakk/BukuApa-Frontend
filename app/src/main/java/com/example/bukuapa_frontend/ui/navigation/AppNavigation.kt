package com.example.bukuapa_frontend.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.auth0.android.jwt.JWT
import com.example.bukuapa_frontend.data.models.Book
import com.example.bukuapa_frontend.ui.views.auth.AccountView
import com.example.bukuapa_frontend.ui.views.auth.LoginView
import com.example.bukuapa_frontend.ui.views.auth.RegisterView
import com.example.bukuapa_frontend.ui.views.book.CatalogView
import com.example.bukuapa_frontend.ui.views.book.CreateUpdateBookView
import com.example.bukuapa_frontend.ui.views.book.ManageBookView
import com.example.bukuapa_frontend.ui.views.borrowing.BorrowingView
import com.example.bukuapa_frontend.ui.views.borrowing.CreateLoanView
import com.example.bukuapa_frontend.ui.views.components.BottomNavigatorBar
import com.example.bukuapa_frontend.utils.TokenManager
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val tokenManager = remember { TokenManager(context) }

    val token by tokenManager.getToken().collectAsState(initial = null)

    val userRole = remember(token) {
        if (token != null) {
            try {
                JWT(token!!).getClaim("role").asString()?.uppercase() ?: "MEMBER"
            } catch (e: Exception) {
                "MEMBER"
            }
        } else {
            "MEMBER"
        }
    }

    val performLogout: () -> Unit = {
        coroutineScope.launch {
            tokenManager.clearToken()
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    var selectedBook by remember { mutableStateOf<Book?>(null) }

    val hideBottomBarRoutes = listOf(
        Screen.Login.route,
        Screen.Register.route,
        Screen.CreateUpdateBook.route,
        Screen.CreateLoan.route
    )
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
                ManageBookView(
                    onNavigateToCreateUpdate = { book ->
                        selectedBook = book
                        navController.navigate(Screen.CreateUpdateBook.route)
                    }
                )
            }

            composable(Screen.CreateUpdateBook.route) {
                CreateUpdateBookView(
                    book = selectedBook,
                    onBackClick = {
                        selectedBook = null
                        navController.popBackStack()
                    },
                    onSaveSuccess = {
                        selectedBook = null
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Catalog.route) {
                CatalogView(
                    role = userRole,
                    onNavigate = { route ->
                        navController.navigate(route)
                    }
                )
            }

            composable(Screen.Borrowing.route) {
                BorrowingView(
                    role = userRole,
                    onNavigateToCreate = {
                        navController.navigate(Screen.CreateLoan.route)
                    }
                )
            }

            composable(Screen.CreateLoan.route) {
                CreateLoanView(
                    onBackClick = { navController.popBackStack() },
                    onSaveSuccess = { navController.popBackStack() }
                )
            }

            composable(Screen.Account.route) {
                AccountView(
                    userRole = userRole,
                    onLogout = performLogout
                )
            }
        }
    }
}
