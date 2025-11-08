package com.example.app_badminton

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

// --- MÀU CHỦ ĐỀ ---
object LoginColors {
    val PrimaryGreen = Color(0xFF4CAF50)
    val AccentBlue = Color(0xFF1976D2)
    val LightBackground = Color(0xFFF5F5F5)
    val CardBackground = Color.White
    val ShadowColor = Color(0x33000000)
    val DarkTextColor = Color(0xFF212121)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
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

                StyledOutlinedTextField(value = username, onValueChange = { username = it }, label = "Tên đăng nhập")
                Spacer(modifier = Modifier.height(12.dp))
                StyledOutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = "Họ và tên")
                Spacer(modifier = Modifier.height(12.dp))
                StyledOutlinedTextField(
                    value = phone,
                    onValueChange = {
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

                Button(
                    onClick = {
                        scope.launch {
                            when {
                                username.isBlank() || password.isBlank() || fullName.isBlank() || phone.isBlank() -> {
                                    snackbarHostState.showSnackbar("⚠️ Vui lòng nhập đầy đủ thông tin")
                                }
                                !phone.matches(Regex("^\\d{10}$")) -> {
                                    snackbarHostState.showSnackbar("⚠️ Số điện thoại phải là 10 chữ số")
                                }
                                password.length < 6 -> {
                                    snackbarHostState.showSnackbar("⚠️ Mật khẩu phải có ít nhất 6 ký tự")
                                }
                                password != confirmPassword -> {
                                    snackbarHostState.showSnackbar("⚠️ Mật khẩu không khớp")
                                }
                                userPrefs.isUserExists(username) -> {
                                    snackbarHostState.showSnackbar("⚠️ Tên đăng nhập đã tồn tại")
                                }
                                else -> {
                                    userPrefs.saveUser(username, password, fullName, phone)
                                    snackbarHostState.showSnackbar("🎉 Đăng ký thành công! Vui lòng đăng nhập.")
                                    navController.navigate("login_screen") {
                                        popUpTo("register_screen") { inclusive = true }
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LoginColors.PrimaryGreen),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("ĐĂNG KÝ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Đã có tài khoản? ", color = Color.Gray, fontSize = 16.sp)
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
}

/**
 * OutlinedTextField có thêm nút con mắt để ẩn/hiện mật khẩu
 */
@Composable
fun StyledOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = {
            if (isPassword) {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description, tint = LoginColors.AccentBlue)
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LoginColors.AccentBlue,
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = LoginColors.AccentBlue,
            unfocusedLabelColor = Color.Gray
        ),
        textStyle = LocalTextStyle.current.copy(color = LoginColors.DarkTextColor)
    )
}
