package com.drewjya.pdfmaster.componentv2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun MainLayout(
    modifier: Modifier = Modifier,
    staging: @Composable (Modifier) -> Unit, // Pass modifier as a parameter
    configuration: @Composable (Modifier) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isCompact = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    if (isCompact) {
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // In Column: staging takes what it needs, config takes the rest
            staging(Modifier.fillMaxWidth())
            configuration(Modifier.fillMaxWidth().weight(1f))
        }
    } else {
        Row(
            modifier =
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // In Row: split width via weights
            staging(Modifier.weight(0.6f))
            configuration(Modifier.weight(0.4f))
        }
    }
}
