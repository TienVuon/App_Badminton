package com.example.app_badminton.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

val Context.bookingDataStore by preferencesDataStore(name = "booking_prefs")

class BookingPreferences(private val context: Context) {

    companion object {
        private val BOOKINGS_KEY = stringPreferencesKey("booked_slots")
    }

    // ✅ Lưu khung giờ khi người dùng tạm đặt (trong giỏ)
    suspend fun saveBooking(court: String, date: String, time: String) {
        context.bookingDataStore.edit { prefs ->
            val existing = prefs[BOOKINGS_KEY] ?: "[]"
            val jsonArray = JSONArray(existing)

            val booking = JSONObject().apply {
                put("court", court)
                put("date", date)
                put("time", time)
                put("status", "TEMP") // tạm giữ (chưa thanh toán)
            }

            jsonArray.put(booking)
            prefs[BOOKINGS_KEY] = jsonArray.toString()
        }
    }

    // ✅ Lấy danh sách khung giờ đã được đặt (đã thanh toán hoặc tạm)
    suspend fun getBookedSlots(court: String, date: String): List<String> {
        val jsonText = context.bookingDataStore.data.map { it[BOOKINGS_KEY] ?: "[]" }.first()
        val jsonArray = JSONArray(jsonText)
        val list = mutableListOf<String>()

        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            if (item.getString("court") == court && item.getString("date") == date) {
                list.add(item.getString("time"))
            }
        }
        return list
    }

    // ✅ Đánh dấu các slot đã thanh toán là "booked" vĩnh viễn
    suspend fun markSlotsAsBooked(court: String, date: String, times: List<String>) {
        context.bookingDataStore.edit { prefs ->
            val existing = prefs[BOOKINGS_KEY] ?: "[]"
            val jsonArray = JSONArray(existing)

            // Nếu slot đã tồn tại trong danh sách, bỏ qua để tránh duplicate
            val existingList = mutableListOf<JSONObject>()
            for (i in 0 until jsonArray.length()) {
                existingList.add(jsonArray.getJSONObject(i))
            }

            times.forEach { time ->
                val alreadyExists = existingList.any {
                    it.getString("court") == court &&
                            it.getString("date") == date &&
                            it.getString("time") == time
                }

                if (!alreadyExists) {
                    val booking = JSONObject().apply {
                        put("court", court)
                        put("date", date)
                        put("time", time)
                        put("status", "BOOKED")
                    }
                    jsonArray.put(booking)
                }
            }

            prefs[BOOKINGS_KEY] = jsonArray.toString()
        }
    }

    // ✅ (tuỳ chọn) Xóa toàn bộ dữ liệu đặt sân
    suspend fun clearAllBookings() {
        context.bookingDataStore.edit { it.remove(BOOKINGS_KEY) }
    }
}
