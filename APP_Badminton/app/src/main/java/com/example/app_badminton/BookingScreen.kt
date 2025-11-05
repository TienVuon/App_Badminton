package com.example.app_badminton

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// KHÔNG CẦN KHAI BÁO LẠI CÁC MÀU SẮC CHUNG (PrimaryColor, AccentColor, ...)
// NẾU CHÚNG ĐÃ ĐƯỢC KHAI BÁO TRONG CÙNG PACKAGE HOẶC FILE CHUNG KHÁC.

data class Court(
    val name: String,
    val imageRes: Int,
    val distance: String,
    val status: String // Trạng thái: "Còn trống" | "Gần đầy" | "Đã đặt"
)

@Composable
fun BookingScreen(navController: NavController) {
    val allCourts = remember {
        listOf(
            Court("Sân 1 - Đại học UTH", R.drawable.caulong1, "300m", "Còn trống"),
            Court("Sân 2 - Cầu Lông Nam Kỳ", R.drawable.caulong2, "500m", "Gần đầy"),
            Court("Sân 3 - Quận 9", R.drawable.caulong3, "2.5km", "Còn trống"),
            Court("Sân 4 - Đại học UTH cs2", R.drawable.caulong4, "1.2km", "Còn trống"),
            Court("Sân 5 - Be Badminton", R.drawable.caulong5, "400m", "Gần đầy"),
            Court("Sân 6 - Way Station", R.drawable.caulong6, "800m", "Còn trống")
        )
    }

    var searchText by remember { mutableStateOf("") }

    val filteredCourts = allCourts.filter {
        it.name.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        containerColor = LightGreyBackground // SỬ DỤNG MÀU NỀN CHUNG
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- 1. Header Nổi Bật ---
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🗓️ ĐẶT SÂN NHANH 🗓️",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        color = DarkTextColor, // SỬ DỤNG MÀU CHỮ ĐẬM CHUNG
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Chọn thời gian vàng, lên sân ngay!",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- 2. Thanh Tìm kiếm Hiện đại ---
            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Tìm kiếm tên sân, khu vực...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Tìm kiếm") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor, // SỬ DỤNG MÀU CHÍNH CHUNG
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = PrimaryColor // SỬ DỤNG MÀU CHÍNH CHUNG
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // --- 3. Danh sách Sân (Court Cards) ---
            items(filteredCourts) { court ->
                BookingCourtCard(court = court, navController = navController)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// --- Component Card Sân Cầu Lông Cho Màn Hình Đặt Lịch ---
@Composable
fun BookingCourtCard(court: Court, navController: NavController) {

    // Đảm bảo các màu sắc này được lấy từ các hằng số đã định nghĩa
    val statusColor = when (court.status) {
        "Còn trống" -> PrimaryColor // Xanh lá
        "Gần đầy" -> AccentColor  // Cam
        "Đã đặt" -> Color.Red     // Đỏ
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor), // SỬ DỤNG MÀU NỀN CARD CHUNG
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column {
            // Phần Ảnh Sân
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = court.imageRes),
                    contentDescription = court.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Label Trạng thái (Góc trên phải)
                Text(
                    text = court.status,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor) // Màu Trạng thái linh hoạt
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            // Phần Thông tin và CTA
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Tên Sân
                Text(
                    text = court.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = DarkTextColor, // SỬ DỤNG MÀU CHỮ ĐẬM CHUNG
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Khoảng cách
                Text(
                    text = "Cách bạn: ${court.distance}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nút Đặt Lịch
                Button(
                    onClick = {
                        if (court.status != "Đã đặt") {
                            navController.navigate("court_booking_detail/${court.name}")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (court.status != "Đã đặt") PrimaryColor else Color.LightGray // SỬ DỤNG MÀU CHÍNH CHUNG
                    ),
                    enabled = court.status != "Đã đặt",
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = "Lịch",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (court.status != "Đã đặt") "CHỌN LỊCH VÀ ĐẶT NGAY" else "ĐÃ ĐẶT HẾT",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
