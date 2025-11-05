package com.example.app_badminton

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_badminton.R

// --- ĐỊNH NGHĨA HẰNG SỐ CHUNG TRONG OBJECT ---
object ThemeConstants {
    val PrimaryColor = Color(0xFF4CAF50) // Xanh lá
    val AccentColor = Color(0xFFFF9800)  // Cam
    val DarkTextColor = Color(0xFF212121)
    val LightGreyBackground = Color(0xFFF7F7F7)
    val CardBackgroundColor = Color(0xFFFFFFFF)
    val PrimaryGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF66BB6A), Color(0xFF4CAF50)) // Gradient Xanh lá
    )
}

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
        containerColor = ThemeConstants.LightGreyBackground,
        topBar = { BookingTopBar(navController = navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), // Thêm padding ngoài
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- 1. Thanh Tìm kiếm Hiện đại (ĐÃ CẬP NHẬT) ---
            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Tìm kiếm sân cầu lông...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Tìm kiếm", tint = ThemeConstants.PrimaryColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp), // Bo tròn hơn (Pro style)
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ThemeConstants.PrimaryColor,
                        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                        focusedLabelColor = ThemeConstants.PrimaryColor,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        cursorColor = ThemeConstants.PrimaryColor
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // --- 2. Danh sách Sân (Court Cards) ---
            items(filteredCourts) { court ->
                BookingCourtCard(court = court, navController = navController)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// -------------------------------------------------------------
// --- COMPONENTS MỚI/CẬP NHẬT ---
// -------------------------------------------------------------

/**
 * Component TopBar (Sử dụng Gradient và font Pro).
 */
@Composable
fun BookingTopBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            // ✅ ĐỔI MỚI: Dùng Gradient cho Header
            .background(ThemeConstants.PrimaryGradient)
            .padding(vertical = 16.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Nút Quay Lại ---
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)) // Nổi bật nút trên nền Gradient
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // --- Tiêu đề (Đã căn giữa) ---
            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "LỊCH SÂN CẦU LÔNG", // Tiêu đề ngắn gọn, chuyên nghiệp hơn
                    fontWeight = FontWeight.Black, // Font siêu đậm
                    fontSize = 22.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Đặt lịch theo thời gian thực",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            // Spacer cân bằng
            Spacer(modifier = Modifier.size(40.dp))
        }
    }
}


/**
 * Component Card Sân Cầu Lông (Kiểu dáng Pro, hiện đại).
 */
@Composable
fun BookingCourtCard(court: Court, navController: NavController) {

    val statusColor = when (court.status) {
        "Còn trống" -> ThemeConstants.PrimaryColor
        "Gần đầy" -> ThemeConstants.AccentColor
        "Đã đặt" -> Color.Red
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (court.status != "Đã đặt") {
                    navController.navigate("court_booking_detail/${court.name}")
                }
            }
            .clip(RoundedCornerShape(20.dp)), // ✅ Bo góc lớn hơn
        colors = CardDefaults.cardColors(containerColor = ThemeConstants.CardBackgroundColor),
        elevation = CardDefaults.cardElevation(10.dp) // ✅ Elevation cao hơn, bóng rõ nét hơn
    ) {
        Column {
            // Phần Ảnh Sân
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = court.imageRes),
                    contentDescription = court.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    contentScale = ContentScale.Crop
                )

                // ✅ THÔNG TIN TÊN SÂN VÀ KHOẢNG CÁCH NỔI BẬT TRÊN ẢNH
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.4f)) // Overlay tối
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = court.name,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = ThemeConstants.AccentColor, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = court.distance,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ThemeConstants.AccentColor
                        )
                    }
                }
            }

            // Phần Trạng thái và CTA (Đã ĐƠN GIẢN HÓA)
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Label Trạng thái (ĐÃ CHUYỂN VỀ DƯỚI)
                Text(
                    text = court.status,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = statusColor,
                    modifier = Modifier
                        .background(statusColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                )

                // Nút Đặt Lịch (ĐÃ CẬP NHẬT)
                Button(
                    onClick = {
                        if (court.status != "Đã đặt") {
                            navController.navigate("court_booking_detail/${court.name}")
                        }
                    },
                    modifier = Modifier.height(48.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (court.status != "Đã đặt") ThemeConstants.PrimaryColor else Color.LightGray
                    ),
                    enabled = court.status != "Đã đặt",
                    shape = RoundedCornerShape(12.dp)
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
                            text = if (court.status != "Đã đặt") "Đặt Ngay" else "Hết Sân", // Ngắn gọn hơn
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