package com.drewjya.pdfmaster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.drewjya.pdfmaster.Screen

// Tailwind Colors from React snippet
private val Indigo100 = Color(0xFFE0E7FF)
private val Indigo700 = Color(0xFF4338CA)
private val Slate600 = Color(0xFF475569)
private val Slate800 = Color(0xFF1E293B)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavigationItem(
    screen: Screen,
    isSelected: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit,
) {
    var isHovered by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(
                    if (isSelected) {
                        Indigo700.copy(
                            alpha = 0.2f,
                        )
                    } else if (isHovered) {
                        Indigo700.copy(
                            alpha = 0.2f,
                        )
                    } else {
                        Color.Transparent
                    },
                ).onPointerEvent(PointerEventType.Enter) { isHovered = true }
                .onPointerEvent(PointerEventType.Exit) { isHovered = false }
                .clickable(onClick = onClick)
                .padding(vertical = 4.dp)
                .padding(horizontal = if (isExpanded) 12.dp else 0.dp),
        contentAlignment = if (isExpanded) Alignment.CenterStart else Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Icon Placeholder
            Box(
                modifier =
                    Modifier
                        .background(
                            if (isSelected) {
                                Indigo700.copy(
                                    alpha = 0.2f,
                                )
                            } else {
                                Slate600.copy(
                                    alpha = 0.1f,
                                )
                            },
                            shape = RoundedCornerShape(6.dp),
                        ).size(30.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = screen.icon,
                    contentDescription = null,
                    tint = if (isSelected) Indigo700 else Slate600,
                    modifier = Modifier.size(20.dp),
                )
            }

            if (isExpanded) {
                Spacer(Modifier.width(12.dp))
                Text(
                    text = screen.title,
                    color = if (isSelected) Indigo700 else Slate600,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
