package com.drewjya.pdfmaster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drewjya.pdfmaster.design.AppTheme
import org.koin.compose.koinInject

@Composable
fun CustomToggle(
    isActive: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    val appTheme: AppTheme = koinInject()
    val backgroundColor = if (isActive) appTheme.primary else appTheme.surfaceAlt
    val knobAlignment = if (isActive) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier =
            Modifier
                .width(36.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
                .clickable { onToggle(!isActive) }
                .padding(2.dp),
        contentAlignment = knobAlignment,
    ) {
        Box(
            modifier =
                Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(appTheme.surface),
        )
    }
}

@Composable
fun EditorialInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit = {},
) {
    val appTheme: AppTheme = koinInject()
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            style = appTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(42.dp),
            textStyle = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = appTheme.surface,
                    unfocusedContainerColor = appTheme.surfaceAlt,
                    focusedBorderColor = appTheme.secondary.copy(alpha = 0.3f),
                    unfocusedBorderColor = Color.Transparent,
                ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val appTheme: AppTheme = koinInject()
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = appTheme.primary,
                disabledContainerColor = appTheme.primary.copy(alpha = 0.4f),
            ),
        modifier = modifier.height(40.dp),
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}
