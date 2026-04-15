package com.drewjya.pdfmaster.componentv2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.drewjya.pdfmaster.design.AppTheme
import com.drewjya.pdfmaster.design.Icons
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.path
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Navbar(pdfViewModel: PdfViewModel = koinViewModel()) {
    val appTheme: AppTheme = koinInject()
    
    val pickerFiles =
        rememberFilePickerLauncher(
            type = FileKitType.File(extensions = setOf("pdf")),
            mode = FileKitMode.Multiple(),
            onResult = { files ->
                if (files != null) {
                    println("Selected files: ${files.map { it.file.name }}")
                    pdfViewModel.addFiles(files.map { it.file })
                }
            },
        )
    val inputDirectory =
        rememberDirectoryPickerLauncher(
            onResult = { directory ->
                if (directory != null) {
                    println("Selected directory: ${directory.path}")
                    pdfViewModel.inputDirectory.value = directory.path
                    println("${directory.file.listFiles()?.size}")
                    val files =
                        directory.file
                            .listFiles()
                            .orEmpty()
                            .toList()
                    val filteredFiles =
                        files.filter { it.isFile && it.extension.equals("pdf", ignoreCase = true) }
                    pdfViewModel.addFiles(filteredFiles)
                }
            },
        )

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(appTheme.neutral)
                .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            BadgeButton(
                label = "Add Files",
                icon = Icons.FileAdd,
                color = appTheme.primary,
                onClick = { pickerFiles.launch() },
            )
            BadgeButton(
                label = "Add Directory",
                icon = Icons.FolderPlus,
                color = appTheme.onSurface,
                onClick = { inputDirectory.launch() },
            )

            BadgeButton(
                label = "Delete All",
                icon = Icons.Trash,
                color = appTheme.error,
                onClick = { pdfViewModel.clearFiles() },
            )
        }
        VersionButton()
    }
}
