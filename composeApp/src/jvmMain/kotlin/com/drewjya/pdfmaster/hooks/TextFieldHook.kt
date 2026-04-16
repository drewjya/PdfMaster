package com.drewjya.pdfmaster.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun rememberAppInput(
    externalValue: String?,
    onValueChange: (String) -> Unit,
): Pair<TextFieldValue, (TextFieldValue) -> Unit> {
    // Initialize state with the external value
    var state by remember {
        mutableStateOf(TextFieldValue(externalValue ?: ""))
    }

    // Sync local state when external value changes (e.g., from a file picker or API)
    LaunchedEffect(externalValue) {
        val safeExternal = externalValue ?: ""
        if (state.text != safeExternal) {
            state =
                state.copy(
                    text = safeExternal,
                    selection = TextRange(safeExternal.length), // Keep cursor at the end
                )
        }
    }

    // A wrapped setter to update both local state (for the UI) and external state (for the VM)
    val updateState: (TextFieldValue) -> Unit = { newValue ->
        state = newValue
        onValueChange(newValue.text)
    }

    return state to updateState
}
