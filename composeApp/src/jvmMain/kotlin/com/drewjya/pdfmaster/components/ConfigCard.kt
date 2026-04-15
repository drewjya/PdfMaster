package com.drewjya.pdfmaster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.drewjya.pdfmaster.design.AppTheme
import org.koin.compose.koinInject

@Composable
fun ConfigCard(
    title: String,
    icon: ImageVector,
    isActive: Boolean,
    onToggleActive: (Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    val appTheme: AppTheme = koinInject()
    var isExpanded by remember { mutableStateOf(isActive) }

    // Auto-expand when activated
    LaunchedEffect(isActive) { if (isActive) isExpanded = true }

    val cardAlpha = if (isActive) 1f else 0.7f
    val iconBgColor = if (isActive) appTheme.primary.copy(alpha = 0.1f) else appTheme.surfaceAlt
    val iconColor = if (isActive) appTheme.primary else appTheme.onSurfaceMuted

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(appTheme.surface)
                .border(1.dp, appTheme.subtleBorder, RoundedCornerShape(16.dp))
                .padding(bottom = if (isExpanded) 16.dp else 0.dp),
    ) {
        // Header
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(20.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = appTheme.onSurfaceMuted.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp).rotate(if (isExpanded) 0f else -90f),
                )
                Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(iconBgColor).padding(6.dp)) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(16.dp))
                }
                Text(
                    title,
                    style = appTheme.typography.headlineSmall.copy(color = if (isActive) appTheme.onSurface else appTheme.onSurfaceMuted),
                )
            }
            CustomToggle(isActive = isActive, onToggle = onToggleActive)
        }

        // Expanded Content
        if (isExpanded) {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                content()
            }
        }
    }
}
