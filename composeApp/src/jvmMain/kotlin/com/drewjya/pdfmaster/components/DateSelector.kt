package com.drewjya.pdfmaster.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import com.drewjya.pdfmaster.helper.formatDate
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import org.koin.compose.koinInject
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
    label: String,
) {
    var showSuggestions by remember { mutableStateOf(false) }
    val pdfViewModel: PdfViewModel = koinInject()
    val formattedDateString =
        remember(pdfViewModel.selectedDate.value, pdfViewModel.dateFormat.value) {
            formatDate(
                timestamp = pdfViewModel.selectedDate.value,
                format = pdfViewModel.dateFormat.value,
            )
        }

    Column(modifier = modifier) {
        Text(
            label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = textAndLabelColor,
        )
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                    .clickable(onClick = {
                        showSuggestions = showSuggestions.not()
                    }),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                formattedDateString,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )
            Icon(
                Icons.Default.DateRange,
                contentDescription = "Arrow down",
                modifier = Modifier.padding(horizontal = 8.dp),
                tint = textAndLabelColor,
            )
        }

        if (showSuggestions) {
            val datePickerState = rememberDatePickerState()

            DatePickerDialog(
                onDismissRequest = { showSuggestions = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            pdfViewModel.selectedDate.value =
                                datePickerState.selectedDateMillis ?: Clock.System.now().toEpochMilliseconds()
                            showSuggestions = false
                        },
                    ) {
                        Text("OK", color = MaterialTheme.colorScheme.primary)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showSuggestions = false },
                    ) {
                        Text("Cancel", color = MaterialTheme.colorScheme.primary)
                    }
                },
            ) {
                DatePicker(
                    state = datePickerState,
                    // Make DatePicker smaller
                    modifier = Modifier.sizeIn(maxWidth = 350.dp),
                )
            }
        }
    }
}
