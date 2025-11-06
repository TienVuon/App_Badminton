package com.example.app_badminton.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Booking,
        BottomNavItem.Service, // ✅ Item Dịch vụ
        BottomNavItem.Cart,
        BottomNavItem.Profile
    )

    val SelectedColor = Color(0xFF4CAF50) // Primary Green
    val UnselectedColor = Color.Gray

    // ✅ THÊM DANH SÁCH ROUTES CỦA NHÓM DỊCH VỤ
    val serviceRoutes = listOf("service_screen", "food_screen", "drinks_screen", "other_service_screen")

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            // 🔄 LOGIC CHỌN MỤC ĐÃ ĐƯỢC CẬP NHẬT ĐỂ BAO GỒM CÁC TRANG CON
            val selected = when (item.route) {
                "service_screen" -> currentRoute in serviceRoutes
                else -> currentRoute == item.route
            }

            NavigationBarItem(
                selected = selected, // ✅ SỬ DỤNG LOGIC CHỌN MỚI
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {

                            // 🚀 Giữ nguyên logic pop-up mà bạn đã cung cấp
                            popUpTo(BottomNavItem.Home.route) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = if (selected) SelectedColor else UnselectedColor
                    )
                },
                label = {
                    Text(
                        item.label,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        color = if (selected) SelectedColor else UnselectedColor
                    )
                }
            )
        }
    }
}