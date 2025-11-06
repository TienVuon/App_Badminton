package com.example.app_badminton.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

object ServiceTheme {
    val BackgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF7F7F7A), Color(0xFFE8F5E9)) // Xanh da trời nhạt -> Xanh lá nhạt
    )
    val PrimaryBlue = Color(0xFF009CA9)  // Xanh da trời
    val AccentGreen = Color(0xFF28A745)  // Xanh mint
    val DarkText = Color(0xFF212121)
    val LightText = Color(0xFF424242)
    val CardBackground = Color.White
    val HeaderGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF6F85F6), Color(0xFF24A5F5)) // Xanh đậm hơn
    )
}
