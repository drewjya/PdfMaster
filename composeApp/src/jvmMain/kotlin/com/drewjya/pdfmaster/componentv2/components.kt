package com.drewjya.pdfmaster.componentv2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.drewjya.pdfmaster.components.MessageType
import com.drewjya.pdfmaster.components.SnackbarMessage
import com.drewjya.pdfmaster.design.AppTheme
import com.drewjya.pdfmaster.design.Icons
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.path
import org.koin.compose.koinInject

@Composable
fun App() {
    val appTheme = koinInject<AppTheme>()
    MaterialTheme(
        colorScheme =
            lightColorScheme(
                primary = appTheme.primary,
                onPrimary = Color.White,
                surface = appTheme.surface,
                onSurface = appTheme.onSurface,
                background = appTheme.neutral,
                onBackground = appTheme.onSurface,
            ),
    ) {
        Column(modifier = Modifier.fillMaxSize().background(appTheme.neutral)) {
            Navbar()
            MainLayout(
                modifier = Modifier.weight(1f),
                staging = { modifier ->
                    FileStagingPane(modifier = modifier)
                },
                configuration = { modifier ->
                    OutputParameterSections(modifier = modifier.fillMaxSize())
                },
            )

            DockedFooter() // Now has space at the bottom because MainLayout uses weight(1f)
        }
    }
}

@Composable
fun DockedFooter() {
    val pdfViewModel: PdfViewModel = koinInject()
    val appTheme = koinInject<AppTheme>()
    val files by pdfViewModel.pdfFiles.collectAsStateWithLifecycle()
    val value = remember { mutableStateOf("") }
    val pickerDirectory =
        rememberDirectoryPickerLauncher(
            onResult = { directory ->
                if (directory != null) {
                    val selectedFolder = directory.file
                    // 2. Check if Windows/Mac actually lets us write here!
                    if (selectedFolder.canWrite()) {
                        value.value = directory.path
                    } else {
                        pdfViewModel.snackbarMessage.value =
                            SnackbarMessage(
                                type = MessageType.Error,
                                title = "Permission Denied",
                                message = "Cannot write to ${directory.path}",
                            )
                    }
                }
            },
        )

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .shadow(elevation = 16.dp, spotColor = Color.Black.copy(alpha = 0.05f))
                .background(appTheme.surface)
                .border(1.dp, Color.Black.copy(alpha = 0.05f))
                .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        Box(modifier = Modifier.weight(1f)) {
            InputIcon(
                label = "TARGET DIRECTORY",
                onIconClick = pickerDirectory::launch,
                onValueChange = { value.value = it },
                text = value.value,
                icon = Icons.FolderOutput,
            )
        }

        val fileCount = files.size
        Box(modifier = Modifier.weight(0.6f).height(height = 32.dp), contentAlignment = Alignment.CenterEnd) {
            Button(
                onClick = { },
                enabled = fileCount > 0,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = appTheme.primary,
                        disabledContainerColor = appTheme.primary.copy(alpha = 0.4f),
                    ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text(
                    "Execute Compile" + if (fileCount > 0) " ($fileCount)" else "",
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    lineHeight = 12.sp,
                )
            }
        }
    }
}
