package com.drewjya.pdfmaster.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.drewjya.pdfmaster.Screen
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import io.github.vinceglb.filekit.dialogs.compose.PickerResultLauncher

// private val Slate900 = Color(0xFF0F172A)

@Composable
fun MainContent(
    screen: Screen,
    pickerFiles: PickerResultLauncher,
    pickerFile: PickerResultLauncher,
    pickerDirectory: PickerResultLauncher,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp, horizontal = 12.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HeaderContent(screen, pickerFiles, modifier = Modifier.padding(horizontal = 12.dp))
            ConfigurationContent(
                screen = screen,
                windowWidthSizeClass = windowSizeClass.windowWidthSizeClass,
                pickerFile = pickerFile,
                pickerDirectory = pickerDirectory,
            )
            FileList()
        }
    }
//    }
}
