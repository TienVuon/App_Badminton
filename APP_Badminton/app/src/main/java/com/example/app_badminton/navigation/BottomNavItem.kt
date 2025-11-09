package com.example.app_badminton.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    object Home : BottomNavItem("Trang chủ", Icons.Default.Home, "home_screen")
    object Booking : BottomNavItem("Đặt sân", Icons.Default.DateRange, "booking_screen")
    object Cart : BottomNavItem("Giỏ hàng", Icons.Default.ShoppingCart, "cart_screen")
    object Profile : BottomNavItem("Hồ sơ", Icons.Default.Person, "profile_screen")

    // ✅ Thêm mục Lịch sử nếu muốn hiển thị trong thanh điều hướng
    object History : BottomNavItem("Lịch sử", Icons.Default.History, "booking_history")
    object Service : BottomNavItem("Dịch vụ", Icons.Default.Build, "service_screen")

}
