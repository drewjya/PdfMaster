package com.drewjya.pdfmaster.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drewjya.pdfmaster.design.AppIcon
import io.github.vinceglb.filekit.dialogs.compose.PickerResultLauncher

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FileUploader(picker: PickerResultLauncher) {
    val brandColor = Color(0xFF8B1E1E) // Dark red from your screenshot

    Button(
        onClick = { picker.launch() },
        colors =
            ButtonDefaults.buttonColors(
                containerColor = brandColor,
                contentColor = Color.White,
            ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Icon(
            imageVector = AppIcon.Upload,
            contentDescription = "Upload Cloud Icon",
            modifier = Modifier.size(20.dp),
        )
    }
}
