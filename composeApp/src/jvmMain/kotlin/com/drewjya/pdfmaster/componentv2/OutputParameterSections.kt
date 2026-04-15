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
import com.drewjya.pdfmaster.components.ColorPicker
import com.drewjya.pdfmaster.design.AppTheme
import com.drewjya.pdfmaster.design.Icons
import com.drewjya.pdfmaster.helper.DatePattern
import com.drewjya.pdfmaster.helper.PageFormat
import com.drewjya.pdfmaster.helper.Position
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.koin.compose.koinInject
import java.math.BigDecimal
import androidx.compose.material.icons.Icons as OldIcon

@Composable
fun OutputParameterSections(modifier: Modifier) {
    val appTheme: AppTheme = koinInject()

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
            val active = remember { mutableStateOf(true) }

            CardTitle(
                title = "Batch",
                icon = Icons.Batch,
                active = active.value,
                onToggle = { active.value = it },
                expanded = expanded,
                onExpand = onExpand,
            )

            ConfigWrapper(active = active.value, expanded = expanded) {
                val value = remember { mutableStateOf<Long?>(null) }
                val format = remember { mutableStateOf<DatePattern?>(null) }
                DynamicPosition(maxItemsInEachRow = { if (it > 350.dp) 2 else 1 }) {
                    DropdownPicker(
                        modifier = Modifier.weight(1f),
                        enabled = active.value,
                        label = "DATE FORMAT",
                        value = format.value,
                        onSelected = { format.value = it },
                        placeholder = "Select date format",
                        items = DatePattern.entries,
                        getLabel = { it.pattern },
                    )

                    DatePicker(
                        modifier = Modifier.weight(1f),
                        enabled = active.value,
                        label = "SELECT DATE",
                        value = value.value,
                        onTap = { value.value = it },
                    )
                }
            }
        }
        CardWrapper { expanded, onExpand ->
            val active = remember { mutableStateOf(true) }

            CardTitle(
                title = "Merge",
                icon = Icons.Merge,
                expanded = expanded,
                onExpand = onExpand,
                active = active.value,
                onToggle = { active.value = it },
            )
            ConfigWrapper(active = active.value, expanded = expanded) {
                val value = remember { mutableStateOf("") }
                val pickerFile =
                    rememberFilePickerLauncher(
                        type = FileKitType.File(extensions = setOf("pdf")),
                        mode = FileKitMode.Single,
                        onResult = { file ->
                            if (file != null) {
                                val fileName = file.file.name
                                value.value = fileName
                            }
                        },
                    )

                InputIcon(
                    enabled = active.value,
                    label = "MERGE NAME",
                    onValueChange = { value.value = it },
                    onIconClick = {
                        pickerFile.launch()
                    },
                    text = value.value,
                    icon = Icons.FileAdd,
                )
            }
        }
        CardWrapper { expanded, onExpand ->

            val active = remember { mutableStateOf(true) }
            val watermark = remember { mutableStateOf("") }
            val opacity = remember { mutableStateOf("0") }
            val position = remember { mutableStateOf<Position?>(null) }
            val fonts = remember { mutableStateOf<Standard14Fonts.FontName?>(null) }
            val fontSize = remember { mutableStateOf("12") }
            val rotation = remember { mutableStateOf("0") }
            val controller = rememberColorPickerController()
            CardTitle(
                title = "Identity",
                icon = Icons.Identity,
                expanded = expanded,
                onExpand = onExpand,
                active = active.value,
                onToggle = { active.value = it },
            )
            ConfigWrapper(active = active.value, expanded = expanded) {
                Input(
                    text = watermark.value,
                    onValueChange = { watermark.value = it },
                    label = "WATERMARK TEXT",
                    placeholder = "Input watermark",
                    enabled = active.value,
                )

                DynamicPosition(maxItemsInEachRow = { if (it > 350.dp) 2 else 1 }) {
                    DropdownPicker(
                        modifier = Modifier.weight(1f),
                        enabled = active.value,
                        label = "POSITION",
                        value = position.value,
                        onSelected = { position.value = it },
                        placeholder = "Select position",
                        items = Position.entries,
                        getLabel = { it.name },
                    )
                    DropdownPicker(
                        modifier = Modifier.weight(1f),
                        enabled = active.value,
                        label = "FONT",
                        value = fonts.value,
                        onSelected = { fonts.value = it },
                        placeholder = "Select font",
                        items = Standard14Fonts.FontName.entries,
                        getLabel = { it.name },
                    )

                    Input(
                        modifier = Modifier.weight(1f),
                        label = "FONT SIZE",
                        text = fontSize.value,
                        onValueChange = {
                            val value = it.toBigDecimalOrNull()
                            if (value != null) {
                                fontSize.value = value.toInt().toString()
                            }
                            if (it.trim().isBlank()) {
                                fontSize.value = "0"
                            }
                        },
                    )

                    Input(
                        modifier = Modifier.weight(1f),
                        label = "ROTATION",
                        text = rotation.value,
                        onValueChange = {
                            val value = it.toBigDecimalOrNull()
                            if (value != null) {
                                rotation.value = value.toInt().toString()
                            }
                            if (it.trim().isBlank()) {
                                rotation.value = "0"
                            }
                        },
                    )

                    ColorPicker(controller = controller, modifier = Modifier.weight(1f))
                    Input(
                        modifier = Modifier.weight(1f),
                        label = "OPACITY",
                        text = opacity.value,
                        onValueChange = {
                            val value = it.toBigDecimalOrNull()
                            if (value != null) {
                                val big0 = BigDecimal("0")
                                val big100 = BigDecimal("100")
                                when {
                                    value in big0..big100 -> opacity.value = value.toInt().toString()
                                    value < big0 -> opacity.value = big0.toString()
                                    value > big100 -> opacity.value = big100.toString()
                                }
                            }
                            if (it.trim().isBlank()) {
                                opacity.value = "0"
                            }
                        },
                        placeholder = "Input opacity",
                        enabled = active.value,
                    )
                }
            }
        }
        CardWrapper { expanded, onExpand ->
            val active = remember { mutableStateOf(true) }
            val format = remember { mutableStateOf<PageFormat?>(null) }
            val fonts = remember { mutableStateOf<Standard14Fonts.FontName?>(null) }
            val controller = rememberColorPickerController()

            val fontSize = remember { mutableStateOf("12") }
            val x = remember { mutableStateOf("0") }
            val y = remember { mutableStateOf("0") }
            CardTitle(
                title = "Numbering",
                icon = Icons.Numbering,
                expanded = expanded,
                onExpand = onExpand,
                active = active.value,
                onToggle = { active.value = it },
            )
            ConfigWrapper(active = active.value, expanded = expanded) {
                DynamicPosition(maxItemsInEachRow = { if (it > 350.dp) 2 else 1 }) {
                    DropdownPicker(
                        modifier = Modifier.weight(1f),
                        enabled = active.value,
                        label = "PAGE FORMAT",
                        value = format.value,
                        onSelected = { format.value = it },
                        placeholder = "Select date format",
                        items = PageFormat.entries,
                        getLabel = { it.value },
                    )
                    DropdownPicker(
                        modifier = Modifier.weight(1f),
                        enabled = active.value,
                        label = "FONT",
                        value = fonts.value,
                        onSelected = { fonts.value = it },
                        placeholder = "Select font",
                        items = Standard14Fonts.FontName.entries,
                        getLabel = { it.name },
                    )

                    Input(
                        modifier = Modifier.weight(1f),
                        label = "FONT SIZE",
                        text = fontSize.value,
                        onValueChange = {
                            val value = it.toBigDecimalOrNull()
                            if (value != null) {
                                fontSize.value = value.toInt().toString()
                            }
                            if (it.trim().isBlank()) {
                                fontSize.value = "0"
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
                            text = x.value,
                            onValueChange = {
                                val value = it.toBigDecimalOrNull()
                                if (value != null) {
                                    x.value = value.toInt().toString()
                                }
                                if (it.trim().isBlank()) {
                                    x.value = "0"
                                }
                            },
                        )
                        Input(
                            modifier = Modifier.weight(1f),
                            label = "Y",
                            text = y.value,
                            onValueChange = {
                                val value = it.toBigDecimalOrNull()
                                if (value != null) {
                                    y.value = value.toInt().toString()
                                }
                                if (it.trim().isBlank()) {
                                    x.value = "0"
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
    val expanded = remember { mutableStateOf(false) }
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
