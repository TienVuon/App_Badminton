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

// --- DỮ LIỆU MOCK VÀ CẤU TRÚC ---
// Nếu ServiceItem được định nghĩa ở một file khác, xóa nó ở đây và import.
data class ServiceItem(val name: String, val description: String, val price: Int, val rating: Double, val imageResId: Int = 0)
fun getOtherServiceData(): List<ServiceItem> {
    return listOf(
        ServiceItem("Thuê vợt Yonex", "Vợt cơ bản, phù hợp cho người mới chơi.", 30000, 4.7),
        ServiceItem("Thuê giày cầu lông", "Cung cấp giày các cỡ từ 38-43.", 50000, 4.4),
        ServiceItem("Bán quấn cán vợt", "Quấn cán cơ bản, nhiều màu sắc.", 15000, 4.9),
        ServiceItem("Bán ống cầu lông", "Cầu lông tiêu chuẩn, 1 ống 12 quả.", 250000, 4.8)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherServiceScreen(navController: NavController) {
    val items = getOtherServiceData()
    val titleText = "DỊCH VỤ & PHỤ KIỆN KHÁC"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titleText, fontWeight = FontWeight.Bold, color = Color.White) },
                // ✅ SỬ DỤNG MODIFIER BACKGROUND ĐỂ ÁP DỤNG GRADIENT
                modifier = Modifier.background(ServiceTheme.HeaderGradient),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                // ✅ ĐẶT CONTAINER COLOR LÀ TRONG SUỐT ĐỂ GRADIENT HIỂN THỊ
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
            // Giả định FoodServiceItemCard đã được tạo
            items(items) { item -> FoodServiceItemCard(item = item) }
            item { Spacer(modifier = Modifier.height(50.dp)) }
        }
    }
}
// ⚠️ Cần định nghĩa FoodServiceItemCard ở đây hoặc trong file FoodScreen.kt (và đảm bảo nó là public)
// (Giả định bạn đã định nghĩa nó ở đâu đó và nó là public)