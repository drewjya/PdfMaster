package com.drewjya.pdfmaster.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.BiasAlignment
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
        val scrollState = rememberScrollState()
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // In Column: staging takes what it needs, config takes the rest
            staging(Modifier.fillMaxWidth())
            configuration(Modifier.fillMaxWidth())
        }
    } else {
        Row(
            modifier =
                modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            staging(Modifier.weight(0.6f))

            val configScrollState = rememberScrollState()

            Box(modifier = Modifier.weight(0.4f).fillMaxHeight()) {
                // The scrollable content
                configuration(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(configScrollState)
                        .padding(),
                )

                // The Desktop Scrollbar
                VerticalScrollbar(
                    modifier =
                        Modifier
                            .align(BiasAlignment(1.05f, 0f))
                            .fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(configScrollState),
                )
            }
        }
    }
}
