package com.drewjya.pdfmaster.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drewjya.pdfmaster.Screen

// Tailwind Colors from React snippet
private val Slate50 = Color(0xFFF8FAFC)
private val Slate100 = Color(0xFFF1F5F9)
private val Slate200 = Color(0xFFE2E8F0)
private val Slate400 = Color(0xFF94A3B8)
private val Slate500 = Color(0xFF64748B)
private val Slate900 = Color(0xFF0F172A)
private val Indigo600 = Color(0xFF4F46E5)

@Composable
fun Sidebar(
    isExpanded: Boolean,
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    onToggleExpand: () -> Unit,
) {
    val width by animateDpAsState(if (isExpanded) 256.dp else 64.dp)

    Surface(
        modifier = Modifier.width(width).fillMaxHeight(),
        color = Slate100,
    ) {
        Box(Modifier.fillMaxSize()) {
            VerticalDivider(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().width(1.dp),
                color = Slate200,
            )

            Column(Modifier.fillMaxSize()) {
                Row(
                    modifier =
                        Modifier
                            .height(64.dp)
                            .fillMaxWidth()
                            .drawBehind {
                                val strokeWidth = 1.dp.toPx()
                                val y = size.height - strokeWidth / 2
                                drawLine(
                                    color = Slate200.copy(alpha = 0.6f),
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = strokeWidth,
                                )
                            }.padding(horizontal = if (isExpanded) 20.dp else 0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (isExpanded) Arrangement.SpaceBetween else Arrangement.Center,
                ) {
                    if (isExpanded) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Indigo600),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text("P", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Text(
                                text = "PdfStudio",
                                color = Slate900,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.5).sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }

                    IconButton(
                        onClick = onToggleExpand,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Text(
                            text = if (isExpanded) "«" else "»",
                            color = Slate500,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light,
                        )
                    }
                }

                // Navigation Tools: flex-1 py-5 px-3
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp, start = 12.dp, end = 12.dp),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Screen.entries.forEach { screen ->
                            NavigationItem(
                                screen = screen,
                                isSelected = currentScreen == screen,
                                isExpanded = isExpanded,
                                onClick = { onScreenSelected(screen) },
                            )
                        }
                    }
                }
            }
        }
    }
}
