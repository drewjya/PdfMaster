package com.drewjya.pdfmaster.componentv2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import java.io.File
import java.math.BigDecimal
import org.koin.compose.koinInject
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

@Composable
fun FileStagingPane(modifier: Modifier = Modifier) {
    val pdfViewModel: PdfViewModel = koinInject()
    val files by pdfViewModel.pdfFiles.collectAsStateWithLifecycle()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isCompact = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
    Column(
        modifier = modifier
            .background(MaroonSurfaceAlt.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .border(1.dp, MaroonSecondary.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
    ) {
        if (files.isEmpty()) {
            EmptyStateView(
                modifier = if (!isCompact) Modifier.fillMaxSize() else Modifier.height(164.dp),
            )
        } else {

            val ceiling = ceil(files.size.toBigDecimal().divide(BigDecimal("10")).toDouble()).toInt()
            val totalPages = max(1, ceiling)
            val currentPage = remember { mutableStateOf(1) }
            val startIdx = (currentPage.value - 1) * 10
            val endIdx = min(startIdx + 10, files.size) - 1
            val visibleFiles = files.slice(startIdx..endIdx)

            if (isCompact) {
                LazyRow(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(visibleFiles) { index, file ->
                        val actualIndex = startIdx + index + 1
                        Column(
                            modifier = Modifier.clip(
                                RoundedCornerShape(12.dp)
                            ).size(140.dp)
                                .border(
                                    1.dp,
                                    MaroonOnSurfaceMuted.copy(alpha = 0.2f),
                                    RoundedCornerShape(12.dp)
                                ).background(
                                    Color.White,
                                ).padding(all = 8.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Box(
                                    modifier = Modifier.size(24.dp).background(
                                        MaroonSecondary.copy(
                                            alpha = 0.1f,
                                        ), RoundedCornerShape(4.dp)
                                    ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = actualIndex.toString().padStart(2, '0'),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaroonOnSurfaceMuted
                                    )
                                }

                                Box(
                                    modifier = Modifier.size(24.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .clickable {
                                            pdfViewModel.removeFile(file)
                                        }.background(
                                            MaroonSecondary.copy(
                                                alpha = 0.1f,
                                            )
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        com.drewjya.pdfmaster.design.Icons.Trash,
                                        contentDescription = null,
                                        tint = MaroonError,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    com.drewjya.pdfmaster.design.Icons.Pdf,
                                    contentDescription = null,
                                    tint = MaroonPrimary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Column {

                                Text(
                                    text = file.name,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaroonOnSurface,
                                    maxLines = 1,
                                    lineHeight = 12.sp,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    formatBytes(file.length()),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaroonOnSurfaceMuted.copy(alpha = 0.8f),
                                )
                            }
                        }
                    }
                }

            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(visibleFiles) { index, file ->
                        val actualIndex = startIdx + index + 1
                        FileRailItem(actualIndex, file)
                    }
                }

            }
            PaginationSystem(
                currentPage = currentPage.value,
                onPageChange = { newPage -> currentPage.value = newPage },
                totalPages = totalPages
            )


        }
    }
}

@Composable
fun EmptyStateView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

        ) {
        Box(
            modifier = Modifier.size(48.dp).background(Color.White, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(com.drewjya.pdfmaster.design.Icons.Empty, contentDescription = null, tint = MaroonPrimary)
        }

        Column {
            Text(
                text = "Drag and drop your PDFs, or",
                fontSize = 13.sp,
                color = MaroonOnSurfaceMuted,
                modifier = Modifier.widthIn(300.dp),
                textAlign = TextAlign.Center,
            )
            Text(
                text = "browse your local directory to begin.",
                fontSize = 13.sp,
                color = MaroonOnSurfaceMuted,
                modifier = Modifier.widthIn(300.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun FileRailItem(
    index: Int,
    file: File,
) {
    val pdfViewModel: PdfViewModel = koinInject()
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaroonSurface, RoundedCornerShape(12.dp))
                .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = index.toString().padStart(2, '0'),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaroonOnSurfaceMuted,
            modifier = Modifier.width(32.dp),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                file.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaroonOnSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                formatBytes(file.length()),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaroonOnSurfaceMuted.copy(alpha = 0.8f),
            )
        }
//        IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
//            Icon(
//                OldIcon.Default.Delete,
//                contentDescription = "Remove",
//                tint = MaroonError,
//                modifier = Modifier.size(16.dp)
//            )
//        }
    }
}

@Composable
fun PaginationSystem(
    currentPage: Int,
    onPageChange: (Int) -> Unit = {},
    totalPages: Int = 1,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaroonSurface)
                .border(1.dp, Color.Black.copy(alpha = 0.05f))
                .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "PAGE $currentPage OF $totalPages",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = MaroonOnSurfaceMuted,
            letterSpacing = 1.sp,
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            item {
                PaginationComponent(
                    modifier = Modifier.border(1.dp, MaroonOnSurfaceMuted.copy(alpha = 0.2f), CircleShape),
                    color = MaroonOnSurfaceMuted.copy(alpha = 0.05f),
                    enabled = currentPage > 1,
                    onClick = {
                        if (currentPage <= 1) return@PaginationComponent
                        onPageChange(currentPage - 1)
                    }) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Prev",
                        tint = MaroonOnSurfaceMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }

            }

            items(totalPages) { index ->
                val isActive = currentPage == index + 1
                PaginationComponent(
                    onClick = { onPageChange(index + 1) },
                    color = if (isActive) MaroonPrimary else Color.Transparent,
                ) {
                    Text(
                        "${index + 1}",
                        color = if (isActive) Color.White else MaroonOnSurface,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            item {
                PaginationComponent(
                    modifier = Modifier.border(1.dp, MaroonOnSurfaceMuted.copy(alpha = 0.2f), CircleShape),
                    color = MaroonOnSurfaceMuted.copy(
                        alpha =
                            if (currentPage < totalPages) 0.1f else 0.005f
                    ),
                    enabled = currentPage < totalPages,
                    onClick = {
                        if (currentPage >= totalPages) return@PaginationComponent
                        onPageChange(currentPage + 1)
                    }) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = MaroonOnSurfaceMuted,
                        modifier = Modifier.size(16.dp)
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
    composable: @Composable () -> Unit
) {
    Box(
        modifier =
            modifier.size(24.dp).clip(CircleShape)
                .clickable(enabled) { onClick() }.background(
                    color, CircleShape
                ),
        contentAlignment = Alignment.Center,
    ) {
        composable()
    }
}