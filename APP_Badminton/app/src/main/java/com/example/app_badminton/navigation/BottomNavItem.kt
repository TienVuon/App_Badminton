package com.example.app_badminton.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Danh sách các nút trong thanh điều hướng dưới (Bottom Navigation)
 */
sealed class BottomNavItem(
    val route: String, // <--- ĐÃ THÊM DẤU PHẨY (,)
    val title: String,
    val icon: ImageVector
) {
    val label: String?
        get() {
            TODO()
        }

    object Home : BottomNavItem(
        route = "home_screen",
        title = "Trang chủ",
        icon = Icons.Outlined.Home
    )

    object Booking : BottomNavItem(
        route = "booking_screen",
        title = "Đặt lịch",
        icon = Icons.Outlined.CalendarMonth
    )

    object Service : BottomNavItem(
        route = "service_screen",
        title = "Dịch vụ",
        icon = Icons.Filled.LocalActivity
    )

    object Cart : BottomNavItem(
        route = "cart_screen",
        title = "Giỏ hàng",
        icon = Icons.Outlined.ShoppingCart
    )

    object Profile : BottomNavItem(
        route = "profile_screen",
        title = "Tài khoản",
        icon = Icons.Outlined.Person
    )
}