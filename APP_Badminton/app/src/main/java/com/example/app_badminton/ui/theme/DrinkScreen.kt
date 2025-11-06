package com.example.app_badminton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.app_badminton.ServiceTheme // ✅ Import ServiceTheme
import com.example.app_badminton.ServiceItem // ✅ Import ServiceItem

// --- DỮ LIỆU MOCK ---
fun getDrinksData(): List<ServiceItem> {
    return listOf(
        ServiceItem("Nước lọc Aquafina", "Bổ sung nước, cần thiết cho mọi hoạt động.", 10000, 5.0),
        ServiceItem("Nước tăng lực Sting", "Tăng cường năng lượng tức thời.", 15000, 4.3),
        ServiceItem("Trà chanh không độ", "Giải khát, giải nhiệt cơ thể.", 18000, 4.6),
        ServiceItem("Coca Cola", "Đồ uống có gas giải khát.", 15000, 4.1)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinksScreen(navController: NavController) {
    val items = getDrinksData()
    val titleText = "NƯỚC UỐNG & GIẢI KHÁT"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titleText, fontWeight = FontWeight.Bold, color = Color.White) },
                // ✅ SỬA LỖI: Dùng Modifier.background cho Brush (Gradient)
                modifier = Modifier.background(ServiceTheme.HeaderGradient),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                // ✅ Đặt containerColor là Transparent để gradient hiển thị
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(ServiceTheme.BackgroundGradient),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Giả định FoodServiceItemCard đã được định nghĩa ở một file khác
            items(items) { item -> FoodServiceItemCard(item = item) }
            item { Spacer(modifier = Modifier.height(50.dp)) }
        }
    }
}