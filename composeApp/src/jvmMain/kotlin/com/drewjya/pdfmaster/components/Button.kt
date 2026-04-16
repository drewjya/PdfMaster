package com.drewjya.pdfmaster.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowWidthSizeClass
import com.drewjya.pdfmaster.design.AppIcon
import com.drewjya.pdfmaster.design.AppTheme
import com.drewjya.pdfmaster.updater.AppUpdater
import com.drewjya.pdfmaster.updater.UpdateState
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun BadgeButton(
    label: String,
    icon: ImageVector? = null,
    color: Color = Color.White,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isCompact = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val backgroundColor by animateColorAsState(
        targetValue =
            color.copy(alpha = if (!isHovered || !enabled) 0.1f else 1f),
        label = label.replace(" ", "") + "_hover",
    )

    val foregroundColor by animateColorAsState(
        targetValue = if (!isHovered || !enabled) color else Color.White,
        label = label.replace(" ", "") + "_foreground",
    )
    Row(
        modifier =
            Modifier
                .clip(RoundedCornerShape(15))
                .hoverable(interactionSource = interactionSource)
                .background(backgroundColor)
                .clickable(enabled = enabled) { onClick.invoke() }
                .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                icon,
                tint = foregroundColor,
                contentDescription = label.replace(" ", ""),
                modifier = Modifier.padding(vertical = 4.dp).size(14.dp),
            )
        } else {
            Text(
                text = label,
                color = foregroundColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.25.sp,
            )
        }
        if (!isCompact && icon != null) {
            Text(
                text = label,
                color = foregroundColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.25.sp,
            )
        }
    }
}

@Composable
fun VersionButton(
    updater: AppUpdater = koinInject(),
    appTheme: AppTheme = koinInject(),
    pdfViewModel: PdfViewModel = koinInject(),
) {

    val state by updater.state.collectAsState()
    val scope = rememberCoroutineScope()
    val currentVersion = updater.currentVersion

    LaunchedEffect(state) {
        if (state is UpdateState.Error) {
            pdfViewModel.snackbarMessage.value = SnackbarMessage(
                type = MessageType.Error,
                title = "Update Failed",
                message = (state as UpdateState.Error).message,
            )
        }
    }
    val isClickable =
        state is UpdateState.Idle || state is UpdateState.Error || state is UpdateState.UpdateAvailable

    val chipColor =
        when (state) {
            is UpdateState.Error -> appTheme.error.copy(alpha = 0.12f)
            is UpdateState.UpdateAvailable -> appTheme.primary.copy(alpha = 0.12f)
            else -> appTheme.surface
        }

    val contentColor =
        when (state) {
            is UpdateState.Error -> appTheme.error
            is UpdateState.UpdateAvailable -> appTheme.primary
            else -> appTheme.onSurface
        }

    Row(
        modifier =
            Modifier
                .clip(RoundedCornerShape(15))
                .background(chipColor)
                .then(
                    if (isClickable) {
                        Modifier.clickable {
                            scope.launch {
                                if (state is UpdateState.UpdateAvailable) {
                                    updater.downloadUpdate()
                                } else {
                                    updater.checkForUpdate()
                                }
                            }
                        }
                    } else {
                        Modifier
                    },
                ).padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        when (val s = state) {
            is UpdateState.Idle, is UpdateState.UpToDate -> {
                val isLatest = (s == UpdateState.UpToDate)
                Icon(
                    imageVector = AppIcon.Download,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = contentColor,
                )
                Text(
                    text = "v$currentVersion${if (isLatest) " (latest)" else ""}",
                    style = appTheme.typography.labelSmall,
                    color = contentColor,
                )
            }

            is UpdateState.Checking -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp),
                    strokeWidth = 1.5.dp,
                    color = contentColor,
                )
                Text(
                    text = "v$currentVersion",
                    style = appTheme.typography.labelSmall,
                    color = contentColor,
                )
            }

            is UpdateState.UpdateAvailable -> {
                Icon(
                    imageVector = AppIcon.Download,
                    contentDescription = "Update available",
                    modifier = Modifier.size(14.dp),
                    tint = contentColor,
                )
                Text(
                    text = "v$currentVersion → v${s.release.version}",
                    style = appTheme.typography.labelSmall,
                    color = contentColor,
                )
            }

            is UpdateState.Downloading -> {
                // Animated percentage text
                val animatedProgress by animateFloatAsState(
                    targetValue = s.progress,
                    animationSpec = tween(durationMillis = 300),
                    label = "download_progress",
                )
                CircularProgressIndicator(
                    progress = { animatedProgress.coerceIn(0f, 1f) },
                    modifier = Modifier.size(12.dp),
                    strokeWidth = 1.5.dp,
                    color = contentColor,
                )
                Text(
                    text = "${(animatedProgress * 100).toInt()}%  ${formatBytes(s.bytesDownloaded)}/${formatBytes(s.totalBytes)}",
                    style = appTheme.typography.labelSmall,
                    color = contentColor,
                )
            }

            is UpdateState.ReadyToInstall -> {
                Icon(
                    imageVector = AppIcon.Download, // or AppIcon.Install
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = contentColor,
                )
                Text(
                    text = "Tap to install",
                    style = appTheme.typography.labelSmall,
                    color = contentColor,
                    modifier = Modifier.clickable { updater.installUpdate() },
                )
            }

            is UpdateState.Error -> {
                Icon(
                    imageVector = AppIcon.Trash, // or error icon
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = contentColor,
                )
                Text(
                    text = "v$currentVersion",
                    style = appTheme.typography.labelSmall,
                    color = contentColor,
                )
            }
        }
    }
}

@Composable
fun IconButton(
    icon: ImageVector,
    clip: Shape = RoundedCornerShape(4.dp),
    boxSize: Dp = 24.dp,
    enabled: Boolean = true,
    onClick: () -> Unit,
    color: Color = Color.Transparent,
    iconSize: Dp = 16.dp,
) {
    Box(
        modifier =
            Modifier
                .size(boxSize)
                .clip(clip)
                .clickable(enabled = enabled) { onClick() }
                .background(color.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(iconSize),
        )
    }
}

fun formatBytes(bytes: Long): String =
    when {
        bytes >= 1_000_000 -> "%.1f MB".format(bytes / 1_000_000.0)
        bytes >= 1_000 -> "%.1f KB".format(bytes / 1_000.0)
        else -> "$bytes B"
    }
