package com.drewjya.pdfmaster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.drewjya.pdfmaster.design.AppColor
import com.drewjya.pdfmaster.design.AppIcon
import com.drewjya.pdfmaster.design.Slate200
import com.drewjya.pdfmaster.design.Slate300
import com.drewjya.pdfmaster.design.Slate500
import io.github.vinceglb.filekit.utils.Platform

@Composable
fun InputButton(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String = "",
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {},
    onValueChange: (String) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        TextInput(
            modifier = Modifier.weight(1f),
            label = label,
            text = text,
            onValueChange = onValueChange,
            placeholder = placeholder,
        )

        Column {
            Box(modifier = Modifier.size(12.dp))
            Row(
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(AppColor.Slate300)
                        .border(1.dp, AppColor.Slate300, RoundedCornerShape(4.dp))
                        .clickable(onClick = onClick),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    icon,
                    contentDescription = icon.name,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp).size(24.dp),
                    tint = com.drewjya.pdfmaster.components.textAndLabelColor,
                )
            }
        }
    }
}
