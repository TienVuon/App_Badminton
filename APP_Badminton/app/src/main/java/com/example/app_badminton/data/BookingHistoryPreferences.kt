package com.example.app_badminton.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

// DataStore riêng cho lịch sử
val Context.historyDataStore by preferencesDataStore(name = "history_prefs")

data class BookingHistoryItem(
    val id: String = UUID.randomUUID().toString(),
    val court: String,
    val date: String,
    val time: String,
    val price: Int,
    val status: String = "PAID",       // PAID, REFUNDED... (mở rộng sau)
    val paidAt: Long = System.currentTimeMillis()
)

class BookingHistoryPreferences(private val context: Context) {

    companion object {
        private val HISTORY_KEY = stringPreferencesKey("history_items")
    }

    // Append nhiều item (chuyển từ giỏ sau khi thanh toán)
    suspend fun appendFromCartItems(cartItems: List<CartItem>) {
        if (cartItems.isEmpty()) return
        context.historyDataStore.edit { prefs ->
            val jsonArray = JSONArray(prefs[HISTORY_KEY] ?: "[]")
            cartItems.forEach { it ->
                val obj = JSONObject().apply {
                    put("id", UUID.randomUUID().toString())
                    put("court", it.court)
                    put("date", it.date)
                    put("time", it.time)
                    put("price", it.price)
                    put("status", "PAID")
                    put("paidAt", System.currentTimeMillis())
                }
                jsonArray.put(obj)
            }
            prefs[HISTORY_KEY] = jsonArray.toString()
        }
    }

    // Lấy toàn bộ lịch sử
    suspend fun getAll(): List<BookingHistoryItem> {
        val text = context.historyDataStore.data.map { it[HISTORY_KEY] ?: "[]" }.first()
        val arr = JSONArray(text)
        val list = mutableListOf<BookingHistoryItem>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                BookingHistoryItem(
                    id = o.optString("id"),
                    court = o.optString("court"),
                    date = o.optString("date"),
                    time = o.optString("time"),
                    price = o.optInt("price"),
                    status = o.optString("status", "PAID"),
                    paidAt = o.optLong("paidAt")
                )
            )
        }
        // Mặc định: mới nhất lên trước
        return list.sortedByDescending { it.paidAt }
    }

    // (tùy chọn) Xóa hết lịch sử
    suspend fun clearAll() {
        context.historyDataStore.edit { it.remove(HISTORY_KEY) }
    }
}
