package com.example.bukuapa_frontend.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object ManageBook : Screen("manage_book")
    object Catalog : Screen("catalog")
    object Borrowing : Screen("borrowing")
}