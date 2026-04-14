package com.drewjya.pdfmaster

import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.graphics.Color
import com.drewjya.pdfmaster.components.AppSnackbar
import com.drewjya.pdfmaster.components.MainContent
import com.drewjya.pdfmaster.components.Sidebar
import com.drewjya.pdfmaster.updater.AppUpdater
import com.drewjya.pdfmaster.viewmodel.PdfViewModel

import java.awt.datatransfer.DataFlavor
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.time.Duration.Companion.hours

private val Slate50 = Color(0xFFF8FAFC)

fun startBackgroundUpdateCheck(updater: AppUpdater, scope: CoroutineScope) {
    scope.launch {
        while (true) {
            updater.checkForUpdate()
            delay(24.hours)
        }
    }
}

@Composable
fun App() {
    val updater: AppUpdater = koinInject()

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        startBackgroundUpdateCheck(updater, scope)
    }
    val viewModel: PdfViewModel = koinInject()

    var currentScreen by remember { mutableStateOf(Screen.Merge) }
    var isExpanded by remember { mutableStateOf(true) }


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
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .fillMaxSize()
                    .background(Slate50)
                    .dragAndDropTarget(
                        shouldStartDragAndDrop = { true },
                        target = dragAndDropTarget,
                    ),
            ) {
                Sidebar(
                    isExpanded = isExpanded,
                    currentScreen = currentScreen,
                    onScreenSelected = { currentScreen = it },
                    onToggleExpand = { isExpanded = !isExpanded },
                )
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    MainContent(currentScreen)
                }
            }



            if (viewModel.isDragging.value) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f)),
                    // Dim the background
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
