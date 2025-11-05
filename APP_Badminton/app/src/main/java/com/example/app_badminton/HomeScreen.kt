// (Nội dung của HomeScreen.kt không cần sửa thêm, đã chính xác ở lần trước)
package com.example.app_badminton

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
// Import R nếu cần
// import com.example.app_badminton.R


// --- Định nghĩa màu sắc và gradient ---
val PrimaryColor = Color(0xFF4CAF50)
val AccentColor = Color(0xFFFF9800)
val SuperAccentColor = Color(0xFFF44336)
val DarkTextColor = Color(0xFF212121)
val LightGreyBackground = Color(0xFFF7F7F7)
val CardBackgroundColor = Color(0xFFFFFFFF)
val ActiveTagColor = Color(0xFF1976D2)

@Composable
fun HomeScreen(navController: NavController) {
    // Giả định R.drawable.caulongX đã được import
    val courtList = listOf(
        Triple(R.drawable.caulong1, "Sân Trong Nhà Đẳng Cấp A1", "300m - 4.8 ⭐"),
        Triple(R.drawable.caulong2, "Sân View Cực Chill Thường", "500m - 4.5 ⭐"),
        Triple(R.drawable.caulong3, "Sân Đôi Cao Cấp VIP 99", "200m - 4.9 ⭐"),
        Triple(R.drawable.caulong4, "Sân Phổ Biến Nhất Tuần", "1km - 4.7 ⭐"),
        Triple(R.drawable.caulong5, "Khu Vực Giải Lao Đầy Đủ", "150m - 4.6 ⭐"),
        Triple(R.drawable.caulong6, "Sân Tập Luyện Chuyên Nghiệp", "800m - 4.4 ⭐")
    )
    val categories = listOf("Gần Nhất", "Được Đánh Giá Cao", "Giá Tốt", "Sân Trong Nhà", "24/7")

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LightGreyBackground),
            contentPadding = PaddingValues(top = 0.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { QuickBookingHeader(navController = navController) }
            item { Spacer(modifier = Modifier.height(0.dp)) }

            item {
                Text(
                    text = "Khám phá sân cầu lông", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkTextColor,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(category = category, isActive = category == "Gần Nhất", navController = navController)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "🔥 Sân Nổi Bật Hôm Nay", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = SuperAccentColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(text = "Xem tất cả", color = PrimaryColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.clickable { /* Handle See All */ })
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(courtList) { (imgId, name, details) ->
                HorizontalCourtCard(imgId = imgId, name = name, details = details, navController = navController)
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

// ----------------------------------------------------------------------
// --- CÁC COMPONENT ---
// ----------------------------------------------------------------------

@Composable
fun QuickBookingHeader(navController: NavController) {
    Box( modifier = Modifier.fillMaxWidth().padding(bottom = 0.dp) ) {
        Column( modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)).background(PrimaryColor) ) {}

        Column( modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp) ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                // ICON DANH MỤC (MENU)
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)).clickable { navController.navigate("menu_drawer") },
                    contentAlignment = Alignment.Center
                ) { Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = Color.White, modifier = Modifier.size(24.dp)) }

                // TÊN APP
                Text(text = "BADMINTON UTH", fontWeight = FontWeight.Black, color = Color.White, fontSize = 26.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))

                // ICON PROFILE (ĐÃ SỬA ROUTE)
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f))
                        .clickable { navController.navigate("profile_screen") },
                    contentAlignment = Alignment.Center
                ) { Icon(imageVector = Icons.Default.Person, contentDescription = "Profile", tint = Color.White, modifier = Modifier.size(24.dp)) }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column( modifier = Modifier.fillMaxWidth().background(CardBackgroundColor).padding(vertical = 16.dp, horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally ) {
                    // Nút CTA Lớn (Đã sửa route)
                    Button(
                        onClick = { navController.navigate("booking_screen") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), shape = RoundedCornerShape(12.dp), contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Brush.horizontalGradient(colors = listOf(AccentColor, Color(0xFFFFCC80))), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                                Icon(imageVector = Icons.Filled.FlashOn, contentDescription = "Đặt lịch nhanh", tint = Color.White, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("ĐẶT SÂN NGAY - CHƠI LIỀN TAY", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, maxLines = 1, overflow = TextOverflow.Clip)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Tìm kiếm sân tốt nhất, ngay lập tức!", color = DarkTextColor.copy(alpha = 0.6f), fontSize = 14.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
fun CategoryChip(category: String, isActive: Boolean, navController: NavController) {
    val backgroundColor = if (isActive) ActiveTagColor else Color.White
    val contentColor = if (isActive) Color.White else DarkTextColor

    Card(
        modifier = Modifier.clickable { navController.navigate("booking_screen") },
        shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = backgroundColor), elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Text(text = category, color = contentColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
    }
}

@Composable
fun HorizontalCourtCard(imgId: Int, name: String, details: String, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth().height(120.dp).padding(horizontal = 16.dp).clip(RoundedCornerShape(12.dp))
            .clickable { navController.navigate("court_booking_detail/$name") },
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor), elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = imgId), contentDescription = name, modifier = Modifier.width(120.dp).fillMaxHeight(), contentScale = ContentScale.Crop)

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)) {
                Text(text = name, color = DarkTextColor, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "Khoảng cách", tint = PrimaryColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = details.substringBefore(" - "), color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(imageVector = Icons.Filled.Star, contentDescription = "Đánh giá", tint = AccentColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = details.substringAfter(" - "), color = AccentColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(PrimaryColor.copy(alpha = 0.1f)).border(1.dp, PrimaryColor, RoundedCornerShape(8.dp))
                        .clickable { navController.navigate("booking_screen") }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) { Text("Đặt lịch ngay", color = PrimaryColor, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            }
        }
    }
}