package com.drewjya.pdfmaster

import androidx.compose.ui.graphics.vector.ImageVector
import com.drewjya.pdfmaster.design.AppIcon

enum class Screen(
    val title: String,
    val description: String = "",
    val icon: ImageVector,
) {
    Merge(
        title = "Merge PDFs",
        description = "Combine multiple PDF files into a single document.",
        icon = AppIcon.Merge,
    ),
    Watermark(
        title = "Batch Watermark",
        description = "Add a watermark to multiple PDF files.",
        icon = AppIcon.Watermark,
    ),
    Numbering(
        title = "Batch Numbering",
        description = "Add page numbers to multiple PDF files.",
        icon = AppIcon.Number,
    ),
    Files(
        title = "Files",
        description = "Manage your PDF files.",
        icon = AppIcon.File,
    ),
}
