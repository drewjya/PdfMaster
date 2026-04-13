package com.drewjya.pdfmaster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.core.layout.WindowWidthSizeClass.Companion.COMPACT
import androidx.window.core.layout.WindowWidthSizeClass.Companion.EXPANDED
import androidx.window.core.layout.WindowWidthSizeClass.Companion.MEDIUM
import com.drewjya.pdfmaster.Screen
import com.drewjya.pdfmaster.design.AppIcon
import com.drewjya.pdfmaster.design.Slate100
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import io.github.vinceglb.filekit.dialogs.compose.PickerResultLauncher
import org.koin.compose.koinInject

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationContent(
    screen: Screen,
    windowWidthSizeClass: WindowWidthSizeClass,
    pickerFile: PickerResultLauncher,
    pickerDirectory: PickerResultLauncher,
) {
    val pdfViewModel: PdfViewModel = koinInject()
    val controller = rememberColorPickerController()

    LaunchedEffect(Unit) {
        controller.wheelRadius = 5.dp
        controller.selectByColor(Color.Black, true)
    }

    LaunchedEffect(controller.selectedColor.value) {
        pdfViewModel.color.value = controller.selectedColor.value
    }

    Column(
        modifier =
            Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxSize()
                .background(Slate100)
                .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        when (windowWidthSizeClass) {
            COMPACT, MEDIUM -> {
                InputButton(
                    label = "OUTPUT",
                    text = pdfViewModel.selectedDirectory.value,
                    onValueChange = { pdfViewModel.setDirectory(it) },
                    onClick = { pickerDirectory.launch() },
                    icon = AppIcon.Folder,
                )

                if (screen == Screen.Merge) {
                    InputButton(
                        label = "NAME",
                        text = pdfViewModel.selectedName.value,
                        onValueChange = { pdfViewModel.setName(it) },
                        onClick = { pickerFile.launch() },
                        icon = AppIcon.File,
                    )
                }

                if (screen == Screen.Watermark) {
                    TextInput(
                        label = "WATERMARK TEXT",
                        text = pdfViewModel.watermarkText.value,
                        onValueChange = { pdfViewModel.watermarkText.value = it },
                        placeholder = "Input watermark",
                    )
                }

                if (screen != Screen.Merge) {
                    FontSelector()

                    TextInput(
                        label = "SIZE",
                        text = pdfViewModel.watermarkFontSize.value.toString(),
                        onValueChange = { text ->
                            if (text.isBlank()) {
                                pdfViewModel.watermarkFontSize.value = 0
                            } else {
                                text.toIntOrNull()?.let { pdfViewModel.watermarkFontSize.value = it }
                            }
                        },
                        placeholder = "Add font size",
                    )

                    if (screen == Screen.Watermark) {
                        TextInput(
                            label = "ROTATION",
                            text = pdfViewModel.rotation.value.toString(),
                            onValueChange = { text ->
                                if (text.isBlank()) {
                                    pdfViewModel.rotation.value = 0.0
                                } else {
                                    text.toDoubleOrNull()?.let { pdfViewModel.rotation.value = it }
                                }
                            },
                            placeholder = "Input rotation",
                        )
                        DropdownSelector("POSITION")
                    }

                    if (screen == Screen.Numbering) {
                        DropdownSelector("NUMBERING FORMAT")
                        MarginPdf(
                            x = pdfViewModel.x.value,
                            y = pdfViewModel.y.value,
                            onXChange = { pdfViewModel.x.value = it },
                            onYChange = { pdfViewModel.y.value = it },
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ColorPicker(controller)
                        if (screen == Screen.Watermark) {
                            AppSlider(
                                value = pdfViewModel.opacity.value,
                                onValueChange = { pdfViewModel.opacity.value = it },
                                label = "OPACITY",
                            )
                        }
                    }
                }
            }

            EXPANDED -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    InputButton(
                        modifier = Modifier.weight(1f),
                        label = "OUTPUT",
                        text = pdfViewModel.selectedDirectory.value,
                        onValueChange = { pdfViewModel.setDirectory(it) },
                        onClick = { pickerDirectory.launch() },
                        icon = AppIcon.Folder,
                    )

                    if (screen == Screen.Merge) {
                        InputButton(
                            modifier = Modifier.weight(1f),
                            label = "NAME",
                            text = pdfViewModel.selectedName.value,
                            onValueChange = { pdfViewModel.setName(it) },
                            onClick = { pickerFile.launch() },
                            icon = AppIcon.File,
                        )
                    }

                    if (screen == Screen.Watermark) {
                        TextInput(
                            modifier = Modifier.weight(1f),
                            label = "WATERMARK TEXT",
                            text = pdfViewModel.watermarkText.value,
                            onValueChange = { pdfViewModel.watermarkText.value = it },
                            placeholder = "Input watermark",
                        )
                    }

                    if (screen == Screen.Numbering) {
                        DropdownSelector("NUMBERING FORMAT", modifier = Modifier.weight(1f))
                    }
                }

                if (screen != Screen.Merge) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FontSelector(modifier = Modifier.weight(1f))
                        TextInput(
                            modifier = Modifier.weight(1f),
                            label = "SIZE",
                            text = pdfViewModel.watermarkFontSize.value.toString(),
                            onValueChange = { text ->
                                if (text.isBlank()) {
                                    pdfViewModel.watermarkFontSize.value = 0
                                } else {
                                    text.toIntOrNull()?.let { pdfViewModel.watermarkFontSize.value = it }
                                }
                            },
                            placeholder = "Add font size",
                        )
                    }

                    if (screen == Screen.Watermark) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextInput(
                                modifier = Modifier.weight(1f),
                                label = "ROTATION",
                                text = pdfViewModel.rotation.value.toString(),
                                onValueChange = { text ->
                                    if (text.isBlank()) {
                                        pdfViewModel.rotation.value = 0.0
                                    } else {
                                        text.toDoubleOrNull()?.let { pdfViewModel.rotation.value = it }
                                    }
                                },
                                placeholder = "Input rotation",
                            )
                            DropdownSelector("POSITION", modifier = Modifier.weight(1f))
                        }
                    }

                    if (screen == Screen.Numbering) {
                        MarginPdf(
                            x = pdfViewModel.x.value,
                            y = pdfViewModel.y.value,
                            onXChange = { pdfViewModel.x.value = it },
                            onYChange = { pdfViewModel.y.value = it },
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ColorPicker(controller, modifier = Modifier.weight(1f))
                        if (screen == Screen.Watermark) {
                            AppSlider(
                                modifier = Modifier.weight(1f),
                                value = pdfViewModel.opacity.value,
                                onValueChange = { pdfViewModel.opacity.value = it },
                                label = "OPACITY",
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MarginPdf(
    x: String,
    y: String,
    onXChange: (String) -> Unit,
    onYChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TextInput(
            modifier = Modifier.weight(1f),
            label = "VERTICAL",
            text = x,
            onValueChange = { text ->
                if (text.isBlank()) {
                    onXChange("0")
                } else {
                    text.toBigDecimalOrNull()?.let { onXChange(text) }
                }
            },
            placeholder = "0",
        )
        TextInput(
            modifier = Modifier.weight(1f),
            label = "HORIZONTAL",
            text = y,
            onValueChange = { text ->
                if (text.isBlank()) {
                    onYChange("0")
                } else {
                    text.toBigDecimalOrNull()?.let { onYChange(text) }
                }
            },
            placeholder = "0",
        )
    }
}
