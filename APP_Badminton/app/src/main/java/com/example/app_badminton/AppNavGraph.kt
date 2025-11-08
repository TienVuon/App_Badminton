package com.example.app_badminton

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app_badminton.navigation.BottomNavigationBar

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // Chỉ hiển thị thanh công cụ nếu không phải Login hoặc Register
            if (currentRoute !in listOf("login_screen", "register_screen")) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "login_screen", // Bắt đầu từ Login
            modifier = Modifier.padding(innerPadding)
        ) {
            // Màn hình không có Bottom Nav
            composable("login_screen") { LoginScreen(navController = navController) }
            composable("register_screen") { RegisterScreen(navController = navController) }

            composable("forgot_password") { ForgotPasswordScreen(navController = navController) }
            // Màn hình có Bottom Nav
            composable("home_screen") { HomeScreen(navController = navController) }
            composable("cart_screen") { CartScreen(navController = navController) }
            composable("profile_screen") { ProfileScreen(navController = navController) }
            composable("booking_screen") { BookingScreen(navController = navController) }

            // Gọi Composable Service Screen chính
            composable("service_screen") { ServiceScreen(navController = navController) }

            // Routes dịch vụ con
            composable("food_screen") { FoodScreen(navController = navController) }
            composable("drinks_screen") { DrinksScreen(navController = navController) }
            composable("other_service_screen") { OtherServiceScreen(navController = navController) }

            // Chuyển động chi tiết (cần các Composable tương ứng)
            composable(
                route = "court_booking_detail/{courtName}",
                arguments = listOf(navArgument("courtName") { type = NavType.StringType })
            ) { backStackEntry ->
                val courtName = backStackEntry.arguments?.getString("courtName") ?: ""
                CourtBookingDetailScreen(navController = navController, courtName = courtName)
            }

            composable("payment_screen") {
                PaymentScreen(navController = navController)
            }
        }
    }
}

