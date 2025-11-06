package com.example.app_badminton.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

val Context.cartDataStore by preferencesDataStore(name = "cart_prefs")

data class CartItem(
    val court: String,
    val date: String,
    val time: String,
    val price: Int
)

class CartPreferences(private val context: Context) {

    companion object {
        private val CART_KEY = stringPreferencesKey("cart_items")
    }

    // ✅ Thêm vào giỏ hàng
    suspend fun addToCart(court: String, date: String, time: String, price: Int) {
        context.cartDataStore.edit { prefs ->
            val existing = prefs[CART_KEY] ?: "[]"
            val array = try {
                JSONArray(existing)
            } catch (e: Exception) {
                JSONArray() // fallback nếu dữ liệu lỗi
            }

            val newItem = JSONObject().apply {
                put("court", court)
                put("date", date)
                put("time", time)
                put("price", price)
            }

            array.put(newItem)
            prefs[CART_KEY] = array.toString()
        }
    }

    // ✅ Lấy danh sách giỏ hàng
    suspend fun getCartItems(): List<CartItem> {
        val json = context.cartDataStore.data.map { it[CART_KEY] ?: "[]" }.first()

        val array = try {
            JSONArray(json)
        } catch (e: Exception) {
            JSONArray() // nếu dữ liệu bị lỗi format
        }

        val list = mutableListOf<CartItem>()
        for (i in 0 until array.length()) {
            val o = array.optJSONObject(i)
            if (o != null) {
                list.add(
                    CartItem(
                        o.optString("court", ""),
                        o.optString("date", ""),
                        o.optString("time", ""),
                        o.optInt("price", 0)
                    )
                )
            }
        }
        return list
    }

    // ✅ Xóa mục trong giỏ hàng
    suspend fun removeItem(index: Int) {
        context.cartDataStore.edit { prefs ->
            val existing = prefs[CART_KEY] ?: "[]"
            val array = try {
                JSONArray(existing)
            } catch (e: Exception) {
                JSONArray()
            }

            val newArray = JSONArray()
            for (i in 0 until array.length()) {
                if (i != index) newArray.put(array.getJSONObject(i))
            }

            prefs[CART_KEY] = newArray.toString()
        }
    }

    // ✅ Xóa toàn bộ giỏ hàng (nếu cần)
    suspend fun clearCart() {
        context.cartDataStore.edit { prefs ->
            prefs[CART_KEY] = "[]"
        }
    }
}