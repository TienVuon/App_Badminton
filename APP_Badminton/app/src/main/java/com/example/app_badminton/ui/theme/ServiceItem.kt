package com.example.app_badminton

import androidx.compose.foundation.Image // Import Image
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
import androidx.compose.ui.layout.ContentScale // Import ContentScale
import androidx.compose.ui.res.painterResource // Import painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.app_badminton.ui.theme.ServiceTheme // Import ServiceTheme (Giả định)
import com.example.app_badminton.FoodServiceItemCard // Import FoodServiceItemCard (Giả định)

// --- DỮ LIỆU MOCK VÀ CẤU TRÚC ---
// ✅ Cập nhật: Đảm bảo ServiceItem có imageResId và không gây lỗi Redeclaration.
// Nếu ServiceItem được định nghĩa ở một file khác, bạn PHẢI XÓA KHỐI CODE này.
data class ServiceItem(
    val name: String,
    val description: String,
    val price: Int,
    val rating: Double,
    val imageResId: Int = 0 // Thuộc tính ảnh
)

fun getOtherServiceData(): List<ServiceItem> {
    return listOf(
        // ✅ CẬP NHẬT: Thêm imageResId cho từng mục
        ServiceItem("Thuê vợt Yonex", "Vợt cơ bản, phù hợp cho người mới chơi.", 30000, 4.7, R.drawable.votyonex),
        ServiceItem("Thuê giày cầu lông", "Cung cấp giày các cỡ từ 38-43.", 50000, 4.4, R.drawable.giaycaulong),
        ServiceItem("Bán quấn cán vợt", "Quấn cán cơ bản, nhiều màu sắc.", 15000, 4.9, R.drawable.quancanvot),
        ServiceItem("Bán ống cầu lông", "Cầu lông tiêu chuẩn, 1 ống 12 quả.", 250000, 4.8, R.drawable.ongcaulong),
        ServiceItem("Y tế", "Đội ngũ y tế giàu kinh nghiệm.", 250000, 4.8, R.drawable.yte),
        ServiceItem("Dịch vụ dọn sân", "Sạch sẽ và gọn gàng.", 250000, 4.8, R.drawable.donsan)
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
                modifier = Modifier.background(ServiceTheme.HeaderGradient),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
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
            // Giả định FoodServiceItemCard đã được tạo và có khả năng hiển thị ảnh
            items(items) { item -> FoodServiceItemCard(item = item) }
            item { Spacer(modifier = Modifier.height(50.dp)) }
        }
    }
}