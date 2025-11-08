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

// Màu chủ đề cho Login
object LoginScreenColors {
    val PrimaryGreen = Color(0xFF4CAF50)
    val AccentBlue = Color(0xFF1976D2)
    val LightBackground = Color(0xFFF5F5F5)
    val CardBackground = Color.White
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(LoginScreenColors.LightBackground),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(LoginScreenColors.CardBackground)
                    .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(24.dp)
            ) {
                Text(
                    text = "BADMINTON UTH",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LoginScreenColors.PrimaryGreen
                )
                Text(
                    text = "ĐĂNG NHẬP",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Username field
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Tên đăng nhập") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LoginScreenColors.AccentBlue,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = LoginScreenColors.AccentBlue
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mật khẩu") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        val image =
                            if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LoginScreenColors.AccentBlue,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = LoginScreenColors.AccentBlue
                    )
                )

                // Quên mật khẩu
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "Quên mật khẩu?",
                        color = LoginScreenColors.AccentBlue,
                        modifier = Modifier
                            .clickable { navController.navigate("forgot_password") }
                            .padding(end = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Nút đăng nhập
                Button(
                    onClick = {
                        scope.launch {
                            when {
                                username.isBlank() || password.isBlank() -> {
                                    snackbarHostState.showSnackbar("⚠️ Vui lòng nhập đủ thông tin!")
                                }

                                userPrefs.validateUser(username, password) -> {
                                    snackbarHostState.showSnackbar("✅ Đăng nhập thành công!")
                                    navController.navigate("home_screen") {
                                        popUpTo("login_screen") { inclusive = true }
                                    }
                                }

                                else -> {
                                    snackbarHostState.showSnackbar("❌ Sai tên đăng nhập hoặc mật khẩu!")
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LoginScreenColors.PrimaryGreen),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("ĐĂNG NHẬP", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Đăng ký
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Chưa có tài khoản? ", color = Color.Gray, fontSize = 16.sp)
                    Text(
                        text = "Đăng ký ngay",
                        color = LoginScreenColors.AccentBlue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { navController.navigate("register_screen") }
                    )
                }
            }
        }
    }
}
