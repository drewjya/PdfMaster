package com.drewjya.pdfmaster.helper

import com.drewjya.pdfmaster.components.MessageType
import com.drewjya.pdfmaster.components.SnackbarMessage
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.Date
import org.openpdf.text.Document
import org.openpdf.text.Element
import org.openpdf.text.FontFactory
import org.openpdf.text.pdf.BaseFont
import org.openpdf.text.pdf.PdfCopy
import org.openpdf.text.pdf.PdfGState
import org.openpdf.text.pdf.PdfReader
import org.openpdf.text.pdf.PdfStamper

object PdfUtils {
    // ──────────────────────────────────────────────
// Entry point
// ──────────────────────────────────────────────

    private fun handleDirectory(directory: File): SnackbarMessage? {
        val directoryPath = directory.absolutePath

        try {
            Files.createDirectories(Paths.get(directoryPath))
            return null
        } catch (e: Exception) {
            return SnackbarMessage(
                MessageType.Error,
                "Failed to create directory",
                "Failed to create directory: ${e.message}",
            )
        }
    }

    fun processFiles(
        files: List<File>,
        configuration: OutputConfiguration,
    ): SnackbarMessage? {
        val outputDir = File(configuration.targetDirectory)
        var message = handleDirectory(outputDir)
        if (message != null) return message

        message = when (configuration.mode) {
            ProcessMode.Merge -> {
                try {
                    val mergedBytes = mergeFiles(files)
                    val enhanced = applyEnhancements(mergedBytes, configuration, totalPages = countPages(mergedBytes))
                    val outName =
                        configuration.mergeSettings.mergeName
                            .ifBlank { configuration.name }
                            .ensurePdfExtension()
                    File(outputDir, outName).writeBytes(enhanced)
                    null
                } catch (e: Exception) {
                    SnackbarMessage(
                        MessageType.Error,
                        "Failed to merge files",
                        "Failed to merge files: ${e.message}",
                    )
                }
            }

            ProcessMode.Batch -> {
                val batchConfig = configuration.batchSettings
                val date =
                    SimpleDateFormat(batchConfig.dateFormat.pattern)
                        .format(Date(batchConfig.selectedDate))

                val groupedOutputDir = File(outputDir, date)
                val msg = handleDirectory(groupedOutputDir)
                if (msg != null) return msg

                val groupedResult = files.groupBy { file ->
                    file.name.split(" - ").first().trim()
                }
                val listPrefixOrder = batchConfig.listPrefixOrder
                groupedResult.forEach { (name, files) ->
                    val sortedList = files.sortedBy { file ->
                        val priority = listPrefixOrder.indexOfFirst { prefix ->
                            file.name.contains(prefix, ignoreCase = true)
                        }
                        if (priority == -1) listPrefixOrder.size else priority
                    }

                    try {
                        val mergedBytes = mergeFiles(sortedList)
                        val enhanced =
                            applyEnhancements(mergedBytes, configuration, totalPages = countPages(mergedBytes))
                        val outName =
                            batchConfig.format
                                .replace("{identifier}", name)
                                .replace("{date}", date)
                                .replace("{variable}", batchConfig.variable)
                                .ifBlank { "$name - $date" }.ensurePdfExtension()
                        File(outputDir, outName).writeBytes(enhanced)
                        null
                    } catch (e: Exception) {
                        return SnackbarMessage(
                            MessageType.Error,
                            "Failed to merge files",
                            "Failed to merge files: ${e.message}",
                        )
                    }
                }
                null

            }

            ProcessMode.None -> {
                if (configuration.activeEnhancements.isEmpty()) {
                    SnackbarMessage(
                        MessageType.Error,
                        "No process selected",
                        "Please select at least one process",
                    )
                } else {

                    try {
                        files.forEach { file ->
                            val bytes = file.readBytes()
                            val enhanced = applyEnhancements(bytes, configuration, totalPages = countPages(bytes))
                            File(outputDir, file.name.ensurePdfExtension()).writeBytes(enhanced)
                        }
                        null
                    } catch (e: Exception) {
                        SnackbarMessage(
                            MessageType.Error,
                            "Failed to process files",
                            "Failed to process files: ${e.message}",
                        )
                    }
                }
            }
        }
        return message
    }

// ──────────────────────────────────────────────
// Merge
// ──────────────────────────────────────────────

    private fun mergeFiles(files: List<File>): ByteArray {
        val out = ByteArrayOutputStream()
        val document = Document()
        val copy = PdfCopy(document, out)
        document.open()

        files.forEach { file ->
            println("Merging ${file.absolutePath}")
            val reader = PdfReader(file.absolutePath)
            for (i in 1..reader.numberOfPages) {
                copy.addPage(copy.getImportedPage(reader, i))
            }
            reader.close()
        }

        document.close()
        return out.toByteArray()
    }

// ──────────────────────────────────────────────
// Enhancement dispatcher
// ──────────────────────────────────────────────

    private fun applyEnhancements(
        pdfBytes: ByteArray,
        configuration: OutputConfiguration,
        totalPages: Int,
    ): ByteArray {
        var current = pdfBytes

        if (EnhancementType.Identity in configuration.activeEnhancements) {
            current = applyIdentityWatermark(current, configuration.identitySettings)
        }

        if (EnhancementType.Numbering in configuration.activeEnhancements) {
            current = applyPageNumbering(current, configuration.numberingSettings, totalPages)
        }

        return current
    }

// ──────────────────────────────────────────────
// Identity / Watermark
// ──────────────────────────────────────────────

    private fun applyIdentityWatermark(
        pdfBytes: ByteArray,
        settings: IdentitySettings,
    ): ByteArray {
        val reader = PdfReader(pdfBytes)
        val out = ByteArrayOutputStream()
        val stamper = PdfStamper(reader, out)

        val baseFont = resolveBaseFont(settings.fontName)
        val javaColor = settings.color.toJavaColor()

        for (pageIndex in 1..reader.numberOfPages) {
            val pageSize = reader.getPageSizeWithRotation(pageIndex)
            val content = stamper.getOverContent(pageIndex)

            content.saveState()

            // Transparency
            val gState =
                PdfGState().apply {
                    setFillOpacity(settings.opacity)
                    setStrokeOpacity(settings.opacity)
                }
            content.setGState(gState)

            content.setColorFill(javaColor)
            content.setColorStroke(javaColor)
            content.beginText()
            content.setFontAndSize(baseFont, settings.fontSize.toFloat())

            val textWidth = baseFont.getWidthPoint(settings.text, settings.fontSize.toFloat())
            val textHeight = settings.fontSize.toFloat()

            val (cx, cy) =
                resolvePosition(
                    position = settings.position,
                    pageWidth = pageSize.width,
                    pageHeight = pageSize.height,
                    itemWidth = textWidth,
                    itemHeight = textHeight,
                )

            content.showTextAligned(
                Element.ALIGN_CENTER,
                settings.text,
                cx,
                cy,
                settings.rotation.toFloat(),
            )

            content.endText()
            content.restoreState()
        }

        stamper.close()
        reader.close()
        return out.toByteArray()
    }

// ──────────────────────────────────────────────
// Page Numbering
// ──────────────────────────────────────────────

    private fun applyPageNumbering(
        pdfBytes: ByteArray,
        settings: NumberingSettings,
        totalPages: Int,
    ): ByteArray {
        val reader = PdfReader(pdfBytes)
        val out = ByteArrayOutputStream()
        val stamper = PdfStamper(reader, out)

        val baseFont = resolveBaseFont(settings.fontName)
        val javaColor = settings.color.toJavaColor()

        for (pageIndex in 1..reader.numberOfPages) {
            val pageSize = reader.getPageSizeWithRotation(pageIndex)
            val content = stamper.getOverContent(pageIndex)

            val label =
                settings.format.value
                    .replace("{page}", pageIndex.toString())
                    .replace("{total}", totalPages.toString())

            content.saveState()
            content.setColorFill(javaColor)
            content.beginText()
            content.setFontAndSize(baseFont, settings.fontSize.toFloat())
            val textHeight = settings.fontSize.toFloat()
            val (finalX, finalY, align) =
                resolveNumberingPosition(
                    x = settings.x,
                    y = settings.y,
                    pageWidth = pageSize.width,
                    pageHeight = pageSize.height,
                    textHeight = textHeight,
                )

            content.showTextAligned(align, label, finalX, finalY, 0f)
            content.endText()
            content.restoreState()
        }

        stamper.close()
        reader.close()
        return out.toByteArray()
    }

// ──────────────────────────────────────────────
// Helpers
// ──────────────────────────────────────────────

    /** Count total pages without full enhancement overhead. */
    private fun countPages(pdfBytes: ByteArray): Int {
        val reader = PdfReader(pdfBytes)
        return reader.numberOfPages.also { reader.close() }
    }

    /**
     * Resolve the stamping position and text alignment for page numbering.
     *
     * x-axis  →  0 = horizontally centered
     *            positive (+N) = right edge, inset by N pt
     *            negative (-N) = left edge, inset by N pt (absolute value used)
     *
     * y-axis  →  0 = bottom edge (default, margin 20 pt)
     *            positive (+N) = top edge, inset by N pt
     *            negative (-N) = bottom edge, inset by N pt (absolute value used)
     *
     * Returns Triple(anchorX, anchorY, PdfContentByte alignment constant)
     * so that showTextAligned() places the text correctly without manual width math.
     */
    private data class NumberingPlacement(
        val x: Float,
        val y: Float,
        val align: Int,
    )

    private fun resolveNumberingPosition(
        x: Int,
        y: Int,
        pageWidth: Float,
        pageHeight: Float,
        textHeight: Float,
        edgeMargin: Float = 20f,
    ): NumberingPlacement {
        // ── Horizontal ────────────────────────────────────────────
        // showTextAligned uses an anchor point + alignment, so no manual width offset needed.
        val (anchorX, align) =
            when {
                x == 0 -> pageWidth / 2f to Element.ALIGN_CENTER

                x > 0 -> pageWidth - x.toFloat() to Element.ALIGN_RIGHT

                // right-anchored, inset by x
                else -> (-x).toFloat() to Element.ALIGN_LEFT // left-anchored,  inset by |x|
            }

        // ── Vertical ─────────────────────────────────────────────
        val anchorY =
            when {
                y == 0 -> edgeMargin

                // auto bottom
                y > 0 -> pageHeight - y.toFloat() - textHeight

                // top side, inset by y
                else -> (-y).toFloat() // bottom side, inset by |y|
            }

        return NumberingPlacement(anchorX, anchorY, align)
    }

    /**
     * Resolve (centerX, centerY) of a positioned element within a page,
     * given the element's bounding dimensions.
     */
    private fun resolvePosition(
        position: Position,
        pageWidth: Float,
        pageHeight: Float,
        itemWidth: Float,
        itemHeight: Float,
        margin: Float = 20f,
    ): Pair<Float, Float> =
        when (position) {
            Position.Center -> pageWidth / 2f to pageHeight / 2f
            Position.Top -> pageWidth / 2f to pageHeight - margin - itemHeight / 2f
            Position.Bottom -> pageWidth / 2f to margin + itemHeight / 2f
            Position.TopLeft -> margin + itemWidth / 2f to pageHeight - margin - itemHeight / 2f
            Position.TopRight -> pageWidth - margin - itemWidth / 2f to pageHeight - margin - itemHeight / 2f
            Position.BottomLeft -> margin + itemWidth / 2f to margin + itemHeight / 2f
            Position.BottomRight -> pageWidth - margin - itemWidth / 2f to margin + itemHeight / 2f
        }

    /** Resolve a BaseFont from an optional family name, falling back to Helvetica. */
    private fun resolveBaseFont(fontName: String): BaseFont {

        val name = fontName.trim()

        // 1. Handle empty name immediately
        if (name.isEmpty()) {
            return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED)
        }

        return runCatching {
            // 2. Check FontFactory Registry first (this handles your registered Noto Sans, etc.)
            if (FontFactory.isRegistered(name)) {
                val font = FontFactory.getFont(name)
                font.baseFont ?: BaseFont.createFont(name, BaseFont.IDENTITY_H, BaseFont.EMBEDDED)
            } else {
                // 3. If not registered, try to load as a direct file path or built-in name
                BaseFont.createFont(name, BaseFont.IDENTITY_H, BaseFont.EMBEDDED)
            }
        }.getOrElse {
            println("Failed to load IDENTITY_H for: $name - trying WINANSI")

            runCatching {
                // 4. Fallback for built-in standard fonts (Helvetica, Times, etc.)
                BaseFont.createFont(name, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED)
            }.getOrElse {
                println("Failed all lookups for: $name - defaulting to Helvetica")
                BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED)
            }
        }
    }

    private fun String.ensurePdfExtension(): String = if (endsWith(".pdf", ignoreCase = true)) this else "$this.pdf"

    /** Convert Compose Color → java.awt.Color */
    private fun androidx.compose.ui.graphics.Color.toJavaColor(): java.awt.Color =
        java.awt.Color(red, green, blue, alpha)
}
