package com.example.app_badminton

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_badminton.data.BookingPreferences
import com.example.app_badminton.data.CartPreferences
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/* -------------------------------
   🎨 MÀU CHỦ ĐỀ
--------------------------------*/
object ThemeColors {
    val PrimaryGreen = Color(0xFF4CAF50)
    val SelectedTimeColor = Color(0xFF1976D2)
    val BookedColor = Color(0xFFE0E0E0)
    val FreeTimeColor = Color.White
    val BorderColor = Color(0xFFCCCCCC)
    val DarkTextColor = Color(0xFF212121)
    val SuperAccentColor = Color(0xFFF44336)
}

/* -------------------------------
   🏸 MÀN HÌNH CHI TIẾT ĐẶT SÂN
--------------------------------*/
@Composable
fun CourtBookingDetailScreen(
    navController: NavController,
    courtName: String
) {
    val context = LocalContext.current
    val bookingPrefs = remember { BookingPreferences(context) }
    val cartPrefs = remember { CartPreferences(context) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var currentViewingDate by remember { mutableStateOf(getTodayDateFormatted()) }
    var selectedBookings by remember { mutableStateOf(mapOf<String, List<String>>()) }
    var bookedSlots by remember { mutableStateOf(listOf<String>()) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val nextSevenDays = remember { getNextSevenDays() }
    val timeSlots = listOf(
        "06:00-07:00", "07:00-08:00", "08:00-09:00",
        "09:00-10:00", "10:00-11:00", "11:00-12:00",
        "13:00-14:00", "14:00-15:00", "15:00-16:00",
        "16:00-17:00", "17:00-18:00", "18:00-19:00",
        "19:00-20:00", "20:00-21:00", "21:00-22:00"
    )

    // Load danh sách giờ đã được đặt (đã thanh toán)
    LaunchedEffect(currentViewingDate) {
        bookedSlots = bookingPrefs.getBookedSlots(courtName, currentViewingDate)
    }

    /* --------------------------
       📅 UI Chính
    -------------------------- */
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(16.dp)
        ) {
            Text(
                "Đặt sân: $courtName",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ThemeColors.PrimaryGreen,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                "Chọn ngày và (các) khung giờ tập luyện",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // --- Chọn ngày ---
            Text(
                "📅 Chọn Ngày",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ThemeColors.DarkTextColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(nextSevenDays) { date ->
                    DateChip(
                        date = date,
                        isSelected = date.formatted == currentViewingDate,
                        hasSelectedSlots = selectedBookings.containsKey(date.formatted)
                                && selectedBookings[date.formatted]!!.isNotEmpty(),
                        onDateSelected = { currentViewingDate = it.formatted }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // --- Chọn giờ ---
            Text(
                "⏰ Khung Giờ (Ngày ${currentViewingDate})",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ThemeColors.DarkTextColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(timeSlots) { slot ->
                    val isBooked = bookedSlots.contains(slot)
                    val isSelected = selectedBookings[currentViewingDate]?.contains(slot) ?: false

                    TimeSlotChip(
                        slot = slot,
                        isBooked = isBooked,
                        isSelected = isSelected
                    ) {
                        if (!isBooked) {
                            val currentSlots = selectedBookings[currentViewingDate] ?: emptyList()
                            val newSlots =
                                if (isSelected) currentSlots - slot else currentSlots + slot

                            selectedBookings = if (newSlots.isEmpty()) {
                                selectedBookings.toMutableMap().apply { remove(currentViewingDate) }
                                    .toMap()
                            } else {
                                selectedBookings.toMutableMap().apply {
                                    this[currentViewingDate] = newSlots.sorted()
                                }.toMap()
                            }
                        }
                    }
                }
            }

            val totalHours = selectedBookings.values.sumOf { it.size }
            val totalCost = totalHours * 100000

            Button(
                onClick = { showConfirmDialog = true },
                enabled = selectedBookings.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = ThemeColors.SelectedTimeColor)
            ) {
                Text(
                    if (totalHours > 0) "Đặt $totalHours giờ (${String.format("%,dđ", totalCost)}đ)"
                    else "Chọn khung giờ để đặt",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Vị trí hiển thị Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        )
    }

    /* --------------------------
       🧾 Hộp thoại xác nhận
    -------------------------- */
    if (showConfirmDialog) {
        val totalCost = selectedBookings.values.sumOf { it.size * 100000 }

        BookingConfirmDialog(
            courtName = courtName,
            selectedBookings = selectedBookings,
            totalCost = totalCost,
            onConfirm = {
                scope.launch {
                    selectedBookings.forEach { (date, times) ->
                        times.forEach { timeSlot ->
                            cartPrefs.addToCart(courtName, date, timeSlot, 100000)
                        }
                    }
                    showConfirmDialog = false
                    selectedBookings = emptyMap()

                    // 🎉 Hiển thị thông báo SnackBar thành công
                    snackbarHostState.showSnackbar("✅ Đã thêm vào giỏ hàng thành công!")

                    // Cập nhật lại danh sách giờ đã đặt (chỉ những cái đã thanh toán)
                    bookedSlots = bookingPrefs.getBookedSlots(courtName, currentViewingDate)
                }
            },
            onDismiss = { showConfirmDialog = false }
        )
    }
}

/* -------------------------------
   🔹 Các hàm phụ trợ
--------------------------------*/
data class DateItem(val displayDay: String, val displayDate: String, val formatted: String)

fun getNextSevenDays(): List<DateItem> {
    val days = mutableListOf<DateItem>()
    val calendar = Calendar.getInstance()
    val sdfDay = SimpleDateFormat("EEE", Locale("vi", "VN"))
    val sdfDate = SimpleDateFormat("dd/MM", Locale.getDefault())
    val sdfFormatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    repeat(7) { i ->
        val date = calendar.time
        val displayDay = if (i == 0) "Hôm nay"
        else sdfDay.format(date).replaceFirstChar { it.titlecase(Locale.getDefault()) }

        days.add(
            DateItem(
                displayDay = displayDay,
                displayDate = sdfDate.format(date),
                formatted = sdfFormatted.format(date)
            )
        )
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    return days
}

fun getTodayDateFormatted(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date())
}

/* -------------------------------
   🔹 UI: Chọn ngày
--------------------------------*/
@Composable
fun DateChip(
    date: DateItem,
    isSelected: Boolean,
    hasSelectedSlots: Boolean,
    onDateSelected: (DateItem) -> Unit
) {
    val backgroundColor = when {
        isSelected -> ThemeColors.SelectedTimeColor
        hasSelectedSlots -> ThemeColors.PrimaryGreen
        else -> Color.White
    }
    val contentColor = if (isSelected || hasSelectedSlots) Color.White else ThemeColors.DarkTextColor
    val borderColor = if (isSelected) ThemeColors.SelectedTimeColor
    else if (hasSelectedSlots) ThemeColors.PrimaryGreen else ThemeColors.BorderColor

    Column(
        modifier = Modifier
            .width(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onDateSelected(date) }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(date.displayDay, color = contentColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(date.displayDate, color = contentColor.copy(alpha = 0.9f), fontSize = 14.sp)
    }
}

/* -------------------------------
   🔹 UI: Chọn khung giờ
--------------------------------*/
@Composable
fun TimeSlotChip(slot: String, isBooked: Boolean, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = when {
        isBooked -> ThemeColors.BookedColor
        isSelected -> ThemeColors.SelectedTimeColor
        else -> ThemeColors.FreeTimeColor
    }
    val contentColor = if (isBooked) Color.Gray else if (isSelected) Color.White else ThemeColors.DarkTextColor
    val borderColor = if (isSelected) ThemeColors.SelectedTimeColor else ThemeColors.BorderColor

    Box(
        modifier = Modifier
            .height(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(enabled = !isBooked, onClick = onClick)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(slot, color = contentColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}

/* -------------------------------
   🔹 UI: Hộp thoại xác nhận
--------------------------------*/
@Composable
fun BookingConfirmDialog(
    courtName: String,
    selectedBookings: Map<String, List<String>>,
    totalCost: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val totalHours = selectedBookings.values.sumOf { it.size }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("XÁC NHẬN ĐẶT SÂN", fontWeight = FontWeight.Bold) },
        text = {
            Column(Modifier.padding(top = 8.dp)) {
                Text("🏸 Sân: $courtName", fontSize = 16.sp)
                Text("🗓️ Chi tiết:", fontSize = 16.sp, fontWeight = FontWeight.Bold)

                selectedBookings.keys.sorted().forEach { date ->
                    val times = selectedBookings[date]?.sorted()?.joinToString(", ") ?: ""
                    Column(Modifier.padding(start = 8.dp, top = 4.dp)) {
                        Text("• Ngày $date", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text("   Giờ: $times", fontSize = 15.sp, color = ThemeColors.DarkTextColor)
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text("🕒 Tổng số giờ: $totalHours giờ", fontSize = 16.sp)
                Text(
                    "💵 Tổng tiền: ${String.format("%,dđ", totalCost)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ThemeColors.SuperAccentColor
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = ThemeColors.PrimaryGreen)) {
                Text("Xác nhận")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
