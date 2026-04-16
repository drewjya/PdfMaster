package com.drewjya.pdfmaster.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.drewjya.pdfmaster.design.AppIcon
import com.drewjya.pdfmaster.design.AppTheme
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.window.styling.defaults
import org.jetbrains.jewel.intui.window.styling.lightWithLightHeader
import org.jetbrains.jewel.window.DecoratedWindowScope
import org.jetbrains.jewel.window.TitleBar
import org.jetbrains.jewel.window.defaultTitleBarStyle
import org.jetbrains.jewel.window.newFullscreenControls
import org.jetbrains.jewel.window.styling.TitleBarColors
import org.jetbrains.jewel.window.styling.TitleBarMetrics
import org.jetbrains.jewel.window.styling.TitleBarStyle
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DecoratedWindowScope.TitleBarView(
    pdfViewModel: PdfViewModel = koinViewModel(),
    appTheme: AppTheme = koinInject()
) {
    val style = JewelTheme.defaultTitleBarStyle
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
    val inputDirectory =
        rememberDirectoryPickerLauncher(
            onResult = { directory ->
                if (directory != null) {
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
    TitleBar(
        Modifier.newFullscreenControls(),
        style = TitleBarStyle(
            colors = TitleBarColors.lightWithLightHeader(
                borderColor = appTheme.neutral,
                backgroundColor = appTheme.neutral
            ),
            metrics = TitleBarMetrics.defaults(
                height = 36.dp
            ),
            icons = style.icons,
            dropdownStyle = style.dropdownStyle,
            iconButtonStyle = style.iconButtonStyle,
            paneButtonStyle = style.paneButtonStyle,
            paneCloseButtonStyle = style.paneCloseButtonStyle
        ),
    ) {
        Row(
            Modifier.align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            BadgeButton(
                label = "Add Files",
                icon = AppIcon.FileAdd,
                color = appTheme.primary,
                onClick = { pickerFiles.launch() },
            )
            BadgeButton(
                label = "Add Directory",
                icon = AppIcon.FolderPlus,
                color = appTheme.onSurface,
                onClick = { inputDirectory.launch() },
            )

            BadgeButton(
                label = "Delete All",
                icon = AppIcon.Trash,
                color = appTheme.error,
                onClick = { pdfViewModel.clearFiles() },
            )
        }


        Row(Modifier.align(Alignment.End).padding(end = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            VersionButton()
        }
    }
}