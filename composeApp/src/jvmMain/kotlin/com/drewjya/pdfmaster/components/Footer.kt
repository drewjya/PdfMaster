package com.drewjya.pdfmaster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.drewjya.pdfmaster.design.AppIcon
import com.drewjya.pdfmaster.design.AppTheme
import com.drewjya.pdfmaster.hooks.rememberAppInput
import com.drewjya.pdfmaster.viewmodel.ConfigViewModel
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.path
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Footer(
    appTheme: AppTheme = koinInject(),
    viewModel: ConfigViewModel = koinViewModel(),
    pdfViewModel: PdfViewModel = koinViewModel(),
) {
    val isProcessing by pdfViewModel.isProcessing.collectAsStateWithLifecycle(false)

    val activeConfig by viewModel.activeConfig.collectAsStateWithLifecycle(initialValue = null)
    val files by pdfViewModel.pdfFiles.collectAsStateWithLifecycle(emptyList())

    val (targetDir, setTargetDir) =
        rememberAppInput(
            externalValue = activeConfig?.targetDirectory,
            onValueChange = { newValue -> viewModel.updateBasicInfo(targetDir = newValue) },
        )

    val pickerDirectory =
        rememberDirectoryPickerLauncher(
            onResult = { directory ->
                if (directory != null) {
                    val selectedFolder = directory.file
                    if (selectedFolder.canWrite()) {
                        viewModel.updateBasicInfo(targetDir = directory.path)
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
            // 3. Use Input instead of InputIcon if you modified Input to use TextFieldValue
            // Or ensure your InputIcon was also updated to accept TextFieldValue
            InputIcon(
                label = "TARGET DIRECTORY",
                icon = AppIcon.FolderOutput,
                onIconClick = pickerDirectory::launch,
                value = targetDir, // Pass the TextFieldValue
                onValueChange = setTargetDir,
            )
        }

        val fileCount = files.size
        Box(
            modifier = Modifier.weight(0.6f).height(height = 32.dp),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Button(
                onClick = {
                    pdfViewModel.processFiles(
                        files = files,
                        configuration = activeConfig ?: return@Button
                    )

                },
                enabled = fileCount > 0 && !isProcessing,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = appTheme.primary,
                        disabledContainerColor = appTheme.primary.copy(alpha = 0.4f),
                    ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        if (isProcessing) "Comipling"
                        else "Execute Compile" + if (fileCount > 0) " ($fileCount)" else "",
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        lineHeight = 12.sp,
                        color = Color.White,
                    )
                }

            }
        }
    }
}
