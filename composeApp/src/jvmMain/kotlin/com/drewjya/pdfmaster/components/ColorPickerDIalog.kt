package com.drewjya.pdfmaster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker

@Composable
fun ColorPicker(
    controller: ColorPickerController,
    modifier: Modifier = Modifier,
) {
    val textAndLabelColor = Color(0xFF4A5158)
    val templateColors = listOf(Color.Black, Color.Blue, Color.Yellow, Color.Red, Color.Green)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        Column {
            Text(
                "COLOR",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = textAndLabelColor,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
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
        }
        ColorPickerPopup(controller = controller)
    }
}

@Composable
fun ColorPickerPopup(controller: ColorPickerController) {
    var showPopup by remember { mutableStateOf(false) }
    val onDismissRequest: () -> Unit = {
        showPopup = false
    }
    Column {
        Row(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {
                        showPopup = true
                    }.padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            AlphaTile(
                controller = controller,
                modifier =
                    Modifier
                        .size(10.dp)
                        .background(controller.selectedColor.value),
            )
            Icon(
                Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )
        }

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
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        HsvColorPicker(
                            modifier = Modifier.fillMaxWidth().height(250.dp),
                            controller = controller,
                        )
                        BrightnessSlider(
                            modifier = Modifier.fillMaxWidth().height(15.dp),
                            controller = controller,
                        )
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
}
