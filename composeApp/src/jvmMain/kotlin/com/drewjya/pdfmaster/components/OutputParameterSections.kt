package com.drewjya.pdfmaster.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.drewjya.pdfmaster.design.AppIcon
import com.drewjya.pdfmaster.design.AppTheme
import com.drewjya.pdfmaster.helper.DatePattern
import com.drewjya.pdfmaster.helper.EnhancementType
import com.drewjya.pdfmaster.helper.PageFormat
import com.drewjya.pdfmaster.helper.Position
import com.drewjya.pdfmaster.helper.ProcessMode
import com.drewjya.pdfmaster.hooks.rememberAppInput
import com.drewjya.pdfmaster.viewmodel.ConfigViewModel
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import java.math.BigDecimal
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OutputParameterSections(
    modifier: Modifier,
    configViewModel: ConfigViewModel = koinViewModel(),
    pdfViewModel: PdfViewModel = koinViewModel(),
) {
    val appTheme: AppTheme = koinInject()
    val activeConfig by configViewModel.activeConfig.collectAsStateWithLifecycle(initialValue = null)
    val fonts by pdfViewModel.fonts.collectAsStateWithLifecycle(initialValue = null)
    val clip = RoundedCornerShape(8.dp)

    Column(
        modifier =
            modifier
                .clip(clip)
                .background(appTheme.surfaceAlt.copy(alpha = 0.5f), clip)
                .border(1.dp, appTheme.secondary.copy(alpha = 0.2f), clip)
                .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CardWrapper { expanded, onExpand ->
            val active = activeConfig?.mode == ProcessMode.Batch

            CardTitle(
                title = "Batch",
                icon = AppIcon.Batch,
                active = active,
                onToggle = {
                    if (it) {
                        configViewModel.updateProcessMode(ProcessMode.Batch)
                    } else {
                        configViewModel.updateProcessMode(ProcessMode.None)
                    }
                },
                expanded = expanded,
                onExpand = onExpand,
            )

            ConfigWrapper(active = active, expanded = expanded) {
                val batchSettings = activeConfig?.batchSettings
                val (format, setFormat) =
                    rememberAppInput(
                        externalValue = batchSettings?.format,
                        onValueChange = { newValue -> configViewModel.updateBatchSettings { it.copy(format = newValue) } },
                    )
                val (variable, setVariable) =
                    rememberAppInput(
                        externalValue = batchSettings?.variable,
                        onValueChange = { newValue -> configViewModel.updateBatchSettings { it.copy(variable = newValue) } },
                    )

                val (separator, setSeparator) =
                    rememberAppInput(
                        externalValue = batchSettings?.separator,
                        onValueChange = { newValue -> configViewModel.updateBatchSettings { it.copy(separator = newValue) } },
                    )

                var textFieldValue by remember {
                    mutableStateOf(TextFieldValue(""))
                }

                DynamicPosition(maxItemsInEachRow = { if (it > 350.dp) 2 else 1 }) {
                    DropdownPicker(
                        modifier = Modifier.weight(1f),
                        enabled = active,
                        label = "DATE FORMAT",
                        value = batchSettings?.dateFormat,
                        onSelected = { value ->
                            configViewModel.updateBatchSettings { it.copy(dateFormat = value) }
                        },
                        placeholder = "Select date format",
                        items = DatePattern.entries,
                        getLabel = { it.pattern },
                    )

                    InputDatePicker(
                        modifier = Modifier.weight(1f),
                        enabled = active,
                        label = "SELECT DATE",
                        value = batchSettings?.selectedDate,
                        onTap = { selected ->
                            configViewModel.updateBatchSettings { it.copy(selectedDate = selected) }
                        },
                    )

                    Input(
                        modifier = Modifier.weight(1f),
                        label = "FORMAT",
                        enabled = active,
                        value = format,
                        onValueChange = setFormat,
                    )
                    Input(
                        modifier = Modifier.weight(1f),
                        label = "SEPARATOR",
                        enabled = active,
                        value = separator,
                        onValueChange = setSeparator,
                    )
                    Input(
                        modifier = Modifier.weight(1f),
                        label = "VARIABLE",
                        enabled = active,
                        value = variable,
                        onValueChange = setVariable,
                    )

                    InputIcon(
                        modifier = Modifier.weight(1f),
                        label = "LIST ORDER",
                        enabled = active,
                        value = textFieldValue,
                        onValueChange = { newValue -> textFieldValue = newValue },
                        placeholder = "Input list order",
                        icon = Icons.AutoMirrored.Filled.List,
                        onIconClick = {

                            configViewModel.updateBatchSettings {
                                it.copy(
                                    listPrefixOrder = (it.listPrefixOrder + textFieldValue.text).distinct()
                                )
                            }

                            textFieldValue = TextFieldValue("")
                        },
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 3,
                    ) {
                        val prefixes = batchSettings?.listPrefixOrder.orEmpty()
                        prefixes.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier.height(32.dp)
                                    .background(appTheme.surfaceAlt)
                                    .clip(RoundedCornerShape(4.dp)),
                            ) {
                                Box(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "${index + 1}. " + item,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = appTheme.onSurface,
                                    )
                                }

                                IconButton(
                                    clip = RoundedCornerShape(0.dp),
                                    boxSize = 32.dp,
                                    onClick = {
                                        configViewModel.updateBatchSettings {
                                            it.copy(
                                                listPrefixOrder = it.listPrefixOrder.toMutableList().apply {
                                                    removeAt(index)
                                                }
                                            )
                                        }
                                    },
                                    color = appTheme.error,
                                    icon = AppIcon.Trash,
                                )


                            }
                        }
                    }

                }
            }
        }
        CardWrapper { expanded, onExpand ->
            val active = activeConfig?.mode == ProcessMode.Merge
            val mergeSettings = activeConfig?.mergeSettings
            val (name, setName) =
                rememberAppInput(
                    externalValue = mergeSettings?.mergeName,
                    onValueChange = { newValue -> configViewModel.updateMergeSettings { it.copy(mergeName = newValue) } },
                )
            CardTitle(
                title = "Merge",
                icon = AppIcon.Merge,
                expanded = expanded,
                onExpand = onExpand,
                active = active,
                onToggle = {
                    if (it) {
                        configViewModel.updateProcessMode(ProcessMode.Merge)
                    } else {
                        configViewModel.updateProcessMode(ProcessMode.None)
                    }
                },
            )
            ConfigWrapper(active = active, expanded = expanded) {
                val pickerFile =
                    rememberFilePickerLauncher(
                        type = FileKitType.File(extensions = setOf("pdf")),
                        mode = FileKitMode.Single,
                        onResult = { file ->
                            if (file != null) {
                                val fileName = file.file.name
                                configViewModel.updateMergeSettings { it.copy(mergeName = fileName) }
                            }
                        },
                    )

                InputIcon(
                    enabled = active,
                    label = "MERGE NAME",
                    onValueChange = setName,
                    onIconClick = {
                        pickerFile.launch()
                    },
                    value = name,
                    icon = AppIcon.FileAdd,
                )
            }
        }
        CardWrapper { expanded, onExpand ->

            val active = activeConfig?.activeEnhancements?.contains(EnhancementType.Identity) ?: false
            val identitySettings = activeConfig?.identitySettings
            val controller = rememberColorPickerController()
            val (watermark, setWatermark) =
                rememberAppInput(
                    externalValue = identitySettings?.text,
                    onValueChange = { newValue -> configViewModel.updateIdentitySettings { it.copy(text = newValue) } },
                )
            val (opacity, setOpacity) =
                rememberAppInput(
                    externalValue = identitySettings?.opacity?.run { (this * 100).toInt() }?.toString() ?: "0",
                    onValueChange = { str ->
                        val value = str.toBigDecimalOrNull()
                        if (value != null) {
                            val clamped = value.coerceIn(BigDecimal("0"), BigDecimal("100"))
                            configViewModel.updateIdentitySettings {
                                it.copy(opacity = clamped.toFloat() / 100f) // ← fix here
                            }
                        } else {
                            if (str.trim().isBlank()) {
                                configViewModel.updateIdentitySettings { it.copy(opacity = 0f) }
                            }
                        }
                    },
                )

            val (rotation, setRotation) =
                rememberAppInput(
                    externalValue = identitySettings?.rotation?.toString(),
                    onValueChange = { str ->
                        val value = str.toBigDecimalOrNull()

                        if (value != null) {
                            val clamped = value.coerceIn(BigDecimal("0"), BigDecimal("360"))
                            configViewModel.updateIdentitySettings { it.copy(rotation = clamped.toInt()) }
                        } else if (str.trim().isBlank()) {
                            configViewModel.updateIdentitySettings { it.copy(rotation = 0) }
                        }
                    },
                )

            val (fontSize, setFontSize) =
                rememberAppInput(
                    externalValue = identitySettings?.fontSize?.toString(),
                    onValueChange = { str ->
                        val value = str.toBigDecimalOrNull()

                        if (value != null) {
                            val clamped = value.coerceIn(BigDecimal("1"), BigDecimal("120"))
                            configViewModel.updateIdentitySettings { it.copy(fontSize = clamped.toInt()) }
                        } else if (str.trim().isBlank()) {
                            configViewModel.updateIdentitySettings { it.copy(fontSize = 1) }
                        }
                    },
                )
            CardTitle(
                title = "Identity",
                icon = AppIcon.Identity,
                expanded = expanded,
                onExpand = onExpand,
                active = active,
                onToggle = { configViewModel.toggleEnhancement(EnhancementType.Identity) },
            )
            ConfigWrapper(active = active, expanded = expanded) {
                Input(
                    value = watermark,
                    onValueChange = setWatermark,
                    label = "WATERMARK TEXT",
                    placeholder = "Input watermark",
                    enabled = active,
                )

                DynamicPosition(maxItemsInEachRow = { if (it > 350.dp) 2 else 1 }) {
                    DropdownPicker(
                        modifier = Modifier.weight(1f),
                        enabled = active,
                        label = "POSITION",
                        value = identitySettings?.position,
                        onSelected = { value -> configViewModel.updateIdentitySettings { it.copy(position = value) } },
                        placeholder = "Select position",
                        items = Position.entries,
                        getLabel = { it.name },
                    )
                    DropdownPicker(
                        modifier = Modifier.weight(1f),
                        enabled = active,
                        label = "FONT",
                        value = identitySettings?.fontName,
                        onSelected = { value -> configViewModel.updateIdentitySettings { it.copy(fontName = value) } },
                        placeholder = "Select font",
                        items = fonts?.toList() ?: emptyList(),
                        getLabel = { it },
                    )

                    Input(
                        modifier = Modifier.weight(1f),
                        label = "FONT SIZE",
                        enabled = active,
                        value = fontSize,
                        onValueChange = setFontSize,
                    )

                    Input(
                        modifier = Modifier.weight(1f),
                        label = "ROTATION",
                        value = rotation,
                        enabled = active,
                        onValueChange = setRotation,
                    )

                    ColorPicker(
                        controller = controller,
                        modifier = Modifier.weight(1f),
                        color = identitySettings?.color ?: Color.Black,
                        onChanged = { color ->
                            configViewModel.updateIdentitySettings { it.copy(colorLong = color.value) }
                        },
                    )
                    Input(
                        modifier = Modifier.weight(1f),
                        label = "OPACITY",
                        value = opacity,
                        enabled = active,
                        onValueChange = setOpacity,
                        placeholder = "Input opacity",
                    )
                }
            }
        }
        CardWrapper { expanded, onExpand ->
            val active = activeConfig?.activeEnhancements?.contains(EnhancementType.Numbering) ?: false
            val numberingSettings = activeConfig?.numberingSettings
            val controller = rememberColorPickerController()

            val (fontSize, setFontSize) =
                rememberAppInput(
                    externalValue = numberingSettings?.fontSize?.toString(),
                    onValueChange = { str ->
                        val value = str.toBigDecimalOrNull()

                        if (value != null) {
                            val clamped = value.coerceIn(BigDecimal("1"), BigDecimal("120"))
                            configViewModel.updateNumberingSettings { it.copy(fontSize = clamped.toInt()) }
                        } else if (str.trim().isBlank()) {
                            configViewModel.updateNumberingSettings { it.copy(fontSize = 1) }
                        }
                    },
                )

            val (x, setX) =
                rememberAppInput(
                    externalValue = numberingSettings?.x?.toString() ?: "0",
                    onValueChange = {
                        val value = it.toBigDecimalOrNull()
                        if (value != null) {
                            configViewModel.updateNumberingSettings { settings -> settings.copy(x = value.toInt()) }
                        } else if (it.trim().isBlank()) {
                            configViewModel.updateNumberingSettings { settings -> settings.copy(x = 0) }
                        }
                    },
                )

            val (y, setY) =
                rememberAppInput(
                    externalValue = numberingSettings?.y?.toString() ?: "0",
                    onValueChange = {
                        val value = it.toBigDecimalOrNull()
                        if (value != null) {
                            configViewModel.updateNumberingSettings { settings -> settings.copy(y = value.toInt()) }
                        } else if (it.trim().isBlank()) {
                            configViewModel.updateNumberingSettings { settings -> settings.copy(y = 0) }
                        }
                    },
                )
            CardTitle(
                title = "Numbering",
                icon = AppIcon.Numbering,
                expanded = expanded,
                onExpand = onExpand,
                active = active,
                onToggle = {
                    configViewModel.toggleEnhancement(EnhancementType.Numbering)
                },
            )
            ConfigWrapper(active = active, expanded = expanded) {
                DynamicPosition(maxItemsInEachRow = { if (it > 350.dp) 2 else 1 }) {
                    DropdownPicker(
                        modifier = Modifier.weight(1f),
                        enabled = active,
                        label = "PAGE FORMAT",
                        value = numberingSettings?.format,
                        onSelected = { configViewModel.updateNumberingSettings { settings -> settings.copy(format = it) } },
                        placeholder = "Select date format",
                        items = PageFormat.entries,
                        getLabel = { it.value },
                    )
                    DropdownPicker(
                        modifier = Modifier.weight(1f),
                        enabled = active,
                        label = "FONT",
                        value = numberingSettings?.fontName,
                        onSelected = {
                            configViewModel.updateNumberingSettings { settings -> settings.copy(fontName = it) }
                        },
                        placeholder = "Select font",
                        items = fonts?.toList() ?: emptyList(),
                        getLabel = { it },
                    )

                    Input(
                        modifier = Modifier.weight(1f),
                        label = "FONT SIZE",
                        value = fontSize,
                        onValueChange = setFontSize,
                    )

                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Input(
                            modifier = Modifier.weight(1f),
                            label = "X",
                            value = x,
                            onValueChange = setX,
                        )
                        Input(
                            modifier = Modifier.weight(1f),
                            label = "Y",
                            value = y,
                            onValueChange = setY,
                        )
                    }
                    ColorPicker(
                        controller = controller,
                        modifier = Modifier.weight(1f),
                        color = numberingSettings?.color ?: Color.Black,
                        onChanged = { color ->
                            configViewModel.updateNumberingSettings { settings -> settings.copy(colorLong = color.value) }
                        },
                    )

                    Box(modifier = Modifier.weight(1f)) {}
                }
            }
        }
    }
}

@Composable
fun CardWrapper(
    modifier: Modifier = Modifier,
    content: @Composable (expanded: Boolean, onExpand: (Boolean) -> Unit) -> Unit,
) {
    val appTheme: AppTheme = koinInject()
    val border = RoundedCornerShape(8.dp)
    val expanded = remember { mutableStateOf(true) }
    Box(
        modifier =
            modifier
                .dropShadow(
                    shape = border,
                    shadow =
                        Shadow(
                            radius = 1.dp,
                            spread = 1.dp,
                            color = appTheme.onSurfaceMuted.copy(alpha = 0.2f),
                            offset = DpOffset(x = 2.dp, 2.dp),
                        ),
                ).background(appTheme.surface, border),
    ) {
        Column(
            modifier = Modifier.clip(border),
        ) {
            content(expanded.value) {
                expanded.value = it
            }
        }
    }
}

@Composable
fun CardTitle(
    title: String,
    icon: ImageVector,
    expanded: Boolean = false,
    active: Boolean = false,
    onExpand: (Boolean) -> Unit = {},
    onToggle: (Boolean) -> Unit = {},
) {
    val appTheme: AppTheme = koinInject()
    Row(
        modifier =
            Modifier
                .background(
                    appTheme.surfaceAlt.copy(alpha = if (active) 0.5f else 0.2f),
                ).fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) { onExpand(!expanded) }
                .padding(vertical = 6.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val rotationState by animateFloatAsState(
                targetValue = if (expanded) 90f else 0f,
                animationSpec = spring(stiffness = Spring.StiffnessHigh),
                label = "ArrowRotation",
            )

            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = if (active) appTheme.primary else appTheme.onSurfaceMuted,
                modifier = Modifier.size(16.dp).rotate(rotationState),
            )

            AppIcon(
                icon = icon,
                backgroundColor = if (active) appTheme.primary.copy(alpha = 0.1f) else appTheme.surfaceAlt,
                color = if (active) appTheme.primary else appTheme.onSurfaceMuted,
            )

            Text(
                title,
                fontSize = 14.sp,
                color = if (active) appTheme.onSurface else appTheme.onSurfaceMuted,
                fontWeight = FontWeight.SemiBold,
            )
        }

        CustomSmallSwitch(
            checked = active,
            onCheckedChange = { value ->
                onToggle(value)
                if (value) {
                    onExpand(true)
                }
            },
        )
    }
}

@Composable
fun CustomSmallSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val width = 36.dp
    val height = 20.dp
    val thumbSize = 16.dp
    val appTheme: AppTheme = koinInject()
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) width - thumbSize - 2.dp else 2.dp,
    )

    val trackColor = if (checked) appTheme.primary else appTheme.surfaceAlt

    Box(
        modifier =
            Modifier
                .size(width, height)
                .clip(RoundedCornerShape(50.dp))
                .background(trackColor, RoundedCornerShape(50.dp))
                .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier =
                Modifier
                    .offset(x = thumbOffset)
                    .size(thumbSize)
                    .background(Color.White, CircleShape),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DynamicPosition(
    maxItemsInEachRow: (Dp) -> Int = { 2 },
    content: @Composable FlowRowScope.() -> Unit,
) {
    val density = LocalDensity.current
    // Initialize with a safe default so it doesn't crash on frame 1
    var containerWidth by remember { mutableStateOf(0.dp) }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This gives us the actual pixel width after measurement
                    containerWidth = with(density) { coordinates.size.width.toDp() }
                },
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            // Fallback to 1 if we haven't measured yet
            maxItemsInEachRow = if (containerWidth > 0.dp) maxItemsInEachRow(containerWidth) else 1,
        ) {
            content()
        }
    }
}

@Composable
fun ConfigWrapper(
    active: Boolean = true,
    expanded: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val appTheme: AppTheme = koinInject()

    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(appTheme.surfaceAlt.copy(alpha = if (active) 0.5f else 0.2f))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
        ) {
            content()
        }
    }
}
