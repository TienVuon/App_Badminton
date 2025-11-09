package com.example.app_badminton.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.UUID

// ✅ DataStore riêng để lưu lịch sử đặt sân
val Context.historyDataStore by preferencesDataStore(name = "history_prefs")

// ✅ Cấu trúc dữ liệu 1 item trong lịch sử
data class BookingHistoryItem(
    val id: String = UUID.randomUUID().toString(),
    val court: String,
    val date: String,
    val time: String,
    val price: Int,
    val status: String = "PAID",  // Trạng thái: PAID, CANCELED, REFUNDED...
    val paidAt: Long = System.currentTimeMillis()
)

// ✅ Lớp quản lý đọc/ghi DataStore
class BookingHistoryPreferences(private val context: Context) {

    companion object {
        private val HISTORY_KEY = stringPreferencesKey("history_items")
    }

    /**
     * 🧾 Thêm nhiều item mới (thường gọi sau khi thanh toán xong giỏ hàng)
     */
    suspend fun appendFromCartItems(cartItems: List<CartItem>) {
        if (cartItems.isEmpty()) return

        context.historyDataStore.edit { prefs ->
            val existing = prefs[HISTORY_KEY] ?: "[]"
            val jsonArray = try {
                JSONArray(existing)
            } catch (e: JSONException) {
                JSONArray()
            }

            cartItems.forEach { item ->
                val obj = JSONObject().apply {
                    put("id", UUID.randomUUID().toString())
                    put("court", item.court)
                    put("date", item.date)
                    put("time", item.time)
                    put("price", item.price)
                    put("status", "PAID")
                    put("paidAt", System.currentTimeMillis())
                }
                jsonArray.put(obj)
            }

            prefs[HISTORY_KEY] = jsonArray.toString()
        }
    }

    /**
     * 📜 Lấy toàn bộ danh sách lịch sử đã lưu
     */
    suspend fun getAll(): List<BookingHistoryItem> {
        val jsonText = context.historyDataStore.data.map {
            it[HISTORY_KEY] ?: "[]"
        }.first()

        val arr = try {
            JSONArray(jsonText)
        } catch (e: JSONException) {
            JSONArray()
        }

        val list = mutableListOf<BookingHistoryItem>()
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            list.add(
                BookingHistoryItem(
                    id = o.optString("id"),
                    court = o.optString("court"),
                    date = o.optString("date"),
                    time = o.optString("time"),
                    price = o.optInt("price"),
                    status = o.optString("status", "PAID"),
                    paidAt = o.optLong("paidAt", System.currentTimeMillis())
                )
            )
        }

        // Sắp xếp giảm dần theo thời gian thanh toán
        return list.sortedByDescending { it.paidAt }
    }

    /**
     * 🧹 Xóa sạch toàn bộ lịch sử (ít khi dùng, ví dụ khi đăng xuất)
     */
    suspend fun clearAll() {
        context.historyDataStore.edit { it.remove(HISTORY_KEY) }
    }
}
