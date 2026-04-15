package com.drewjya.pdfmaster.helper


import com.lowagie.text.Document
import com.lowagie.text.Element
import com.lowagie.text.pdf.BaseFont
import com.lowagie.text.pdf.PdfCopy
import com.lowagie.text.pdf.PdfGState
import com.lowagie.text.pdf.PdfReader
import com.lowagie.text.pdf.PdfStamper
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


object PdfUtils {

// ──────────────────────────────────────────────
// Entry point
// ──────────────────────────────────────────────

    fun processFiles(files: List<File>, configuration: OutputConfiguration) {
        require(files.isNotEmpty()) { "File list must not be empty" }

        val outputDir = File(configuration.targetDirectory).also { it.mkdirs() }

        when (configuration.mode) {
            ProcessMode.Merge -> {
                val mergedBytes = mergeFiles(files)
                val enhanced = applyEnhancements(mergedBytes, configuration, totalPages = countPages(mergedBytes))
                val outName = configuration.mergeSettings.mergeName
                    .ifBlank { configuration.name }
                    .ensurePdfExtension()
                File(outputDir, outName).writeBytes(enhanced)
            }

            ProcessMode.Batch -> {
                val date = SimpleDateFormat(configuration.batchSettings.dateFormat.pattern)
                    .format(Date(configuration.batchSettings.selectedDate))

                files.forEach { file ->
                    val bytes = file.readBytes()
                    val enhanced = applyEnhancements(bytes, configuration, totalPages = countPages(bytes))
                    val outName = "${date}_${file.nameWithoutExtension}.pdf"
                    File(outputDir, outName).writeBytes(enhanced)
                }
            }

            ProcessMode.None -> {
                files.forEach { file ->
                    val bytes = file.readBytes()
                    val enhanced = applyEnhancements(bytes, configuration, totalPages = countPages(bytes))
                    File(outputDir, file.name.ensurePdfExtension()).writeBytes(enhanced)
                }
            }
        }
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

    private fun applyIdentityWatermark(pdfBytes: ByteArray, settings: IdentitySettings): ByteArray {
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
            val gState = PdfGState().apply {
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

            val (cx, cy) = resolvePosition(
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

            val label = settings.format.value
                .replace("{page}", pageIndex.toString())
                .replace("{total}", totalPages.toString())

            content.saveState()
            content.setColorFill(javaColor)
            content.beginText()
            content.setFontAndSize(baseFont, settings.fontSize.toFloat())

            val textWidth = baseFont.getWidthPoint(label, settings.fontSize.toFloat())
            val textHeight = settings.fontSize.toFloat()
            val (finalX, finalY, align) = resolveNumberingPosition(
                x = settings.x,
                y = settings.y,
                pageWidth = pageSize.width,
                pageHeight = pageSize.height,
                textWidth = textWidth,
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
    private data class NumberingPlacement(val x: Float, val y: Float, val align: Int)

    private fun resolveNumberingPosition(
        x: Int,
        y: Int,
        pageWidth: Float,
        pageHeight: Float,
        textWidth: Float,
        textHeight: Float,
        edgeMargin: Float = 20f,
    ): NumberingPlacement {
        // ── Horizontal ────────────────────────────────────────────
        // showTextAligned uses an anchor point + alignment, so no manual width offset needed.
        val (anchorX, align) = when {
            x == 0 -> pageWidth / 2f to Element.ALIGN_CENTER
            x > 0 -> pageWidth - x.toFloat() to Element.ALIGN_RIGHT  // right-anchored, inset by x
            else -> (-x).toFloat() to Element.ALIGN_LEFT   // left-anchored,  inset by |x|
        }

        // ── Vertical ─────────────────────────────────────────────
        val anchorY = when {
            y == 0 -> edgeMargin                                      // auto bottom
            y > 0 -> pageHeight - y.toFloat() - textHeight           // top side, inset by y
            else -> (-y).toFloat()                                  // bottom side, inset by |y|
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
    ): Pair<Float, Float> = when (position) {
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
        return if (name.isNotEmpty()) {
            runCatching {
                BaseFont.createFont(name, BaseFont.IDENTITY_H, BaseFont.EMBEDDED)
            }.getOrElse {
                // Try as a built-in name
                runCatching {
                    BaseFont.createFont(name, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED)
                }.getOrElse {
                    BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED)
                }
            }
        } else {
            BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED)
        }
    }

    private fun String.ensurePdfExtension(): String =
        if (endsWith(".pdf", ignoreCase = true)) this else "$this.pdf"

    /** Convert Compose Color → java.awt.Color */
    private fun androidx.compose.ui.graphics.Color.toJavaColor(): java.awt.Color =
        java.awt.Color(red, green, blue, alpha)
}