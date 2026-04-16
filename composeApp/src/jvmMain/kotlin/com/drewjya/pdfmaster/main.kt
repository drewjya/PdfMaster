package com.drewjya.pdfmaster

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.window.application
import com.drewjya.pdfmaster.components.Navbar
import com.drewjya.pdfmaster.di.appModule
import io.github.vinceglb.filekit.FileKit
import java.awt.Dimension
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.intui.standalone.theme.createDefaultTextStyle
import org.jetbrains.jewel.intui.standalone.theme.createEditorTextStyle
import org.jetbrains.jewel.intui.standalone.theme.default
import org.jetbrains.jewel.intui.standalone.theme.lightThemeDefinition
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.intui.window.styling.lightWithLightHeader
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.window.DecoratedWindow
import org.jetbrains.jewel.window.styling.TitleBarStyle
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
//        val windowState = rememberWindowState()
        val textStyle = JewelTheme.createDefaultTextStyle()
        val editorStyle = JewelTheme.createEditorTextStyle()

        val themeDefinition =
            JewelTheme.lightThemeDefinition(defaultTextStyle = textStyle, editorTextStyle = editorStyle)
        IntUiTheme(
            theme = themeDefinition,
            styling =
                ComponentStyling.default()
                    .decoratedWindow(titleBarStyle = TitleBarStyle.lightWithLightHeader()),
            swingCompatMode = true,
        ) {

            DecoratedWindow(
                onCloseRequest = ::exitApplication,
                title = "PdfMaster",


                icon = windowIcon,

                ) {


                window.minimumSize = Dimension(500, 500)

                Column {

                    Navbar()

                    App()

                }
            }
        }
    }


}
