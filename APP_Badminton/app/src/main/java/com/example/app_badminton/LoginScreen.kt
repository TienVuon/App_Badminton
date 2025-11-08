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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
// Giả định bạn có file UserPreferences.kt
import com.example.app_badminton.data.UserPreferences
import kotlinx.coroutines.launch

// Định nghĩa Màu sắc
object LoginScreen {
    val PrimaryGreen = Color(0xFF4CAF50)
    val AccentBlue = Color(0xFF1976D2)
    val LightBackground = Color(0xFFF5F5F5)
    val CardBackground = Color.White
}

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginScreen.LightBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(LoginScreen.CardBackground)
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Text(text = "BADMINTON UTH", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = LoginScreen.PrimaryGreen)
            Text(text = "ĐĂNG NHẬP", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = username, onValueChange = { username = it }, label = { Text("Tên đăng nhập") },
                singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = LoginScreen.AccentBlue, unfocusedBorderColor = Color.LightGray, focusedLabelColor = LoginScreen.AccentBlue)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password, onValueChange = { password = it }, label = { Text("Mật khẩu") },
                // Logic hiển thị/ẩn mật khẩu
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = LoginScreen.AccentBlue, unfocusedBorderColor = Color.LightGray, focusedLabelColor = LoginScreen.AccentBlue),
                // Icon Mắt
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu")
                    }
                }
            )

            Box(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), contentAlignment = Alignment.CenterEnd) {
                // trong cột nội dung login, thêm (sau password field, trước nút đăng nhập hoặc cạnh nó)
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Quên mật khẩu?",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clickable { navController.navigate("forgot_password") }
                            .padding(end = 8.dp, top = 8.dp)
                    )
                }

            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        try {
                            val success = userPrefs.validateUser(username, password)
                            if (success) {
                                message = ""
                                navController.navigate("home_screen") { popUpTo("login_screen") { inclusive = true } }
                            } else {
                                message = "Sai tên đăng nhập hoặc mật khẩu!"
                            }
                        } catch (e: Exception) {
                            message = "Lỗi khi đăng nhập: ${e.message}"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LoginScreen.PrimaryGreen),
                shape = RoundedCornerShape(12.dp), elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("ĐĂNG NHẬP", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            if (message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = message, color = Color.Red, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Chưa có tài khoản? ", color = Color.Gray, fontSize = 16.sp)
                Text(text = "Đăng ký ngay", color = LoginScreen.AccentBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { navController.navigate("register_screen") })
            }
        }
    }
}