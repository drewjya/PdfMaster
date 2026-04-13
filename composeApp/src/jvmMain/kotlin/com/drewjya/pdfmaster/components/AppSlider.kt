package com.drewjya.pdfmaster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
) {
    // Colors sampled from your image
    val textAndLabelColor = Color(0xFF4A5158)
    val trackLightGray = Color(0xFFEFEFEF)
    val thumbDarkRed = Color(0xFF92271D)
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                color = textAndLabelColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
            )
            Text(
                text = "${(value * 100).roundToInt()}%",
                color = textAndLabelColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )
        }
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.5.dp) {
            Slider(
                value = value,
                onValueChange = onValueChange,
                interactionSource = interactionSource,
                modifier = Modifier.fillMaxWidth(),
                // 1. COMPLETELY CUSTOM THUMB
                thumb = {
                    // You can put ANY Composable here! Let's make a rounded rectangle with an icon.
                    Box(
                        modifier =
                            Modifier
                                .size(16.dp)
                                .background(thumbDarkRed, CircleShape),
                    )
                },
                // 2. COMPLETELY CUSTOM TRACK
                track = { sliderState ->
                    // We can use the default track but apply a massive height to it
                    SliderDefaults.Track(
                        sliderState = sliderState,
                        thumbTrackGapSize = 2.dp,
                        modifier = Modifier.height(8.dp), // Thicker track!
                        colors =
                            SliderDefaults.colors(
                                activeTrackColor = thumbDarkRed,
                                inactiveTrackColor = trackLightGray,
                            ),
                        drawStopIndicator = null, // Removes the default dots at the ends
                    )
                },
            )
        }
    }
}
