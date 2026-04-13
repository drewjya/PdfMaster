package com.drewjya.pdfmaster.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.drewjya.pdfmaster.design.AppIcon
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.koin.compose.koinInject

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FileUploader() {
    val brandColor = Color(0xFF8B1E1E) // Dark red from your screenshot

    val pdfViewModel: PdfViewModel = koinInject()
    val pickerFiles =
        rememberFilePickerLauncher(
            type = FileKitType.File(extensions = setOf("pdf")),
            mode = FileKitMode.Multiple(),
            onResult = { files ->
                if (files != null) {
                    pdfViewModel.addFiles(files.map { it.file })
                }
            },
        )
    Button(
        onClick = { pickerFiles.launch() },
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
