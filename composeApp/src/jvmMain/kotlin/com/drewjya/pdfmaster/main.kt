package com.drewjya.pdfmaster

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.drewjya.pdfmaster.di.appModule
import io.github.vinceglb.filekit.FileKit
import java.awt.Dimension
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
            window.minimumSize = Dimension(500, 500)
            App()
        }
    }
}
