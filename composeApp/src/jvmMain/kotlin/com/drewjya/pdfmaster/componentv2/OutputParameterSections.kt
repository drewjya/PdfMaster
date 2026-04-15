package com.drewjya.pdfmaster.componentv2

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.drewjya.pdfmaster.components.ColorPicker
import com.drewjya.pdfmaster.design.AppTheme
import com.drewjya.pdfmaster.design.Icons
import com.drewjya.pdfmaster.helper.DatePattern
import com.drewjya.pdfmaster.helper.EnhancementType
import com.drewjya.pdfmaster.helper.PageFormat
import com.drewjya.pdfmaster.helper.Position
import com.drewjya.pdfmaster.helper.ProcessMode
import com.drewjya.pdfmaster.viewmodel.ConfigViewModel
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import java.math.BigDecimal
import androidx.compose.material.icons.Icons as OldIcon

@Composable
fun OutputParameterSections(
    modifier: Modifier,
    viewModel: ConfigViewModel = koinViewModel(),
) {
    val appTheme: AppTheme = koinInject()
    val activeConfig by viewModel.activeConfig.collectAsStateWithLifecycle(initialValue = null)
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
                icon = Icons.Batch,
                active = active,
                onToggle = {
                    if (it) {
                        viewModel.updateProcessMode(ProcessMode.Batch)
                    } else {
                        viewModel.updateProcessMode(ProcessMode.None)
                    }
                },
                expanded = expanded,
                onExpand = onExpand,
            )

            ConfigWrapper(active = active, expanded = expanded) {
                val batchSettings = activeConfig?.batchSettings

                DynamicPosition(maxItemsInEachRow = { if (it > 350.dp) 2 else 1 }) {
                    DropdownPicker(
                        modifier = Modifier.weight(1f),
                        enabled = active,
                        label = "DATE FORMAT",
                        value = batchSettings?.dateFormat,
                        onSelected = { value ->
                            viewModel.updateBatchSettings { it.copy(dateFormat = value) }
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
                            viewModel.updateBatchSettings { it.copy(selectedDate = selected) }
                        },
                    )
                }
            }
        }
        CardWrapper { expanded, onExpand ->
            val active = activeConfig?.mode == ProcessMode.Merge
            val mergeSettings = activeConfig?.mergeSettings
            CardTitle(
                title = "Merge",
                icon = Icons.Merge,
                expanded = expanded,
                onExpand = onExpand,
                active = active,
                onToggle = {
                    if (it) {
                        viewModel.updateProcessMode(ProcessMode.Merge)
                    } else {
                        viewModel.updateProcessMode(ProcessMode.None)
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
                                viewModel.updateMergeSettings { it.copy(mergeName = fileName) }
                            }
                        },
                    )

                InputIcon(
                    enabled = active,
                    label = "MERGE NAME",
                    onValueChange = { value ->
                        viewModel.updateMergeSettings { it.copy(mergeName = value) }
                    },
                    onIconClick = {
                        pickerFile.launch()
                    },
                    text = mergeSettings?.mergeName ?: "",
                    icon = Icons.FileAdd,
                )
            }
        }
        CardWrapper { expanded, onExpand ->

            val active = activeConfig?.activeEnhancements?.contains(EnhancementType.Identity) ?: false
            val identitySettings = activeConfig?.identitySettings
            val controller = rememberColorPickerController()
            CardTitle(
                title = "Identity",
                icon = Icons.Identity,
                expanded = expanded,
                onExpand = onExpand,
                active = active,
                onToggle = { viewModel.toggleEnhancement(EnhancementType.Identity) },
            )
            ConfigWrapper(active = active, expanded = expanded) {
                Input(
                    text = identitySettings?.text ?: "",
                    onValueChange = { value -> viewModel.updateIdentitySettings { it.copy(text = value) } },
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
                        onSelected = { value -> viewModel.updateIdentitySettings { it.copy(position = value) } },
                        placeholder = "Select position",
                        items = Position.entries,
                        getLabel = { it.name },
                    )
                    DropdownPicker(
                        modifier = Modifier.weight(1f),
                        enabled = active,
                        label = "FONT",
                        value = identitySettings?.font,
                        onSelected = { value -> viewModel.updateIdentitySettings { it.copy(fontName = value.name) } },
                        placeholder = "Select font",
                        items = Standard14Fonts.FontName.entries,
                        getLabel = { it.name },
                    )

                    Input(
                        modifier = Modifier.weight(1f),
                        label = "FONT SIZE",
                        enabled = active,
                        text = identitySettings?.fontSize?.toString() ?: "1",
                        onValueChange = {
                            val value = it.toBigDecimalOrNull()

                            if (value != null) {
                                val clamped = value.coerceIn(BigDecimal("1"), BigDecimal("120"))
                                viewModel.updateIdentitySettings { it.copy(fontSize = clamped.toInt()) }
                            } else if (it.trim().isBlank()) {
                                viewModel.updateIdentitySettings { it.copy(fontSize = 1) }
                            }
                        },
                    )

                    Input(
                        modifier = Modifier.weight(1f),
                        label = "ROTATION",
                        text = identitySettings?.rotation?.toString() ?: "0",
                        enabled = active,
                        onValueChange = {
                            val value = it.toBigDecimalOrNull()

                            if (value != null) {
                                val clamped = value.coerceIn(BigDecimal("0"), BigDecimal("360"))
                                viewModel.updateIdentitySettings { it.copy(rotation = clamped.toInt()) }
                            } else if (it.trim().isBlank()) {
                                viewModel.updateIdentitySettings { it.copy(rotation = 0) }
                            }
                        },
                    )

                    ColorPicker(controller = controller, modifier = Modifier.weight(1f))
                    Input(
                        modifier = Modifier.weight(1f),
                        label = "OPACITY",
                        text =
                            identitySettings
                                ?.opacity
                                ?.run {
                                    (this * 100).toInt()
                                }?.toString() ?: "0",
                        enabled = active,
                        onValueChange = { str ->
                            val value = str.toBigDecimalOrNull()
                            println(str)
                            if (value != null) {
                                val clamped = value.coerceIn(BigDecimal("0"), BigDecimal("100"))
                                viewModel.updateIdentitySettings {
                                    it.copy(opacity = clamped.toFloat() / 100f) // ← fix here
                                }
                            } else {
                                if (str.trim().isBlank()) {
                                    viewModel.updateIdentitySettings { it.copy(opacity = 0f) }
                                }
                            }
                        },
                        placeholder = "Input opacity",
                    )
                }
            }
        }
        CardWrapper { expanded, onExpand ->
            val active = activeConfig?.activeEnhancements?.contains(EnhancementType.Numbering) ?: false
            val numberingSettings = activeConfig?.numberingSettings
            val controller = rememberColorPickerController()

            CardTitle(
                title = "Numbering",
                icon = Icons.Numbering,
                expanded = expanded,
                onExpand = onExpand,
                active = active,
                onToggle = {
                    viewModel.toggleEnhancement(EnhancementType.Numbering)
                },
            )
            ConfigWrapper(active = active, expanded = expanded) {
                DynamicPosition(maxItemsInEachRow = { if (it > 350.dp) 2 else 1 }) {
                    DropdownPicker(
                        modifier = Modifier.weight(1f),
                        enabled = active,
                        label = "PAGE FORMAT",
                        value = numberingSettings?.format,
                        onSelected = { viewModel.updateNumberingSettings { settings -> settings.copy(format = it) } },
                        placeholder = "Select date format",
                        items = PageFormat.entries,
                        getLabel = { it.value },
                    )
                    DropdownPicker(
                        modifier = Modifier.weight(1f),
                        enabled = active,
                        label = "FONT",
                        value = numberingSettings?.font,
                        onSelected = {
                            viewModel.updateNumberingSettings { settings -> settings.copy(fontName = it.name) }
                        },
                        placeholder = "Select font",
                        items = Standard14Fonts.FontName.entries,
                        getLabel = { it.name },
                    )

                    Input(
                        modifier = Modifier.weight(1f),
                        label = "FONT SIZE",
                        text = numberingSettings?.fontSize?.toString() ?: "1",
                        onValueChange = {
                            val value = it.toBigDecimalOrNull()

                            if (value != null) {
                                val clamped = value.coerceIn(BigDecimal("1"), BigDecimal("120"))
                                viewModel.updateNumberingSettings { settings -> settings.copy(fontSize = clamped.toInt()) }
                            } else if (it.trim().isBlank()) {
                                viewModel.updateNumberingSettings { settings -> settings.copy(fontSize = 1) }
                            }
                        },
                    )

                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Input(
                            modifier = Modifier.weight(1f),
                            label = "X",
                            text = numberingSettings?.x?.toString() ?: "0",
                            onValueChange = {
                                val value = it.toBigDecimalOrNull()
                                if (value != null) {
                                    viewModel.updateNumberingSettings { settings -> settings.copy(x = value.toInt()) }
                                } else if (it.trim().isBlank()) {
                                    viewModel.updateNumberingSettings { settings -> settings.copy(x = 0) }
                                }
                            },
                        )
                        Input(
                            modifier = Modifier.weight(1f),
                            label = "Y",
                            text = numberingSettings?.y?.toString() ?: "0",
                            onValueChange = {
                                val value = it.toBigDecimalOrNull()
                                if (value != null) {
                                    viewModel.updateNumberingSettings { settings -> settings.copy(y = value.toInt()) }
                                } else if (it.trim().isBlank()) {
                                    viewModel.updateNumberingSettings { settings -> settings.copy(y = 0) }
                                }
                            },
                        )
                    }
                    ColorPicker(controller = controller, modifier = Modifier.weight(1f))
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
            content(expanded.value, { expanded.value = it })
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
                imageVector = OldIcon.AutoMirrored.Default.KeyboardArrowRight,
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
