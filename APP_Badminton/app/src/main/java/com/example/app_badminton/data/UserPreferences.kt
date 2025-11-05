package com.example.app_badminton.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

// ✅ Tạo DataStore riêng cho người dùng
val Context.dataStore by preferencesDataStore(name = "user_prefs")

// ✅ Model người dùng
data class User(
    val username: String,
    val password: String,
    val fullName: String = "",
    val gender: String = "",
    val phone: String = "",
    val address: String = "",
    val email: String = "",
    val avatarUri: String = ""
)

class UserPreferences(private val context: Context) {

    companion object {
        private val USERS_KEY = stringPreferencesKey("users")
        private val LOGGED_USER_KEY = stringPreferencesKey("logged_user")
    }

    /**
     * ✅ LƯU NGƯỜI DÙNG MỚI (ĐĂNG KÝ) - Đã Cập Nhật 4 Tham Số
     */
    suspend fun saveUser(
        username: String,
        password: String,
        fullName: String, // <--- THÊM
        phone: String     // <--- THÊM
    ) {
        context.dataStore.edit { prefs ->
            val jsonArray = JSONArray(prefs[USERS_KEY] ?: "[]")

            // Kiểm tra trùng username (không phân biệt hoa thường)
            for (i in 0 until jsonArray.length()) {
                val user = jsonArray.getJSONObject(i)
                if (user.getString("username").equals(username, ignoreCase = true)) {
                    return@edit // Nếu đã tồn tại thì bỏ qua
                }
            }

            // Tạo user mới với thông tin đầy đủ
            val newUser = JSONObject().apply {
                put("username", username.trim())
                put("password", password.trim())
                put("fullName", fullName.trim()) // <--- LƯU fullName
                put("phone", phone.trim())       // <--- LƯU phone
                put("gender", "")
                put("address", "")
                put("email", "")
                put("avatarUri", "")
            }

            jsonArray.put(newUser)
            prefs[USERS_KEY] = jsonArray.toString()

            // Tự động đăng nhập sau khi đăng ký
            prefs[LOGGED_USER_KEY] = username.trim()
        }
    }

    // ✅ Cập nhật thông tin hồ sơ người dùng
    suspend fun updateUserProfile(
        username: String,
        fullName: String,
        gender: String,
        phone: String,
        address: String,
        email: String,
        avatarUri: String
    ) {
        context.dataStore.edit { prefs ->
            val jsonArray = JSONArray(prefs[USERS_KEY] ?: "[]")

            for (i in 0 until jsonArray.length()) {
                val user = jsonArray.getJSONObject(i)
                if (user.getString("username").equals(username, ignoreCase = true)) {
                    user.put("fullName", fullName)
                    user.put("gender", gender)
                    user.put("phone", phone)
                    user.put("address", address)
                    user.put("email", email)
                    user.put("avatarUri", avatarUri)
                }
            }

            prefs[USERS_KEY] = jsonArray.toString()
        }
    }

    // ✅ Lấy toàn bộ danh sách người dùng trong DataStore
    private suspend fun getAllUsers(): List<User> {
        val jsonText = context.dataStore.data.map { it[USERS_KEY] ?: "[]" }.first()
        val jsonArray = JSONArray(jsonText)
        val list = mutableListOf<User>()

        for (i in 0 until jsonArray.length()) {
            val u = jsonArray.getJSONObject(i)
            list.add(
                User(
                    username = u.getString("username"),
                    password = u.getString("password"),
                    fullName = u.optString("fullName", ""),
                    gender = u.optString("gender", ""),
                    phone = u.optString("phone", ""),
                    address = u.optString("address", ""),
                    email = u.optString("email", ""),
                    avatarUri = u.optString("avatarUri", "")
                )
            )
        }
        return list
    }

    // ✅ Kiểm tra tài khoản đăng nhập (Validate)
    suspend fun validateUser(username: String, password: String): Boolean {
        val users = getAllUsers()
        val match = users.any {
            it.username.trim().equals(username.trim(), ignoreCase = true) &&
                    it.password.trim() == password.trim()
        }
        if (match) setLoggedInUser(username.trim()) // Lưu trạng thái đăng nhập khi validate thành công
        return match
    }

    // ✅ Kiểm tra user đã tồn tại
    suspend fun isUserExists(username: String): Boolean {
        return getAllUsers().any { it.username.equals(username, ignoreCase = true) }
    }

    // ✅ Lưu thông tin user đang đăng nhập
    suspend fun setLoggedInUser(username: String) {
        context.dataStore.edit { prefs -> prefs[LOGGED_USER_KEY] = username }
    }

    // ✅ Lấy thông tin user đang đăng nhập
    suspend fun getLoggedInUser(): User? {
        val loggedUser = context.dataStore.data.map { it[LOGGED_USER_KEY] }.first() ?: return null
        return getAllUsers().find { it.username.equals(loggedUser, ignoreCase = true) }
    }

    // ✅ Đăng xuất
    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs.remove(LOGGED_USER_KEY)
        }
    }

    // ✅ Tìm người dùng theo số điện thoại
    suspend fun findUserByPhone(phone: String): User? {
        val users = getAllUsers()
        return users.find { it.phone.trim() == phone.trim() && it.phone.isNotEmpty() }
    }

    // ✅ Cập nhật mật khẩu mới
    suspend fun updatePassword(username: String, newPassword: String) {
        context.dataStore.edit { prefs ->
            val jsonArray = JSONArray(prefs[USERS_KEY] ?: "[]")

            for (i in 0 until jsonArray.length()) {
                val user = jsonArray.getJSONObject(i)
                if (user.getString("username").equals(username, ignoreCase = true)) {
                    user.put("password", newPassword.trim())
                }
            }

            prefs[USERS_KEY] = jsonArray.toString()
        }
    }
}