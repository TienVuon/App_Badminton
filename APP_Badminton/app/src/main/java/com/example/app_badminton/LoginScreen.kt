package com.example.app_badminton

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.BorderStroke
import com.example.app_badminton.firebase.FirebaseAuthManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.launch

// 🎨 Màu chủ đề
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
    val authManager = remember { FirebaseAuthManager() }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }

    // ⚙️ Google Sign-In launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.result
            scope.launch {
                val signInResult = authManager.firebaseAuthWithGoogle(account)
                if (signInResult == "success") {
                    snackbarHostState.showSnackbar("✅ Đăng nhập Google thành công!")
                    navController.navigate("home_screen") {
                        popUpTo("login_screen") { inclusive = true }
                    }
                } else {
                    snackbarHostState.showSnackbar("❌ Đăng nhập Google thất bại: $signInResult")
                }
            }
        } catch (e: Exception) {
            scope.launch {
                snackbarHostState.showSnackbar("⚠️ Lỗi đăng nhập Google: ${e.message}")
            }
        }
    }

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

                // 📨 Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
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

                // 🔒 Mật khẩu
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

                // 🔁 Ghi nhớ
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(checkedColor = LoginScreenColors.AccentBlue)
                    )
                    Text("Ghi nhớ đăng nhập", color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 🚀 Nút đăng nhập Firebase (Email & Password)
                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            scope.launch { snackbarHostState.showSnackbar("⚠️ Vui lòng nhập đủ thông tin!") }
                            return@Button
                        }

                        scope.launch {
                            loading = true
                            val result = authManager.loginUser(email, password)
                            loading = false
                            if (result != null && !result.contains("Exception")) {
                                snackbarHostState.showSnackbar("✅ Đăng nhập thành công!")
                                navController.navigate("home_screen") {
                                    popUpTo("login_screen") { inclusive = true }
                                }
                            } else {
                                snackbarHostState.showSnackbar("❌ Đăng nhập thất bại: $result")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LoginScreenColors.PrimaryGreen),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !loading
                ) {
                    Text(
                        if (loading) "Đang đăng nhập..." else "ĐĂNG NHẬP",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 🔐 Nút đăng nhập Google
                OutlinedButton(
                    onClick = {
                        val activity = context as? Activity
                        if (activity != null) {
                            val signInClient = authManager.getGoogleSignInClient(activity)
                            googleSignInLauncher.launch(signInClient.signInIntent)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = LoginScreenColors.AccentBlue),
                    border = BorderStroke(1.dp, LoginScreenColors.AccentBlue)
                ) {
                    Icon(Icons.Filled.Visibility, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Đăng nhập bằng Google", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 📄 Chuyển sang đăng ký
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
