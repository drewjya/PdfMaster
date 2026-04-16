package com.drewjya.pdfmaster.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.drewjya.pdfmaster.design.AppTheme
import com.drewjya.pdfmaster.helper.DatePattern
import com.drewjya.pdfmaster.helper.formatDate
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
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
    value: TextFieldValue = TextFieldValue(""), // Correct: Using TextFieldValue
    placeholder: String = "",
    onValueChange: (TextFieldValue) -> Unit = {}, // Correct: Passing back the full value
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
                value = value,
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
                        // Use value.text to check if empty
                        if (value.text.isEmpty()) {
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
    value: TextFieldValue = TextFieldValue(""),
    enabled: Boolean = true,
    placeholder: String = "",
    icon: ImageVector,
    onValueChange: (TextFieldValue) -> Unit = {},
    onIconClick: () -> Unit = {},
) {
    val appTheme = koinInject<AppTheme>()
    Input(
        enabled = enabled,
        modifier = modifier,
        placeholder = placeholder,
        label = label,
        value = value,
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
fun InputDatePicker(
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
                    .clickable(enabled = enabled) { showPicker = true }
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
    suggestionSize: Int = 10,
) {
    var showSuggestions by remember { mutableStateOf(false) }
    var dropdownWidth by remember { mutableStateOf(0) }

    // Initialize with the current label if value exists
    var searchQuery by remember {
        mutableStateOf(TextFieldValue(value?.let { getLabel(it) } ?: ""))
    }

    LaunchedEffect(value, showSuggestions) {
        if (!showSuggestions) {
            searchQuery = TextFieldValue("")
            return@LaunchedEffect
        }
        val currentLabel = value?.let { getLabel(it) } ?: ""
        if (searchQuery.text != currentLabel) {
            searchQuery = TextFieldValue(currentLabel)
        }
    }

    // When opening: Clear search to show all options OR Keep label and select all text

    val clip = RoundedCornerShape(8.dp)
    val appTheme = koinInject<AppTheme>()
    val density = LocalDensity.current

    val useSearch = items.size > suggestionSize

    // Filter logic: If searching, filter. If not, show original list.
    val filteredItems =
        remember(searchQuery.text, items) {
            if (useSearch && searchQuery.text.isNotEmpty()) {
                items.filter { getLabel(it).contains(searchQuery.text, ignoreCase = true) }
            } else {
                items
            }
        }.take(suggestionSize)
    InputWrapper(modifier = modifier, label = label) {
        Row(
            Modifier
                .fillMaxWidth()
                .onSizeChanged { dropdownWidth = it.width }
                .clip(clip)
                .clickable(enabled = enabled) { showSuggestions = true }
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
                        color = appTheme.onSurfaceMuted,
                    )
                } else {
                    Text(
                        text = getLabel(value),
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
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
                modifier = { m -> m.rotate(rotationState) },
            )
        }

        DropdownMenu(
            expanded = showSuggestions,
            properties = PopupProperties(focusable = true), // Ensures TextField gets focus
            modifier =
                Modifier
                    .width(with(density) { dropdownWidth.toDp() })
                    .background(Color.White)
                    .heightIn(max = 300.dp),
            onDismissRequest = { showSuggestions = false },
        ) {
            if (useSearch) {
                Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                    val searchInteractionSource = remember { MutableInteractionSource() }
                    val isSearchFocused by searchInteractionSource.collectIsFocusedAsState()

                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        interactionSource = searchInteractionSource,
                        textStyle =
                            LocalTextStyle.current.copy(
                                color = appTheme.onSurface,
                                fontSize = 12.sp,
                            ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        cursorBrush = SolidColor(appTheme.primary),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .background(appTheme.surfaceAlt, RoundedCornerShape(6.dp))
                                        .border(
                                            1.dp,
                                            if (isSearchFocused) appTheme.primary.copy(0.3f) else Color.Transparent,
                                            RoundedCornerShape(6.dp),
                                        ).height(32.dp)
                                        .padding(horizontal = 8.dp),
                                contentAlignment = Alignment.CenterStart,
                            ) {
                                if (searchQuery.text.isEmpty()) {
                                    Text("Search...", fontSize = 12.sp, color = appTheme.onSurfaceMuted)
                                }
                                innerTextField()
                            }
                        },
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                if (filteredItems.isEmpty()) {
                    Text(
                        "No results",
                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
                        fontSize = 12.sp,
                        color = appTheme.onSurfaceMuted,
                    )
                } else {
                    filteredItems.forEach { item ->
                        val isSelected = item == value
                        DropdownMenuItem(
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .height(36.dp)
                                    .background(if (isSelected) appTheme.primary.copy(0.1f) else Color.Transparent),
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            text = {
                                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                    Text(
                                        getLabel(item),
                                        fontSize = 12.sp,
                                        color = if (isSelected) appTheme.primary else appTheme.onSurface,
                                    )
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, null, Modifier.size(16.dp), appTheme.primary)
                                    }
                                }
                            },
                            onClick = {
                                onSelected(item)
                                showSuggestions = false
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    controller: ColorPickerController,
    color: Color? = null,
    onChanged: (Color) -> Unit = {},
) {
    LaunchedEffect(Unit) {
        if (color != null) {

            controller.selectByColor(color, true)
        }
    }

    InputWrapper(modifier = modifier, label = "COLOR") {
        ColorPickerPopup(
            controller = controller,
            color = color ?: Color.Black,
            onColorSelected = onChanged,
        )
    }
}

@Composable
private fun ColorPickerPopup(
    controller: ColorPickerController,
    color: Color = Color.Black,
    onColorSelected: (Color) -> Unit = {},
    appTheme: AppTheme = koinInject(),
) {
    var showPopup by remember { mutableStateOf(false) }
    val onDismissRequest: () -> Unit = {
        showPopup = false
    }
    val templateColors = listOf(Color.Black, Color.Blue, Color.Yellow, Color.Red, Color.Green)

    val clip = RoundedCornerShape(4.dp)
    Box(
        modifier =
            Modifier
                .fillMaxWidth(1f)
                .height(32.dp)
                .clip(clip)
                .border(0.5.dp, appTheme.onSurfaceMuted, clip)
                .padding(4.dp)
                .clickable { showPopup = true }
                .background(color, clip),
    )

    if (showPopup) {
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = onDismissRequest,
            // focusable = true is REQUIRED for sliders to work and outside-clicks to dismiss
            properties = PopupProperties(focusable = true, dismissOnClickOutside = true),
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp, // Added shadow to make it pop off the background
                modifier = Modifier.width(240.dp),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    HsvColorPicker(
                        initialColor = color,
                        onColorChanged = { onColorSelected(it.color) },
                        modifier = Modifier.fillMaxWidth().height(250.dp),
                        controller = controller,
                    )
                    BrightnessSlider(
                        modifier = Modifier.fillMaxWidth().height(15.dp),
                        controller = controller,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        templateColors.forEach { color ->
                            Box(
                                modifier =
                                    Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .clickable {
                                            controller.selectByColor(color, true)
                                        },
                            )
                        }
                    }
                    Button(onClick = {
                        onDismissRequest()
                    }) {
                        Text("Back")
                    }
                }
            }
        }
    }
}
