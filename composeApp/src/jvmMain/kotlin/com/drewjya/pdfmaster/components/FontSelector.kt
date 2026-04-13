package com.drewjya.pdfmaster.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drewjya.pdfmaster.helper.getDeviceFonts

@Composable
fun FontSelector(modifier: Modifier = Modifier) {
    val suggestions = remember { getDeviceFonts() }
    var textFieldValue by remember { mutableStateOf(suggestions.firstOrNull() ?: "Arial") }
    var filteredSuggestions by remember { mutableStateOf(suggestions) }
    var showSuggestions by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        DropdownSelector(
            label = "FONT",
        )
        LaunchedEffect(Unit) { focusRequester.requestFocus() }
        // FIX: Wrapped the results in a Box with a constrained height
        // This ensures the LazyColumn knows exactly how much space it can take.
        if (showSuggestions) {
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp) // Set a maximum height for the dropdown
                        .animateContentSize(),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                if (filteredSuggestions.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(filteredSuggestions) { suggestion ->
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            textFieldValue = suggestion
                                            showSuggestions = false
                                        }.padding(16.dp),
                            ) {
                                Text(text = suggestion, fontSize = 18.sp)
                            }
                        }
                    }
                } else {
                    Text(
                        "No results found",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray,
                    )
                }
            }
        }
    }
}
