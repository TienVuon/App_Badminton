package com.example.app_badminton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_badminton.data.BookingHistoryItem
import com.example.app_badminton.data.BookingHistoryPreferences
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { BookingHistoryPreferences(context) }
    val scope = rememberCoroutineScope()
    var items by remember { mutableStateOf<List<BookingHistoryItem>>(emptyList()) }

    // Load dữ liệu an toàn bằng coroutine
    LaunchedEffect(Unit) {
        items = prefs.getAll()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lịch sử đặt sân", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF1FDFB)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF9F9F9))
                .padding(16.dp)
        ) {

            if (items.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Chưa có lịch sử đặt sân.",
                        color = Color.Gray,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
                return@Column
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items) { item ->
                    HistoryCard(
                        item = item,
                        onRebook = {
                            // Có thể điều hướng tới màn đặt sân cụ thể:
                            // navController.navigate("court_detail/${item.court}")
                            navController.navigate("booking") // tạm điều hướng về màn đặt sân chung
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryCard(item: BookingHistoryItem, onRebook: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("🏸 ${item.court}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(4.dp))
            Text("📅 Ngày: ${item.date}", color = Color.Gray)
            Text("⏰ Giờ: ${item.time}", color = Color.Gray)
            Spacer(Modifier.height(6.dp))
            Text("💵 ${String.format("%,dđ", item.price)}", color = Color(0xFF009688), fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            val sdf = remember { SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()) }
            Text("Trạng thái: ${item.status} • Thanh toán lúc: ${sdf.format(Date(item.paidAt))}",
                color = Color(0xFF616161), fontSize = 13.sp)

            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = onRebook,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Đặt lại sân này") }
        }
    }
}
