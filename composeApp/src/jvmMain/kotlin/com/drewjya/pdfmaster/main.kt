package com.drewjya.pdfmaster

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.drewjya.pdfmaster.di.appModule
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext.startKoin

fun main() {
    FileKit.init(appId = "PdfMaster")
    startKoin {
        modules(appModule)
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "PdfMaster",
        ) {
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

            val pickerFile =
                rememberFilePickerLauncher(
                    type = FileKitType.File(extensions = setOf("pdf")),
                    mode = FileKitMode.Single,
                    onResult = { file ->
                        println("Directory: ${file?.name} ${file?.path}")
                        if (file != null) {
                            pdfViewModel.setName(file.file.name)
                            pdfViewModel.setDirectory(file.file.parent)
                        }
                    },
                )

            val pickerDirectory =
                rememberDirectoryPickerLauncher(
                    onResult = { directory ->
                        println("Directory: $directory")
                        if (directory != null) {
                            pdfViewModel.setDirectory(directory.path)
                        }
                    },
                )
            App(
                pickerFiles = pickerFiles,
                pickerFile = pickerFile,
                pickerDirectory = pickerDirectory,
            )
        }
    }
}
