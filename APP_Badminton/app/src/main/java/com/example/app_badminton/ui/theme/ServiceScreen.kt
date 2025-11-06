package com.example.app_badminton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_badminton.ui.theme.ServiceTheme

// Mã khác của bạn


object ServiceTheme {
    val PrimaryBlue = Color(0xFF007BFF)
    val AccentGreen = Color(0xFF28A745)
    val DarkText = Color(0xFF343A40)
    val HeaderGradient = Brush.horizontalGradient(listOf(Color(0xFF00B4D8), Color(0xFF007BFF)))
    val BackgroundGradient = Brush.verticalGradient(listOf(Color(0xFFF0F0F0), Color(0xFFE5E5E5)))
    val CardBackground = Color(0xFFFFFFFF)
    val LightText = Color(0xFF6C757D)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("DỊCH VỤ", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = Color.White) },
                modifier = Modifier.background(ServiceTheme.HeaderGradient),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ServiceTheme.BackgroundGradient)
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Card(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ServiceTheme.CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ServiceButton(
                        text = "DANH SÁCH DỊCH VỤ",
                        onClick = { navController.navigate("food_screen") },
                        backgroundColor = ServiceTheme.PrimaryBlue,
                        textColor = Color.White,
                        isLarge = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ServiceButton(
                        text = "THỨC ĂN",
                        onClick = { navController.navigate("food_screen") },
                        backgroundColor = ServiceTheme.AccentGreen,
                        textColor = ServiceTheme.DarkText
                    )
                    ServiceButton(
                        text = "NƯỚC UỐNG",
                        onClick = { navController.navigate("drinks_screen") },
                        backgroundColor = ServiceTheme.AccentGreen,
                        textColor = ServiceTheme.DarkText
                    )
                    ServiceButton(
                        text = "DỊCH VỤ KHÁC",
                        onClick = { navController.navigate("other_service_screen") },
                        backgroundColor = ServiceTheme.AccentGreen,
                        textColor = ServiceTheme.DarkText
                    )
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun ServiceButton(text: String, onClick: () -> Unit, backgroundColor: Color, textColor: Color, isLarge: Boolean = false) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isLarge) 60.dp else 52.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
    ) {
        Text(text = text, fontSize = if (isLarge) 20.sp else 18.sp, fontWeight = FontWeight.ExtraBold, color = textColor, textAlign = TextAlign.Center)
    }
}
