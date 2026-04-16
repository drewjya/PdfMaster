package com.drewjya.pdfmaster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AppIcon(
    icon: ImageVector,
    backgroundColor: Color,
    color: Color,
    modifier: (Modifier) -> Modifier = { it },
) {
    val clip = RoundedCornerShape(8.dp)
    Box(
        modifier = modifier(Modifier.clip(clip).background(backgroundColor, clip)).padding(6.dp),
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = color,
        )
    }
}
