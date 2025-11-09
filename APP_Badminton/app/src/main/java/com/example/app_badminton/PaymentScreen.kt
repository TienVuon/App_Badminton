package com.example.app_badminton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_badminton.data.BookingPreferences
import com.example.app_badminton.data.CartPreferences
import com.example.app_badminton.data.CartItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController) {
    val context = LocalContext.current
    val bookingPrefs = remember { BookingPreferences(context) }
    val cartPrefs = remember { CartPreferences(context) }
    val historyPrefs = remember { com.example.app_badminton.data.BookingHistoryPreferences(context) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var cartItems by remember { mutableStateOf(listOf<CartItem>()) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // ✅ Load danh sách từ SharedPreferences trong coroutine
    LaunchedEffect(Unit) {
        cartItems = cartPrefs.getCartItems()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "💳 Thanh toán",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF009688)
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF7F7F7)),
            contentAlignment = Alignment.Center
        ) {
            if (cartItems.isEmpty()) {
                Text(
                    "Không có sân nào trong giỏ hàng để thanh toán.",
                    color = Color.Gray,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    val total = cartItems.sumOf { it.price }

                    Text(
                        "Xác nhận thanh toán cho ${cartItems.size} lượt đặt sân",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        "💰 Tổng cộng: ${String.format("%,d", total)}đ",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF009688)
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = { showConfirmDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Xác nhận thanh toán", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // 🔐 Xác nhận thanh toán
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("XÁC NHẬN THANH TOÁN", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Sau khi xác nhận, các khung giờ này sẽ được đánh dấu là 'đã đặt' và không thể chỉnh sửa.",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        val cartItems = cartPrefs.getCartItems()       // suspend -> gọi trong coroutine
                        if (cartItems.isNotEmpty()) {
                            historyPrefs.appendFromCartItems(cartItems) // ✅ ghi lịch sử
                            cartPrefs.clearCart()                       // ✅ dọn giỏ
                            snackbarHostState.showSnackbar("Thanh toán thành công! Đã lưu vào Lịch sử đặt sân.")
                            // Điều hướng sang màn lịch sử để xem ngay:
                            navController.navigate("booking_history") {
                                popUpTo("payment") { inclusive = true }
                            }
                        } else {
                            snackbarHostState.showSnackbar("Giỏ hàng trống.")
                        }
                    }
                }) {
                    Text("Xác nhận thanh toán")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}
