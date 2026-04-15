package com.drewjya.pdfmaster.design

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

interface AppTheme {
    val primary: Color
    val primaryHover: Color
    val secondary: Color
    val tertiary: Color
    val neutral: Color
    val surface: Color
    val surfaceAlt: Color
    val onSurface: Color
    val onSurfaceMuted: Color
    val error: Color
    val subtleBorder: Color
    val typography: Typography
}

class PrimaryAppTheme(
    override val primary: Color = Color(0xFF5B0D13),
    override val primaryHover: Color = Color(0xFF3A0005),
    override val secondary: Color = Color(0xFF9E6967),
    override val tertiary: Color = Color(0xFF002F4A),
    override val neutral: Color = Color(0xFFF9F7F6),
    override val surface: Color = Color(0xFFFFFFFF),
    override val surfaceAlt: Color = Color(0xFFF4F1F0),
    override val onSurface: Color = Color(0xFF1C1B1F),
    override val onSurfaceMuted: Color = Color(0xFF79747E),
    override val error: Color = Color(0xFFBA1A1A),
    override val subtleBorder: Color = Color.Black.copy(alpha = 0.05f),
    override val typography: Typography =
        Typography(
            displayLarge =
                TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    letterSpacing = (-0.02).em,
                    color = onSurface,
                ),
            headlineMedium =
                TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = onSurface,
                ),
            headlineSmall =
                TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = onSurface,
                ),
            bodyMedium =
                TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = onSurfaceMuted,
                    lineHeight = 20.sp,
                ),
            labelMedium =
                TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 0.05.em,
                    color = onSurfaceMuted,
                ),
        ),
) : AppTheme
