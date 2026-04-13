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
import com.drewjya.pdfmaster.helper.DatePattern
import com.drewjya.pdfmaster.helper.PageFormat
import com.drewjya.pdfmaster.helper.Position
import com.drewjya.pdfmaster.helper.formatDate
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.path
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.koin.compose.koinInject

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationContent(
    screen: Screen,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    val pdfViewModel: PdfViewModel = koinInject()
//    var inputDir by mutableStateOf("")

    val pickerFile =
        rememberFilePickerLauncher(
            type = FileKitType.File(extensions = setOf("pdf")),
            mode = FileKitMode.Single,
            onResult = { file ->
                if (file != null) {
                    pdfViewModel.setName(file.file.name)
                    pdfViewModel.setDirectory(file.file.parent)
                }
            },
        )

    val inputDirectory =
        rememberDirectoryPickerLauncher(
            onResult = { directory ->
                if (directory != null) {
                    println("Selected directory: ${directory.path}")
                    pdfViewModel.inputDirectory.value = directory.path
                    val pdfFiles =
                        directory.file
                            .listFiles { file ->
                                file.isFile && file.extension.equals("pdf", ignoreCase = true)
                            }.orEmpty()
                            .toList()
                    val files = pdfFiles.filter { it.name.contains("-") }
                    pdfViewModel.addFiles(files)
                }
            },
        )

    val pickerDirectory =
        rememberDirectoryPickerLauncher(
            onResult = { directory ->
                if (directory != null) {
                    if (screen == Screen.Files) {
                        pdfViewModel.monthlyDirectory.value = directory.path
                    } else {
                        pdfViewModel.setDirectory(directory.path)
                    }
                }
            },
        )

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
                if (screen == Screen.Files) {
                    InputButton(
                        label = "INPUT",
                        text = pdfViewModel.inputDirectory.value,
                        onValueChange = {
                            pdfViewModel.inputDirectory.value = it
                        },
                        onClick = { inputDirectory.launch() },
                        icon = AppIcon.Folder,
                    )
                }
                InputButton(
                    label = "OUTPUT",
                    text = (if (screen == Screen.Files) pdfViewModel.monthlyDirectory else pdfViewModel.selectedDirectory).value,
                    onValueChange = {
                        if (screen == Screen.Files) {
                            pdfViewModel.monthlyDirectory.value = it
                        } else {
                            pdfViewModel.setDirectory(it)
                        }
                    },
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

                if (screen in listOf(Screen.Numbering, Screen.Watermark)) {
                    DropdownSelector(
                        selectedItem = pdfViewModel.font.value,
                        label = "FONT",
                        items = Standard14Fonts.FontName.entries,
                        getLabel = { it.name },
                        onItemSelected = { pdfViewModel.font.value = it },
                    )

                    TextInput(
                        label = "SIZE",
                        text =
                            (
                                if (screen ==
                                    Screen.Watermark
                                ) {
                                    pdfViewModel.watermarkFontSize.value
                                } else {
                                    pdfViewModel.numberingFontSize.value
                                }
                            ).toString(),
                        onValueChange = { text ->
                            val state =
                                (if (screen == Screen.Watermark) pdfViewModel.watermarkFontSize else pdfViewModel.numberingFontSize)
                            if (text.isBlank()) {
                                state.value = 0
                            } else {
                                text.toIntOrNull()?.let { state.value = it }
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
                        DropdownSelector(
                            "POSITION",
                            items = Position.entries,
                            selectedItem = pdfViewModel.position.value,
                            onItemSelected = { pdfViewModel.position.value = it },
                            getLabel = { it.name },
                        )
                    }

                    if (screen == Screen.Numbering) {
                        DropdownSelector(
                            "NUMBERING FORMAT",
                            items = PageFormat.entries,
                            selectedItem = pdfViewModel.pageFormat.value,
                            onItemSelected = { pdfViewModel.pageFormat.value = it },
                            getLabel = { it.name + " (${it.value})".replace("{page}", "1").replace("{total}", "10") },
                        )
                        MarginPdf(
                            x = pdfViewModel.x.value,
                            y = pdfViewModel.y.value,
                            onXChange = { pdfViewModel.x.value = it },
                            onYChange = { pdfViewModel.y.value = it },
                        )
                    }

                    if (screen == Screen.Watermark) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ColorPicker(controller)
                            AppSlider(
                                value = pdfViewModel.opacity.value,
                                onValueChange = { pdfViewModel.opacity.value = it },
                                label = "OPACITY",
                            )
                        }
                    }
                }

                DropdownSelector(
                    "FORMATTER",
                    selectedItem = pdfViewModel.dateFormat.value,
                    onItemSelected = {
                        pdfViewModel.dateFormat.value = it
                    },
                    items = DatePattern.entries,
                    getLabel = { value ->

                        value.pattern + " - " + formatDate(pdfViewModel.selectedDate.value, value)
                    },
                )
                DateSelector(label = "DATE")
            }

            EXPANDED -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (screen == Screen.Files) {
                        InputButton(
                            modifier = Modifier.weight(1f),
                            label = "INPUT",
                            text = pdfViewModel.inputDirectory.value,
                            onValueChange = {
                                pdfViewModel.inputDirectory.value = it
                            },
                            onClick = { inputDirectory.launch() },
                            icon = AppIcon.Folder,
                        )
                    }
                    InputButton(
                        modifier = Modifier.weight(1f),
                        label = "OUTPUT",
                        text = (if (screen == Screen.Files) pdfViewModel.monthlyDirectory else pdfViewModel.selectedDirectory).value,
                        onValueChange = {
                            if (screen == Screen.Files) {
                                pdfViewModel.monthlyDirectory.value = it
                            } else {
                                pdfViewModel.setDirectory(it)
                            }
                        },
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
                        DropdownSelector(
                            "NUMBERING FORMAT",
                            modifier = Modifier.weight(1f),
                            items = PageFormat.entries,
                            selectedItem = pdfViewModel.pageFormat.value,
                            onItemSelected = { pdfViewModel.pageFormat.value = it },
                            getLabel = { it.name + " (${it.value})".replace("{page}", "1").replace("{total}", "10") },
                        )
                    }
                }

                if (screen in listOf(Screen.Numbering, Screen.Watermark)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val pdfViewModel: PdfViewModel = koinInject()

                        DropdownSelector(
                            modifier = Modifier.weight(1f),
                            selectedItem = pdfViewModel.font.value,
                            label = "FONT",
                            items = Standard14Fonts.FontName.entries,
                            getLabel = { it.name },
                            onItemSelected = { pdfViewModel.font.value = it },
                        )
                        TextInput(
                            modifier = Modifier.weight(1f),
                            label = "SIZE",
                            text =
                                (
                                    if (screen ==
                                        Screen.Watermark
                                    ) {
                                        pdfViewModel.watermarkFontSize.value
                                    } else {
                                        pdfViewModel.numberingFontSize.value
                                    }
                                ).toString(),
                            onValueChange = { text ->
                                val state =
                                    (if (screen == Screen.Watermark) pdfViewModel.watermarkFontSize else pdfViewModel.numberingFontSize)
                                if (text.isBlank()) {
                                    state.value = 0
                                } else {
                                    text.toIntOrNull()?.let { state.value = it }
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
                            DropdownSelector(
                                modifier = Modifier.weight(1f),
                                label = "POSITION",
                                items = Position.entries,
                                selectedItem = pdfViewModel.position.value,
                                onItemSelected = { pdfViewModel.position.value = it },
                                getLabel = { it.name },
                            )
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

                    if (screen == Screen.Watermark) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ColorPicker(controller, modifier = Modifier.weight(1f))
                            AppSlider(
                                modifier = Modifier.weight(1f),
                                value = pdfViewModel.opacity.value,
                                onValueChange = { pdfViewModel.opacity.value = it },
                                label = "OPACITY",
                            )
                        }
                    }
                }

                if (screen == Screen.Files) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DropdownSelector(
                            "FORMATTER",
                            modifier = Modifier.weight(1f),
                            selectedItem = pdfViewModel.dateFormat.value,
                            onItemSelected = {
                                pdfViewModel.dateFormat.value = it
                            },
                            items = DatePattern.entries,
                            getLabel = { value ->

                                value.pattern + " - " + formatDate(pdfViewModel.selectedDate.value, value)
                            },
                        )
                        DateSelector(label = "DATE", modifier = Modifier.weight(1f))
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
