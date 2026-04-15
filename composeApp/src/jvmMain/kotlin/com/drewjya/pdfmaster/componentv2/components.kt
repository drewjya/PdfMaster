package com.drewjya.pdfmaster.componentv2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import com.drewjya.pdfmaster.design.AppIcon
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import org.koin.compose.koinInject
import androidx.compose.material.icons.Icons as OldIcon

// --- Design System Colors ---
val MaroonPrimary = Color(0xFF5B0D13)
val MaroonPrimaryHover = Color(0xFF3A0005)
val MaroonSecondary = Color(0xFF9E6967)
val MaroonTertiary = Color(0xFF002F4A)
val MaroonNeutral = Color(0xFFF9F7F6)
val MaroonSurface = Color(0xFFFFFFFF)
val MaroonSurfaceAlt = Color(0xFFF4F1F0)
val MaroonOnSurface = Color(0xFF1C1B1F)
val MaroonOnSurfaceMuted = Color(0xFF79747E)
val MaroonError = Color(0xFFBA1A1A)

// --- Models ---
data class PdfDocument(
    val id: String,
    val name: String,
    val sizeMb: Double,
)

@Composable
fun App() {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isCompact = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
    MaterialTheme(
        colorScheme =
            lightColorScheme(
                primary = MaroonPrimary,
                onPrimary = Color.White,
                surface = MaroonSurface,
                onSurface = MaroonOnSurface,
                background = MaroonNeutral,
                onBackground = MaroonOnSurface,
            ),
    ) {

        Column(modifier = Modifier.fillMaxSize().background(MaroonNeutral)) {
            Navbar()

            MainLayout(
                modifier = Modifier.weight(1f),
                staging = { modifier ->
                    FileStagingPane(modifier = modifier)
                },
                configuration = { modifier ->
                    OutputParametersPane(modifier = modifier)
                },
            )

            // DockedFooter() // Now has space at the bottom because MainLayout uses weight(1f)
        }
    }
}

@Composable
fun OutputParametersPane(modifier: Modifier = Modifier) {
    var isMergeActive by remember { mutableStateOf(true) }
    var isMergeExpanded by remember { mutableStateOf(true) }

    var isBatchActive by remember { mutableStateOf(false) }
    var isBatchExpanded by remember { mutableStateOf(false) }

    var isWatermarkActive by remember { mutableStateOf(false) }
    var isWatermarkExpanded by remember { mutableStateOf(false) }

    var isNumberingActive by remember { mutableStateOf(false) }
    var isNumberingExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxHeight()) {
        Text(
            text = "Output Parameters",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaroonOnSurface,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) {
            item {
                ConfigCard(
                    title = "Compilation",
                    icon = AppIcon.Merge,
                    isActive = isMergeActive,
                    isExpanded = isMergeExpanded,
                    onToggleActive = { active ->
                        isMergeActive = active
                        if (active) {
                            isMergeExpanded = true
                            isBatchActive = false // Exclusivity
                        }
                    },
                    onToggleExpand = { isMergeExpanded = !isMergeExpanded },
                ) {
                    EditorialInput("Destination Filename", "Curated_Volume_01.pdf")
                }
            }

            item {
                ConfigCard(
                    title = "Batch (Regex)",
                    icon = AppIcon.File,
                    isActive = isBatchActive,
                    isExpanded = isBatchExpanded,
                    onToggleActive = { active ->
                        isBatchActive = active
                        if (active) {
                            isBatchExpanded = true
                            isMergeActive = false // Exclusivity
                        }
                    },
                    onToggleExpand = { isBatchExpanded = !isBatchExpanded },
                ) {
                    Column(
                        modifier =
                            Modifier
                                .background(
                                    MaroonSurfaceAlt.copy(alpha = 0.5f),
                                    RoundedCornerShape(12.dp),
                                ).padding(16.dp),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f)) { EditorialInput("Date Formatter", "YYYY-MM-DD") }
                            Box(modifier = Modifier.weight(1f)) { EditorialInput("Target Date", "2026-04-15") }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        EditorialInput("Regex Pattern (Optional)", "^document_\\d{4}\\.pdf$")
                    }
                }
            }

            item {
                ConfigCard(
                    title = "Identity Mark",
                    icon = OldIcon.Default.Edit,
                    isActive = isWatermarkActive,
                    isExpanded = isWatermarkExpanded,
                    onToggleActive = { active ->
                        isWatermarkActive = active
                        if (active) isWatermarkExpanded = true
                    },
                    onToggleExpand = { isWatermarkExpanded = !isWatermarkExpanded },
                ) {
                    EditorialInput("Display Text", "DRAFT / INTERNAL")
                }
            }

            item {
                ConfigCard(
                    title = "Pagination",
                    icon = AppIcon.Number,
                    isActive = isNumberingActive,
                    isExpanded = isNumberingExpanded,
                    onToggleActive = { active ->
                        isNumberingActive = active
                        if (active) isNumberingExpanded = true
                    },
                    onToggleExpand = { isNumberingExpanded = !isNumberingExpanded },
                ) {
                    EditorialInput("Nomenclature", "Page {n} of {t}")
                }
            }
        }
    }
}

@Composable
fun ConfigCard(
    title: String,
    icon: ImageVector,
    isActive: Boolean,
    isExpanded: Boolean,
    onToggleActive: (Boolean) -> Unit,
    onToggleExpand: () -> Unit,
    content: @Composable () -> Unit,
) {
    val rotation by animateFloatAsState(if (isExpanded) 0f else -90f)
    val alpha by animateFloatAsState(if (isActive) 1f else 0.7f)

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (isActive) 12.dp else 2.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = Color.Black.copy(alpha = if (isActive) 0.06f else 0.03f),
                ).background(MaroonSurface.copy(alpha = alpha), RoundedCornerShape(16.dp))
                .border(1.dp, Color.Black.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp)),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
                    .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = OldIcon.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    modifier = Modifier.size(16.dp).rotate(rotation),
                    tint = MaroonOnSurfaceMuted.copy(alpha = 0.5f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier =
                        Modifier
                            .background(
                                if (isActive) MaroonPrimary.copy(alpha = 0.1f) else MaroonSurfaceAlt,
                                RoundedCornerShape(6.dp),
                            ).padding(6.dp),
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = if (isActive) MaroonPrimary else MaroonOnSurfaceMuted,
                        modifier = Modifier.size(16.dp),
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) MaroonOnSurface else MaroonOnSurfaceMuted,
                )
            }

            CustomToggle(isActive = isActive, onToggle = { onToggleActive(it) })
        }

        AnimatedVisibility(visible = isExpanded) {
            Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {
                content()
            }
        }
    }
}

@Composable
fun EditorialInput(
    label: String,
    value: String,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = MaroonOnSurfaceMuted,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().height(44.dp),
            textStyle =
                LocalTextStyle.current.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaroonOnSurface,
                ),
            colors =
                OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaroonSurfaceAlt,
                    focusedContainerColor = MaroonSurface,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = MaroonSecondary.copy(alpha = 0.3f),
                ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
        )
    }
}

@Composable
fun CustomToggle(
    isActive: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    Box(
        modifier =
            Modifier
                .width(36.dp)
                .height(20.dp)
                .background(if (isActive) MaroonPrimary else MaroonSurfaceAlt, RoundedCornerShape(50))
                .clickable { onToggle(!isActive) }
                .padding(2.dp),
        contentAlignment = if (isActive) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        Box(
            modifier =
                Modifier
                    .size(16.dp)
                    .shadow(1.dp, CircleShape)
                    .background(Color.White, CircleShape),
        )
    }
}

@Composable
fun SurfaceButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .background(MaroonSurfaceAlt, RoundedCornerShape(50))
                .clickable { onClick() }
                .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = MaroonPrimary, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, color = MaroonPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DockedFooter() {
    val pdfViewModel: PdfViewModel = koinInject()
    val files by pdfViewModel.pdfFiles.collectAsStateWithLifecycle()

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .shadow(elevation = 16.dp, spotColor = Color.Black.copy(alpha = 0.05f))
                .background(MaroonSurface)
                .border(1.dp, Color.Black.copy(alpha = 0.05f))
                .padding(horizontal = 32.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier.background(MaroonSurfaceAlt, RoundedCornerShape(8.dp)).padding(8.dp),
            ) {
                Icon(AppIcon.Folder, contentDescription = null, tint = MaroonSecondary, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "TARGET DIRECTORY",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaroonOnSurfaceMuted,
                    letterSpacing = 1.sp,
                )
                Text(
                    "/Users/Studio/Archives/PDFMaster_Output",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaroonOnSurface,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier =
                    Modifier
                        .border(1.dp, Color.Black.copy(alpha = 0.05f), RoundedCornerShape(6.dp))
                        .clickable { }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
            ) {
                Text("Modify", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaroonOnSurface)
            }
        }
        val fileCount = files.size
        Button(
            onClick = { },
            enabled = fileCount > 0,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaroonPrimary,
                    disabledContainerColor = MaroonPrimary.copy(alpha = 0.4f),
                ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 14.dp),
        ) {
            Text("Execute Compile" + if (fileCount > 0) " ($fileCount)" else "", fontWeight = FontWeight.Bold)
        }
    }
}
