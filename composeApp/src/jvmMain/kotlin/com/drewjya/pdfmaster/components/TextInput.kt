package com.drewjya.pdfmaster.components

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val textAndLabelColor = Color(0xFF4A5158)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(
    label: String,
    text: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = textAndLabelColor,
        )
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            textStyle =
                LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                ),
            modifier = Modifier.fillMaxWidth().border(1.dp, Color.Black, RoundedCornerShape(4.dp)),
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    placeholder = {
                        Text(placeholder, fontWeight = FontWeight.Light, fontSize = 12.sp)
                    },
                    value = text,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = remember { MutableInteractionSource() },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    container = {
                        TextFieldDefaults.Container(
                            enabled = true,
                            isError = false,
                            interactionSource = remember { MutableInteractionSource() },
                            colors =
                                TextFieldDefaults.colors(
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    unfocusedLabelColor = Color.Transparent,
                                ),
                        )
                    },
                )
            },
        )
    }
}
