package com.drewjya.pdfmaster

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.drewjya.pdfmaster.di.appModule
import io.github.vinceglb.filekit.FileKit
import java.awt.Dimension
import org.jetbrains.compose.resources.painterResource
import org.koin.core.context.GlobalContext.startKoin
import pdfmaster.composeapp.generated.resources.Res
import pdfmaster.composeapp.generated.resources.icon

fun main() {
    FileKit.init(appId = "PdfMaster")
    startKoin {
        modules(appModule)
    }
    application {
        val windowIcon = painterResource(Res.drawable.icon)
        Window(
            onCloseRequest = ::exitApplication,
            title = "PdfMaster",
            icon = windowIcon,
        ) {
            window.minimumSize = Dimension(500, 500)
            App()
        }
    }
}
