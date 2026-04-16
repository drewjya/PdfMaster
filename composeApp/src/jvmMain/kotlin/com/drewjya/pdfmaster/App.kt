package com.drewjya.pdfmaster

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.graphics.Color
import com.drewjya.pdfmaster.components.AppSnackbar
import com.drewjya.pdfmaster.components.Root
import com.drewjya.pdfmaster.design.AppTheme
import com.drewjya.pdfmaster.updater.AppUpdater
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import java.awt.datatransfer.DataFlavor
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.hours


fun startBackgroundUpdateCheck(
    updater: AppUpdater,
    scope: CoroutineScope,
) {
    scope.launch {
        while (true) {
            updater.checkForUpdate()
            delay(24.hours)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App(
    updater: AppUpdater = koinInject(),
    appTheme: AppTheme = koinInject(),
    viewModel: PdfViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        startBackgroundUpdateCheck(updater, scope)
    }

    val dragAndDropTarget =
        remember {
            object : DragAndDropTarget {
                override fun onStarted(event: DragAndDropEvent) {
                    viewModel.setDragging(true)
                }

                override fun onEnded(event: DragAndDropEvent) {
                    viewModel.setDragging(false)
                }

                override fun onDrop(event: DragAndDropEvent): Boolean {
                    @OptIn(ExperimentalComposeUiApi::class)
                    val transferable = event.awtTransferable
                    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        val data = transferable.getTransferData(DataFlavor.javaFileListFlavor)
                        val files = (data as? List<*>)?.filterIsInstance<File>() ?: emptyList()
                        viewModel.addFiles(files.filter { it.extension == "pdf" })
                        return true
                    }
                    return false
                }
            }
        }
    MaterialTheme(
        colorScheme =
            lightColorScheme(
                primary = appTheme.primary,
                onPrimary = Color.White,
                surface = appTheme.surface,
                onSurface = appTheme.onSurface,
                background = appTheme.neutral,
                onBackground = appTheme.onSurface,
            ),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier =
                    Modifier.fillMaxSize().dragAndDropTarget(
                        shouldStartDragAndDrop = { true },
                        target = dragAndDropTarget,
                    ),
                content = { Root(appTheme) },
            )

            if (viewModel.isDragging.value) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Drop PDFs Here",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            AppSnackbar(modifier = Modifier.align(Alignment.BottomEnd))
        }
    }
}
