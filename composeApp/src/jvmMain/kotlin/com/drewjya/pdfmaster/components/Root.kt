package com.drewjya.pdfmaster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.drewjya.pdfmaster.design.AppTheme
import org.koin.compose.koinInject

@Composable
fun Root(appTheme: AppTheme = koinInject()) {
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
        Footer(appTheme = appTheme)
    }
}
