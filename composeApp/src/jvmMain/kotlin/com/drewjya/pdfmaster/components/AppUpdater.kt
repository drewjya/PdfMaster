package com.drewjya.pdfmaster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.drewjya.pdfmaster.design.AppColor
import com.drewjya.pdfmaster.design.AppIcon
import com.pavi2410.appupdater.AppUpdater
import com.pavi2410.appupdater.UpdateState
import com.pavi2410.appupdater.ui.DownloadProgressIndicator
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun AppUpdater() {

    val updater: AppUpdater = koinInject()


    val state by updater.state.collectAsState()
    val scope = rememberCoroutineScope()
    val currentVersion = updater.currentVersion


    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Current: v$currentVersion",
            style = MaterialTheme.typography.bodySmall,
        )

        when (val s = state) {
            is UpdateState.Idle -> {
                Text("Tap below to check for updates", style = MaterialTheme.typography.bodyMedium)
            }

            is UpdateState.Checking -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LinearProgressIndicator(modifier = Modifier.weight(1f))
                    Text("Checking...", style = MaterialTheme.typography.bodySmall)
                }
            }

            is UpdateState.UpdateAvailable -> {
                Text(
                    text = "Updated: v${s.release.version}",
                    style = MaterialTheme.typography.bodySmall,
                )
                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .clickable(
                            onClick = { scope.launch { updater.downloadUpdate() } },
                        )
                        .background(AppColor.PRIMARY)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center

                ) {

                    Icon(
                        imageVector = AppIcon.Download,
                        contentDescription = "Download",
                        modifier = Modifier.size((MaterialTheme.typography.bodySmall.fontSize.value * 1.5).dp),
                        tint = Color.White,

                        )
                    Text(
                        formatSize(s.asset.size),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = Color.White,
                    )

                }
            }

            is UpdateState.UpToDate -> {
                Text(
                    text = "You're up to date!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            is UpdateState.Downloading -> {
                Text("Downloading update...", style = MaterialTheme.typography.bodyMedium)
                DownloadProgressIndicator(
                    progress = s.progress,
                    bytesDownloaded = s.bytesDownloaded,
                    totalBytes = s.totalBytes,
                )
            }

            is UpdateState.ReadyToInstall -> {
                Text(
                    text = "Download complete! Ready to install.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(
                    onClick = { updater.installUpdate() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Install Update")
                }
            }

            is UpdateState.Error -> {
                Text(
                    text = s.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        if (state is UpdateState.Idle || state is UpdateState.UpToDate || state is UpdateState.Error) {
            OutlinedButton(
                onClick = { scope.launch { updater.checkForUpdate() } },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Check for Updates")
            }
        }
    }

}

private fun formatSize(bytes: Long): String {
    return when {
        bytes >= 1_000_000 -> "%.1f MB".format(bytes / 1_000_000.0)
        bytes >= 1_000 -> "%.1f KB".format(bytes / 1_000.0)
        else -> "$bytes B"
    }
}

