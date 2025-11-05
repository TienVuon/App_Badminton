package com.example.app_badminton

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_badminton.data.UserPreferences
import kotlinx.coroutines.launch

// --- Định nghĩa Màu sắc Mới (Đã đồng bộ) ---
object LoginColors {
    val PrimaryGreen = Color(0xFF4CAF50)
    val AccentBlue = Color(0xFF1976D2)
    val LightBackground = Color(0xFFF5F5F5)
    val CardBackground = Color.White
    val ShadowColor = Color(0x33000000)
    val DarkTextColor = Color(0xFF212121)
}

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginColors.LightBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(LoginColors.CardBackground)
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {

            Text(
                text = "BADMINTON UTH",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = LoginColors.PrimaryGreen
            )
            Text(
                text = "TẠO TÀI KHOẢN MỚI",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- CÁC TRƯỜNG NHẬP LIỆU ---
            StyledOutlinedTextField(value = username, onValueChange = { username = it }, label = "Tên đăng nhập")
            Spacer(modifier = Modifier.height(12.dp))
            StyledOutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = "Họ và tên")
            Spacer(modifier = Modifier.height(12.dp))
            StyledOutlinedTextField(
                value = phone,
                onValueChange = {
                    // Chỉ cho phép nhập số và giới hạn 10 ký tự
                    if (it.length <= 10) phone = it
                },
                label = "Số điện thoại",
                keyboardType = KeyboardType.Phone
            )
            Spacer(modifier = Modifier.height(12.dp))
            StyledOutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = "Mật khẩu",
                isPassword = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            StyledOutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Nhập lại mật khẩu",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nút đăng ký
            Button(
                onClick = {
                    scope.launch {
                        when {
                            username.isBlank() || password.isBlank() || fullName.isBlank() || phone.isBlank() -> message = "Vui lòng nhập đầy đủ thông tin"
                            !phone.matches(Regex("^\\d{10}$")) -> message = "Số điện thoại phải là 10 chữ số"
                            password.length < 6 -> message = "Mật khẩu phải có ít nhất 6 ký tự"
                            password != confirmPassword -> message = "Mật khẩu không khớp"
                            userPrefs.isUserExists(username) -> message = "Tên đăng nhập đã tồn tại"
                            else -> {
                                // ✅ SỬA LỖI: GỌI ĐÚNG HÀM saveUser VỚI ĐỦ 4 THAM SỐ
                                userPrefs.saveUser(username, password, fullName, phone)

                                message = "✅ Đăng ký thành công! Vui lòng đăng nhập."
                                navController.navigate("login") { popUpTo("register") { inclusive = true } }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LoginColors.PrimaryGreen),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("ĐĂNG KÝ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            // Hiển thị thông báo
            if (message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    color = if (message.startsWith("✅")) LoginColors.PrimaryGreen else Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Liên kết chuyển sang đăng nhập
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Đã có tài khoản? ",
                    color = Color.Gray,
                    fontSize = 16.sp,
                )
                Text(
                    text = "Đăng nhập ngay",
                    color = LoginColors.AccentBlue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
            }
        }
    }
}


/**
 * Hàm Composable helper để tạo OutlinedTextField đồng bộ về phong cách.
 */
@Composable
fun StyledOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LoginColors.AccentBlue,
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = LoginColors.AccentBlue,
            unfocusedLabelColor = Color.Gray
        ),
        textStyle = LocalTextStyle.current.copy(color = LoginColors.DarkTextColor)
    )
}