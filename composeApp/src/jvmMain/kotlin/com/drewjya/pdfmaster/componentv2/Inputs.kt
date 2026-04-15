package com.drewjya.pdfmaster.componentv2

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drewjya.pdfmaster.design.AppTheme
import com.drewjya.pdfmaster.helper.DatePattern
import com.drewjya.pdfmaster.helper.formatDate
import org.koin.compose.koinInject

@Composable
fun InputWrapper(
    modifier: Modifier = Modifier,
    label: String = "",
    content: @Composable () -> Unit = {},
) {
    val appTheme = koinInject<AppTheme>()
    Column(modifier = modifier) {
        Text(
            text = label,
            color = appTheme.onSurfaceMuted,
            fontSize = 10.sp,
            lineHeight = 8.sp,
        )
        content()
    }
}

@Composable
fun Input(
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    text: String = "",
    placeholder: String = "",
    onValueChange: (String) -> Unit = {},
    components: @Composable () -> Unit = {},
) {
    val appTheme = koinInject<AppTheme>()

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val clip = RoundedCornerShape(8.dp)

    InputWrapper(modifier = modifier, label = label) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            BasicTextField(
                value = text,
                enabled = enabled,
                onValueChange = onValueChange,
                interactionSource = interactionSource,
                textStyle =
                    LocalTextStyle.current.copy(
                        color = if (enabled) appTheme.onSurface else appTheme.onSurfaceMuted,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        lineHeight = 12.sp,
                    ),
                modifier = Modifier.weight(1f),
                singleLine = true,
                cursorBrush = SolidColor(appTheme.primary),
                decorationBox = { innerTextField ->
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(
                                    color = if (isFocused) Color.Transparent else appTheme.surfaceAlt,
                                    shape = clip,
                                ).border(
                                    width = 1.dp,
                                    color = if (isFocused) appTheme.primary.copy(alpha = 0.2f) else Color.Transparent,
                                    shape = clip,
                                ).height(height = 32.dp)
                                .padding(horizontal = 8.dp, vertical = 2.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp,
                                color = appTheme.onSurfaceMuted,
                            )
                        }
                        innerTextField()
                    }
                },
            )

            components()
        }
    }
}

@Composable
fun InputIcon(
    modifier: Modifier = Modifier,
    label: String = "",
    text: String = "",
    enabled: Boolean = true,
    icon: ImageVector,
    onValueChange: (String) -> Unit = {},
    onIconClick: () -> Unit = {},
) {
    val appTheme = koinInject<AppTheme>()
    Input(
        enabled = enabled,
        modifier = modifier,
        label = label,
        text = text,
        onValueChange = onValueChange,
    ) {
        AppIcon(
            icon = icon,
            backgroundColor = appTheme.surfaceAlt,
            color = if (enabled) appTheme.primary else appTheme.primary.copy(alpha = 0.5f),
            modifier = { modifier ->
                if (enabled) modifier.clickable { onIconClick() } else modifier
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    label: String = "",
    value: Long? = null,
    enabled: Boolean = true,
    onTap: (Long) -> Unit = {},
) {
    val appTheme = koinInject<AppTheme>()
    val clip = RoundedCornerShape(8.dp)
    var showPicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = value)

    LaunchedEffect(datePickerState.selectedDateMillis) {
        val millis = datePickerState.selectedDateMillis ?: value ?: return@LaunchedEffect
        showPicker = false
        onTap(millis)
    }

    InputWrapper(modifier = modifier, label = label) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(clip)
                    .clickable { showPicker = true }
                    .background(color = appTheme.surfaceAlt, shape = clip)
                    .border(width = 1.dp, color = Color.Transparent, shape = clip)
                    .height(height = 32.dp)
                    .padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (value == null) {
                    Text(
                        text = "Please select a date",
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        color = appTheme.onSurfaceMuted,
                    )
                } else {
                    Text(
                        text = formatDate(value, format = DatePattern.Long),
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        lineHeight = 12.sp,
                        color = if (enabled) appTheme.onSurface else appTheme.onSurfaceMuted,
                    )
                }
            }

            AppIcon(
                icon = Icons.Default.DateRange,
                backgroundColor = appTheme.surfaceAlt,
                color = if (enabled) appTheme.primary else appTheme.primary.copy(alpha = 0.5f),
            )
        }

        if (showPicker) {
            DatePickerDialog(
                onDismissRequest = { showPicker = false },
                confirmButton = {},
                dismissButton = {
                    TextButton(
                        onClick = { showPicker = false },
                    ) {
                        Text("Cancel", color = MaterialTheme.colorScheme.primary)
                    }
                },
            ) {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.sizeIn(maxWidth = 350.dp, maxHeight = 380.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T> DropdownPicker(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true,
    value: T? = null,
    onSelected: (T) -> Unit = {},
    placeholder: String = "Select Item",
    items: List<T> = emptyList(),
    getLabel: (T) -> String = { it.toString() },
) {
    var showSuggestions by remember { mutableStateOf(false) }
    var dropdownWidth by remember { mutableStateOf(0) }
    val clip = RoundedCornerShape(8.dp)
    val appTheme = koinInject<AppTheme>()
    val density = LocalDensity.current

    InputWrapper(modifier = modifier, label = label) {
        Row(
            Modifier
                .fillMaxWidth()
                .onSizeChanged { dropdownWidth = it.width }
                .clip(clip)
                .clickable { showSuggestions = true }
                .background(color = appTheme.surfaceAlt, shape = clip)
                .border(width = 1.dp, color = Color.Transparent, shape = clip)
                .height(height = 32.dp)
                .padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (value == null) {
                    Text(
                        text = placeholder,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        color = appTheme.onSurfaceMuted,
                    )
                } else {
                    Text(
                        text = getLabel(value),
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        lineHeight = 12.sp,
                        color = if (enabled) appTheme.onSurface else appTheme.onSurfaceMuted,
                    )
                }
            }

            val rotationState by animateFloatAsState(
                targetValue = if (showSuggestions) 0f else 180f,
                animationSpec = spring(stiffness = Spring.StiffnessHigh),
                label = "ArrowRotation",
            )
            AppIcon(
                Icons.Default.KeyboardArrowDown,
                backgroundColor = appTheme.surfaceAlt,
                color = if (enabled) appTheme.primary else appTheme.primary.copy(alpha = 0.5f),
                modifier = { modifier -> modifier.rotate(rotationState) },
            )
        }

        DropdownMenu(
            expanded = showSuggestions,
            modifier =
                Modifier
                    .clip(clip)
                    .width(with(density) { dropdownWidth.toDp() })
                    .background(Color.White)
                    .padding(horizontal = 8.dp)
                    .heightIn(max = 200.dp),
            onDismissRequest = { showSuggestions = false },
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = item == value
                DropdownMenuItem(
                    modifier =
                        Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .height(36.dp)
                            .background(color = if (isSelected) appTheme.primary.copy(alpha = 0.1f) else Color.White),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                getLabel(item),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) appTheme.primary else appTheme.onSurface,
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp).align(Alignment.CenterVertically),
                                    tint = appTheme.primary,
                                )
                            }
                        }
                    },
                    onClick = {
                        onSelected(item)
                        showSuggestions = false
                    },
                    colors =
                        MenuItemColors(
                            textColor = if (isSelected) appTheme.primary else appTheme.onSurface,
                            leadingIconColor = if (isSelected) appTheme.primary else appTheme.onSurface,
                            trailingIconColor = if (isSelected) appTheme.primary else appTheme.onSurface,
                            disabledTextColor = appTheme.onSurface,
                            disabledLeadingIconColor = appTheme.onSurface,
                            disabledTrailingIconColor = appTheme.onSurface,
                        ),
                )

                if (index != items.lastIndex) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
