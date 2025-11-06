package com.example.app_badminton

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import java.util.Calendar
import java.util.Date
import java.util.Locale

// --- ĐỊNH NGHĨA MÀU SẮC ---
object ThemeColors {
    val PrimaryGreen = Color(0xFF4CAF50)
    val SelectedTimeColor = Color(0xFF1976D2)
    val BookedColor = Color(0xFFE0E0E0)
    val FreeTimeColor = Color.White
    val BorderColor = Color(0xFFCCCCCC)
    val DarkTextColor = Color(0xFF212121)
    val SuperAccentColor = Color(0xFFF44336)
}

@Composable
fun CourtBookingDetailScreen(
    navController: NavController,
    courtName: String
) {
    val context = LocalContext.current
    val bookingPrefs = remember { BookingPreferences(context) }
    val cartPrefs = remember { CartPreferences(context) }
    val scope = rememberCoroutineScope()

    // ✅ THAY ĐỔI: currentViewingDate là ngày đang được hiển thị trong lưới giờ
    var currentViewingDate by remember { mutableStateOf(getTodayDateFormatted()) }

    // ✅ THAY ĐỔI: selectedBookings lưu trữ các lựa chọn (Map: Ngày -> List<Giờ đã chọn>)
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

    // ✅ LaunchedEffect load các slot ĐÃ ĐƯỢC ĐẶT cho ngày đang xem
    LaunchedEffect(currentViewingDate) {
        bookedSlots = bookingPrefs.getBookedSlots(courtName, currentViewingDate)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(16.dp)
    ) {
        // --- 1. Header (Tên Sân) ---
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

        // --- 2. Date Selector (Thanh Cuộn Ngang) ---
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
                // DateChip giờ đây chỉ chuyển đổi ngày đang xem,
                // nhưng cũng hiển thị trạng thái đã chọn
                DateChip(
                    date = date,
                    isSelected = date.formatted == currentViewingDate,
                    hasSelectedSlots = selectedBookings.containsKey(date.formatted) && selectedBookings[date.formatted]!!.isNotEmpty(),
                    onDateSelected = { currentViewingDate = it.formatted }
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // --- 3. Time Slot Grid ---
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
                // ✅ Kiểm tra slot có được chọn cho ngày đang xem không
                val isSelected = selectedBookings[currentViewingDate]?.contains(slot) ?: false

                TimeSlotChip(
                    slot = slot,
                    isBooked = isBooked,
                    isSelected = isSelected,
                    onClick = {
                        if (!isBooked) {
                            val currentSlots = selectedBookings[currentViewingDate] ?: emptyList()
                            val newSlots = if (isSelected) {
                                currentSlots - slot // Bỏ chọn
                            } else {
                                currentSlots + slot // Chọn thêm
                            }

                            // ✅ Cập nhật map selectedBookings (sử dụng immutable update)
                            selectedBookings = if (newSlots.isEmpty()) {
                                // Xóa ngày khỏi map nếu không còn slot nào được chọn
                                selectedBookings.toMutableMap().apply { remove(currentViewingDate) }.toMap()
                            } else {
                                // Cập nhật/thêm danh sách giờ cho ngày đó
                                selectedBookings.toMutableMap().apply { this[currentViewingDate] = newSlots.sorted() }.toMap()
                            }
                        }
                    }
                )
            }
        }

        // Tính tổng tiền và tổng giờ
        val totalHours = selectedBookings.values.sumOf { it.size }
        val totalCost = totalHours * 100000

        // --- 4. Booking Button ---
        Button(
            onClick = { showConfirmDialog = true },
            enabled = selectedBookings.isNotEmpty(), // ✅ Kích hoạt khi có bất kỳ lựa chọn nào trong map
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = ThemeColors.SelectedTimeColor)
        ) {
            Text(
                if (totalHours > 0) "Đặt ${totalHours} giờ ngay (${String.format("%,dđ", totalCost)}đ)"
                else "Chọn khung giờ để đặt",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    // Hộp thoại xác nhận & thanh toán
    if (showConfirmDialog) {
        val totalCost = selectedBookings.values.sumOf { it.size * 100000 } // ✅ Tính đúng tổng tiền
        BookingConfirmDialog(
            courtName = courtName,
            selectedBookings = selectedBookings, // ✅ TRUYỀN TOÀN BỘ MAP
            totalCost = totalCost,
            onConfirm = {
                scope.launch {
                    // ✅ LƯU TẤT CẢ BOOKING TỪ MAP
                    selectedBookings.forEach { (date, times) ->
                        times.forEach { timeSlot ->
                            bookingPrefs.saveBooking(courtName, date, timeSlot)
                            cartPrefs.addToCart(courtName, date, timeSlot, 100000)
                        }
                    }
                }
                showConfirmDialog = false
                navController.navigate("payment")
            },
            onDismiss = { showConfirmDialog = false }
        )
    }
}

// -------------------------------------------------------------
// --- FUNCTIONS VÀ COMPONENTS KHÁC ---
// -------------------------------------------------------------

data class DateItem(val displayDay: String, val displayDate: String, val formatted: String)

fun getNextSevenDays(): List<DateItem> {
    val days = mutableListOf<DateItem>()
    val calendar = Calendar.getInstance()
    val sdfDay = SimpleDateFormat("EEE", Locale("vi", "VN"))
    val sdfDate = SimpleDateFormat("dd/MM", Locale.getDefault())
    val sdfFormatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    for (i in 0 until 7) {
        val date = calendar.time

        val displayDay = when (i) {
            0 -> "Hôm nay"
            else -> sdfDay.format(date).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }

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

@Composable
fun DateChip(date: DateItem, isSelected: Boolean, hasSelectedSlots: Boolean, onDateSelected: (DateItem) -> Unit) {
    val backgroundColor = when {
        isSelected -> ThemeColors.SelectedTimeColor // Ngày đang xem
        hasSelectedSlots -> ThemeColors.PrimaryGreen // Ngày đã chọn slot nhưng không phải ngày đang xem
        else -> Color.White
    }
    val contentColor = if (isSelected || hasSelectedSlots) Color.White else ThemeColors.DarkTextColor
    val borderColor = if (isSelected) ThemeColors.SelectedTimeColor else if (hasSelectedSlots) ThemeColors.PrimaryGreen else ThemeColors.BorderColor

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
        Text(
            text = date.displayDay,
            color = contentColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = date.displayDate,
            color = contentColor.copy(alpha = if (isSelected || hasSelectedSlots) 1f else 0.7f),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun TimeSlotChip(slot: String, isBooked: Boolean, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = when {
        isBooked -> ThemeColors.BookedColor
        isSelected -> ThemeColors.SelectedTimeColor
        else -> ThemeColors.FreeTimeColor
    }
    val contentColor = if (isBooked) Color.Gray else if (isSelected) Color.White else ThemeColors.DarkTextColor
    val borderColor = if (isSelected) ThemeColors.SelectedTimeColor else ThemeColors.BorderColor
    val enabled = !isBooked

    Box(
        modifier = Modifier
            .height(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            slot,
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun BookingConfirmDialog(
    courtName: String,
    selectedBookings: Map<String, List<String>>, // ✅ THAY ĐỔI: Nhận Map
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

                // ✅ HIỂN THỊ CHI TIẾT CỦA TẤT CẢ CÁC NGÀY ĐÃ CHỌN
                Text("🗓️ Chi tiết:", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))

                // Sắp xếp theo ngày để hiển thị dễ đọc
                selectedBookings.keys.sorted().forEach { date ->
                    val times = selectedBookings[date]?.sorted()?.joinToString(", ") ?: ""
                    Column(Modifier.padding(start = 8.dp)) {
                        Text("• Ngày $date:", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text("   Giờ: $times", fontSize = 15.sp, color = ThemeColors.DarkTextColor.copy(alpha = 0.8f))
                    }
                    Spacer(Modifier.height(4.dp))
                }

                Spacer(Modifier.height(8.dp))

                Text("🕒 Tổng số giờ: $totalHours giờ", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

                Text("💵 Tổng tiền: ${String.format("%,dđ", totalCost)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ThemeColors.SuperAccentColor
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = ThemeColors.PrimaryGreen)
            ) {
                Text("Thanh toán")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
