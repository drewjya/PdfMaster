package com.drewjya.pdfmaster.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import com.drewjya.pdfmaster.design.AppTheme
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import java.io.File
import java.math.BigDecimal
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

@Composable
fun FileStagingPane(
    modifier: Modifier = Modifier,
    pdfViewModel: PdfViewModel = koinViewModel(),
) {
    val appTheme = koinInject<AppTheme>()
    val files by pdfViewModel.pdfFiles.collectAsStateWithLifecycle(emptyList())
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isCompact = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
    val horizontalState = rememberLazyListState()
    val verticalState = rememberLazyListState()
    val clip = RoundedCornerShape(8.dp)
    Column(
        modifier =
            modifier
                .background(appTheme.surfaceAlt.copy(alpha = 0.5f), clip)
                .border(1.dp, appTheme.secondary.copy(alpha = 0.2f), clip),
    ) {
        if (files.isEmpty()) {
            EmptyStateView(
                modifier = if (!isCompact) Modifier.fillMaxSize() else Modifier.height(164.dp),
            )
        } else {
            val sizes = files.size
            val ceiling =
                ceil(
                    sizes
                        .toBigDecimal()
                        .divide(BigDecimal("10"))
                        .toDouble(),
                ).toInt()
            val totalPages = max(1, ceiling)
            val currentPage = remember { mutableStateOf(1) }
            val startIdx = (currentPage.value - 1) * 10
            val endIdx = min(startIdx + 10, sizes) - 1
            val visibleFiles = files.slice(startIdx..endIdx)

            if (isCompact) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    LazyRow(
                        state = horizontalState,
                        modifier = Modifier.padding(12.dp).padding(bottom = 8.dp), // Space for scrollbar
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        itemsIndexed(visibleFiles) { index, file ->
                            val actualIndex = startIdx + index + 1
                            FileItem(index = actualIndex, file = file, totalPages = files.size)
                        }
                    }
                    HorizontalScrollbar(
                        modifier =
                            Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                        adapter = rememberScrollbarAdapter(horizontalState),
                    )
                }
            } else {
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    LazyColumn(
                        state = verticalState,
                        modifier = Modifier.fillMaxSize().padding(12.dp).padding(end = 4.dp), // Space for scrollbar
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        itemsIndexed(visibleFiles) { index, file ->
                            val actualIndex = startIdx + index + 1
                            FileItem(index = actualIndex, file = file, totalPages = files.size)
                        }
                    }

                    VerticalScrollbar(
                        modifier =
                            Modifier
                                .align(Alignment.CenterEnd)
                                .fillMaxHeight()
                                .padding(vertical = 12.dp),
                        adapter = rememberScrollbarAdapter(verticalState),
                    )
                }
            }
            PaginationSystem(
                currentPage = currentPage.value,
                onPageChange = { newPage -> currentPage.value = newPage },
                totalPages = totalPages,
            )
        }
    }
}

@Composable
fun EmptyStateView(modifier: Modifier = Modifier) {
    val appTheme = koinInject<AppTheme>()
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier.size(48.dp).background(Color.White, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(com.drewjya.pdfmaster.design.Icons.Empty, contentDescription = null, tint = appTheme.primary)
        }

        Column {
            Text(
                text = "Drag and drop your PDFs, or",
                fontSize = 13.sp,
                color = appTheme.onSurfaceMuted,
                modifier = Modifier.widthIn(300.dp),
                textAlign = TextAlign.Center,
            )
            Text(
                text = "browse your local directory to begin.",
                fontSize = 13.sp,
                color = appTheme.onSurfaceMuted,
                modifier = Modifier.widthIn(300.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FileItem(
    pdfViewModel: PdfViewModel = koinViewModel(),
    index: Int,
    totalPages: Int = 1,
    file: File,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isCompact = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
    val appTheme = koinInject<AppTheme>()
    var isHovered by remember { mutableStateOf(false) }

    val tintColor by animateColorAsState(
        targetValue = if (isHovered) appTheme.primary.copy(
            alpha = 0.7f
        ) else appTheme.onSurfaceMuted,
        label = "TintColorAnimation"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isHovered) appTheme.primary.copy(
            alpha = 0.7f
        ) else appTheme.onSurfaceMuted.copy(alpha = 0.2f),
        label = "BorderColorAnimation"
    )
    val weight by animateFloatAsState(
        targetValue = if (isHovered) 650f else 400f,
        label = "WeightAnimation",

        )

    val clip = RoundedCornerShape(8.dp)

    @Composable
    fun actions() {
        AnimatedVisibility(
            visible = isHovered,
            // Enter transition: Fade in + Expand horizontally
            enter = fadeIn(animationSpec = tween(200)) + expandHorizontally(),
            // Exit transition: Fade out + Shrink horizontally
            exit = fadeOut(animationSpec = tween(200)) + shrinkHorizontally()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { pdfViewModel.moveFile(file, isUp = true) },
                    color = if (index > 1) appTheme.onSurface else appTheme.onSurfaceMuted,
                    enabled = index > 1,
                    icon = Icons.AutoMirrored.Outlined.ArrowBack,
                )
                IconButton(
                    onClick = { pdfViewModel.removeFile(file) },
                    color = appTheme.error,
                    icon = com.drewjya.pdfmaster.design.Icons.Trash,
                )
                IconButton(
                    onClick = { pdfViewModel.moveFile(file, isUp = false) },
                    color = if (index < (totalPages)) appTheme.onSurface else appTheme.onSurfaceMuted,
                    enabled = index < (totalPages),
                    icon = Icons.AutoMirrored.Outlined.ArrowForward,
                )
            }
        }
    }

    if (isCompact) {
        Column(
            modifier =
                Modifier
                    .clip(clip)
                    .size(140.dp)
                    .onPointerEvent(PointerEventType.Enter) { isHovered = true }
                    .onPointerEvent(PointerEventType.Exit) { isHovered = false }
                    .border(1.5.dp, borderColor, clip)
                    .background(Color.White)
                    .padding(all = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(24.dp)
                            .background(appTheme.secondary.copy(alpha = 0.1f), clip),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = index.toString().padStart(2, '0'),
                        fontSize = 11.sp,
                        fontWeight = FontWeight(weight.toInt()),
                        color = appTheme.onSurfaceMuted,
                    )
                }

                actions()
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    com.drewjya.pdfmaster.design.Icons.Pdf,
                    contentDescription = null,
                    tint = tintColor,
                    modifier = Modifier.size(32.dp),
                )
            }
            Column {
                Text(
                    text = file.name,
                    fontSize = 11.sp,
                    fontWeight = FontWeight(weight.toInt()),
                    color = tintColor,
                    maxLines = 1,
                    lineHeight = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    formatBytes(file.length()),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = appTheme.onSurfaceMuted.copy(alpha = 0.8f),
                )
            }

        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onPointerEvent(PointerEventType.Enter) { isHovered = true }
                .onPointerEvent(PointerEventType.Exit) { isHovered = false }
                .clip(clip)
                .background(appTheme.surface, clip)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(clip)
                    .border(1.5.dp, borderColor, clip)
                    .padding(12.dp),
//                .blur(blurRadius)
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = index.toString().padStart(2, '0'),
                        fontSize = 11.sp,
                        fontWeight = FontWeight(weight.toInt()),
                        color = tintColor,
                    )
                }

                Icon(
                    com.drewjya.pdfmaster.design.Icons.Pdf,
                    contentDescription = null,
                    tint = tintColor,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        file.name,
                        fontSize = 13.sp,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight(weight.toInt()),
                        color = appTheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        formatBytes(file.length()),
                        fontSize = 10.sp,
                        lineHeight = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = appTheme.onSurfaceMuted.copy(alpha = 0.8f),
                    )
                }

                actions()
            }
        }


    }
}

@Composable
fun PaginationSystem(
    currentPage: Int,
    onPageChange: (Int) -> Unit = {},
    totalPages: Int = 1,
) {
    val appTheme = koinInject<AppTheme>()
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(appTheme.surface)
                .border(1.dp, appTheme.onSurfaceMuted.copy(alpha = 0.05f))
                .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "PAGE $currentPage OF $totalPages",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = appTheme.onSurfaceMuted,
            letterSpacing = 1.sp,
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            item {
                PaginationComponent(
                    modifier = Modifier.border(1.dp, appTheme.onSurfaceMuted.copy(alpha = 0.2f), CircleShape),
                    color = appTheme.onSurfaceMuted.copy(alpha = if (currentPage > 1) 0.1f else 0.005f),
                    enabled = currentPage > 1,
                    onClick = {
                        if (currentPage <= 1) return@PaginationComponent
                        onPageChange(currentPage - 1)
                    },
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Prev",
                        tint = appTheme.onSurfaceMuted,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            items(totalPages) { index ->
                val isActive = currentPage == index + 1
                PaginationComponent(
                    onClick = { onPageChange(index + 1) },
                    color = if (isActive) appTheme.primary else Color.Transparent,
                ) {
                    Text(
                        "${index + 1}",
                        color = if (isActive) Color.White else appTheme.onSurface,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            item {
                PaginationComponent(
                    modifier = Modifier.border(1.dp, appTheme.onSurfaceMuted.copy(alpha = 0.2f), CircleShape),
                    color = appTheme.onSurfaceMuted.copy(alpha = if (currentPage < totalPages) 0.1f else 0.005f),
                    enabled = currentPage < totalPages,
                    onClick = {
                        if (currentPage >= totalPages) return@PaginationComponent
                        onPageChange(currentPage + 1)
                    },
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = appTheme.onSurfaceMuted,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun PaginationComponent(
    color: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    composable: @Composable () -> Unit,
) {
    Box(
        modifier =
            modifier
                .size(24.dp)
                .clip(CircleShape)
                .clickable(enabled) { onClick() }
                .background(
                    color,
                    CircleShape,
                ),
        contentAlignment = Alignment.Center,
    ) {
        composable()
    }
}
