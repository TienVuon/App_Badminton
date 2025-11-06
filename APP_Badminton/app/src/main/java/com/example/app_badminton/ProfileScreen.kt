package com.example.app_badminton

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.app_badminton.data.User
import com.example.app_badminton.data.UserPreferences
import kotlinx.coroutines.launch

// --- APP COLORS ---
object AppColors {
    val PrimaryColor = Color(0xFF4CAF50)
    val AccentColor = Color(0xFFFF9800)
    val DarkTextColor = Color(0xFF212121)
    val LightGreyBackground = Color(0xFFF7F7F7)
    val RedStrong = Color(0xFFFF5252)
    val RedDark = Color(0xFFD32F2F)

    val GradientAvatarBorder: Brush = Brush.sweepGradient(
        colors = listOf(PrimaryColor, AccentColor, PrimaryColor.copy(alpha = 0.5f), AccentColor)
    )

    val GradientLogoutButton: Brush = Brush.horizontalGradient(
        colors = listOf(RedStrong, RedDark)
    )
}

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()

    var user by remember { mutableStateOf<User?>(null) }
    var editMode by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        user = prefs.getLoggedInUser()
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && user != null) {
            user = user!!.copy(avatarUri = uri.toString())
        }
    }

    user?.let { current ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.LightGreyBackground)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- TIÊU ĐỀ ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "THÔNG TIN CÁ NHÂN",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AppColors.DarkTextColor,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            // --- AVATAR ---
            item {
                val avatarSize = 130.dp
                Box(
                    modifier = Modifier
                        .size(avatarSize)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    AppColors.PrimaryColor.copy(alpha = 0.15f),
                                    AppColors.AccentColor.copy(alpha = 0.15f)
                                )
                            ),
                            shape = CircleShape
                        )
                        .border(
                            border = BorderStroke(3.dp, AppColors.GradientAvatarBorder),
                            shape = CircleShape
                        )
                        .clickable(enabled = editMode) { imagePicker.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (current.avatarUri.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(current.avatarUri),
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(avatarSize - 8.dp)
                                .clip(CircleShape)
                                .align(Alignment.Center)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(avatarSize - 8.dp)
                                .background(AppColors.PrimaryColor, CircleShape)
                                .align(Alignment.Center),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = current.username.take(1).uppercase(),
                                fontSize = 48.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (editMode) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = 4.dp, y = 4.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(AppColors.AccentColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CameraAlt,
                                contentDescription = "Change Avatar",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- TÊN NGƯỜI DÙNG ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    current.username,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = AppColors.DarkTextColor
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // --- THÔNG TIN NGƯỜI DÙNG ---
            item { ProfileInputField("Họ và tên", current.fullName, { user = current.copy(fullName = it) }, editMode) }
            item { ProfileInputField("Giới tính", current.gender, { user = current.copy(gender = it) }, editMode) }
            item { ProfileInputField("Số điện thoại", current.phone, { user = current.copy(phone = it) }, editMode) }
            item { ProfileInputField("Địa chỉ", current.address, { user = current.copy(address = it) }, editMode) }
            item {
                ProfileInputField("Email", current.email, { user = current.copy(email = it) }, editMode)
                Spacer(modifier = Modifier.height(32.dp))
            }

            // --- NÚT SỬA / LƯU ---
            item {
                if (!editMode) {
                    Button(
                        onClick = { editMode = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("SỬA THÔNG TIN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                } else {
                    Button(
                        onClick = {
                            scope.launch {
                                prefs.updateUserProfile(
                                    current.username,
                                    current.fullName,
                                    current.gender,
                                    current.phone,
                                    current.address,
                                    current.email,
                                    current.avatarUri
                                )
                                message = "✅ Đã lưu thay đổi!"
                                editMode = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("LƯU THÔNG TIN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
            }

            // --- THÔNG BÁO ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                if (message.isNotEmpty()) {
                    Text(
                        text = message,
                        color = AppColors.PrimaryColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }

            // --- NÚT ĐĂNG XUẤT (ĐÃ FIX) ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            prefs.logout()
                            // ✅ Quay về login_screen và xóa toàn bộ back stack
                            navController.navigate("login_screen") {
                                popUpTo(0)
                                launchSingleTop = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    border = BorderStroke(2.dp, AppColors.GradientLogoutButton),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AppColors.RedDark,
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("ĐĂNG XUẤT", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// --- INPUT FIELD ---
@Composable
fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    editMode: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (editMode) onValueChange(it) },
        label = { Text(label, color = if (editMode) AppColors.PrimaryColor else Color.Gray) },
        readOnly = !editMode,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppColors.PrimaryColor,
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = AppColors.PrimaryColor,
            disabledBorderColor = Color.LightGray,
            disabledTextColor = AppColors.DarkTextColor,
            disabledLabelColor = Color.Gray
        ),
        textStyle = LocalTextStyle.current.copy(
            color = AppColors.DarkTextColor,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
}
