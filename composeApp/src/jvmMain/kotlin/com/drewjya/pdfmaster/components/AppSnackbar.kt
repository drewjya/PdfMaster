package com.drewjya.pdfmaster.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.drewjya.pdfmaster.design.AppTheme
import com.drewjya.pdfmaster.design.ERROR
import com.drewjya.pdfmaster.design.SUCCESS
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.milliseconds

enum class MessageType(
    val color: Color,
) {
    Success(SUCCESS),
    Error(ERROR),
}

data class SnackbarMessage(
    val type: MessageType,
    val title: String,
    val message: String,
)

@Composable
fun AppSnackbar(
    modifier: Modifier = Modifier,
    pdfViewModel: PdfViewModel = koinViewModel(),
    appTheme: AppTheme = koinInject(),
) {
    val message by pdfViewModel.snackbarMessage
    LaunchedEffect(message) {
        if (message != null) {
            delay(5500.milliseconds)
            pdfViewModel.snackbarMessage.value = null
        }
    }

    AnimatedVisibility(
        visible = message != null,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
        modifier = modifier,
    ) {
        message?.let {
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier =
                    Modifier
                        .widthIn(min = 250.dp, max = 400.dp)
                        .padding(16.dp)
                        .clickable {
                            pdfViewModel.snackbarMessage.value = null
                        },
            ) {
                Row(
                    modifier = Modifier.background(appTheme.surface).height(IntrinsicSize.Max).fillMaxWidth(),
                ) {
                    Box(
                        modifier = Modifier.width(6.dp).fillMaxHeight().background(it.type.color),
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(
                            text = it.title,
                            style = appTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = it.type.color,
                        )
                        Text(
                            text = it.message,
                            style = appTheme.typography.bodyMedium,
                            color = appTheme.onSurface,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                }
            }
        }
    }
}
