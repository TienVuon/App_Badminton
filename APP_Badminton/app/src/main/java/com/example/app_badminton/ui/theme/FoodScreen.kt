package com.example.app_badminton

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_badminton.ui.theme.ServiceTheme // Import đúng cách

// --- DỮ LIỆU MOCK VÀ CẤU TRÚC ---
fun getFoodData(): List<ServiceItem> {
    return listOf(
        ServiceItem("Mì gói bò trứng", "Nhanh chóng, bổ sung năng lượng tức thì.", 25000, 4.5),
        ServiceItem("Bánh mì chà bông", "Ăn nhẹ trước hoặc sau khi chơi.", 15000, 4.2),
        ServiceItem("Snack khoai tây", "Đồ ăn vặt yêu thích của dân thể thao.", 10000, 4.8)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodScreen(navController: NavController) {
    val items = getFoodData()
    val titleText = "THỰC PHẨM & ĂN NHẸ"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titleText, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(ServiceTheme.BackgroundGradient), // Sử dụng ServiceTheme đã được import
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item -> FoodServiceItemCard(item = item) }
            item { Spacer(modifier = Modifier.height(50.dp)) }
        }
    }
}

// Component Card riêng cho FoodScreen
@Composable
fun FoodServiceItemCard(item: ServiceItem) {
    Card(
        modifier = Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(16.dp)).clickable { /* Mở chi tiết */ },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.width(100.dp).fillMaxHeight().clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)).background(ServiceTheme.AccentGreen.copy(alpha = 0.5f))) {}
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f).padding(vertical = 8.dp)) {
                Text(text = item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ServiceTheme.DarkText, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = item.description, fontSize = 12.sp, color = Color.Gray, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                    Text(text = item.rating.toString(), color = Color(0xFFFFC107), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Column(modifier = Modifier.padding(end = 12.dp), horizontalAlignment = Alignment.End) {
                Text(text = "${item.price}đ", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = ServiceTheme.PrimaryBlue)
                Button(onClick = { /* TODO: Thêm vào giỏ hàng */ }, modifier = Modifier.height(36.dp).clip(RoundedCornerShape(8.dp)), contentPadding = PaddingValues(horizontal = 8.dp), colors = ButtonDefaults.buttonColors(containerColor = ServiceTheme.PrimaryBlue)) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = "Thêm", modifier = Modifier.size(16.dp), tint = Color.White)
                }
            }
        }
    }
}
