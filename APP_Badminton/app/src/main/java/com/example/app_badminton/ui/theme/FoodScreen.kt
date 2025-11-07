package com.example.app_badminton

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_badminton.ui.theme.ServiceTheme

// --- DỮ LIỆU MOCK THÊM HÌNH ---
// ✅ Thêm drawableResId (ID hình trong drawable)
fun getFoodData(): List<ServiceItem> {
    return listOf(
        ServiceItem("Mì gói bò trứng", "Nhanh chóng, bổ sung năng lượng tức thì.", 25000, 4.5, drawableResId = R.drawable.migoi),
        ServiceItem("Bánh mì chà bông", "Ăn nhẹ trước hoặc sau khi chơi.", 15000, 4.2, drawableResId = R.drawable.banhmichabong),
        ServiceItem("Snack khoai tây", "Đồ ăn vặt yêu thích của dân thể thao.", 10000, 4.8, drawableResId = R.drawable.snackhoaitay)
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
                title = {
                    Text(titleText, fontWeight = FontWeight.Bold, color = Color.White)
                },
                modifier = Modifier.background(ServiceTheme.HeaderGradient),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
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
