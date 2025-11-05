package com.example.app_badminton.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Booking,
        BottomNavItem.Cart,
        BottomNavItem.Profile
        // Lưu ý: Item Service thường không đặt ở Bottom Bar mà dùng FAB, nhưng nếu cần:
        // BottomNavItem.Service
    )


    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // SỬA: Dùng startDestination ID để popUpTo (hoặc route "login_screen")
                        // Điều này đảm bảo PopUpTo tìm thấy route khởi đầu hợp lệ
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }

                        // Tránh tạo nhiều bản sao của màn hình
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title, fontSize = MaterialTheme.typography.labelSmall.fontSize) }
            )
        }
    }
}