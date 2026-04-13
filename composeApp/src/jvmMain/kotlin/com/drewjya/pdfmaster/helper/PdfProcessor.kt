package com.drewjya.pdfmaster.helper

import androidx.compose.ui.graphics.Color
import java.io.File
import org.apache.pdfbox.Loader
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.PDResources
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState
import org.apache.pdfbox.util.Matrix
import kotlin.math.cos
import kotlin.math.sin

enum class ProcessType {
    Watermark,
    Numbering,
    All,
}

enum class Position {
    Top,
    Bottom,
    Center,
    TopLeft,
    TopRight,
    BottomLeft,
    BottomRight,
}

enum class PageFormat(
    val value: String,
) {
    Page("{page}"),
    PageSize("{page} of {total}"),
    All("Page {page} of {total}"),
}

data class NumberPosition(
    val x: Int = 0,
    val y: Int = 0,
)

enum class DatePattern(
    val pattern: String,
) {
    Date("dd-MM-yyyy"),
    Long("dd MMMM yyyy"),
    Iso("yyyy-MM-dd"),
}

data class PdfConfig(
    val type: ProcessType = ProcessType.All,
    val watermarkText: String = "",
    val watermarkFontSize: Int = 60,
    val numberingFontSize: Int = 12,
    val pageFormat: PageFormat = PageFormat.All,
    val color: Color = Color.Black,
    val rotation: Double = 45.toDouble(),
    val font: Standard14Fonts.FontName = Standard14Fonts.FontName.HELVETICA_BOLD,
    val position: Position = Position.Center,
    val opacity: Float = 30f,
    val numberPosition: NumberPosition = NumberPosition(),
)

object PdfProcessor {
    fun batchMergePdfs(
        sourceFiles: List<File>,
        outputDirectoryPath: String,
        pattern: DatePattern,
        selectedDate: Long,
    ) {
        val date = formatDate(selectedDate, pattern)
        val parentDir = File(outputDirectoryPath)
        val outputDirectory = File(parentDir, date)
        println("outputDirectory: ${outputDirectory.absolutePath}")
        if (!outputDirectory.exists()) {
            val created = outputDirectory.mkdirs()
            if (created) {
                println("Created new directory at: ${outputDirectory.absolutePath}")
            } else {
                println("Warning: Failed to create directory. Path might be invalid or lacking permissions.")
            }
        }
        val groupedResults =
            sourceFiles.groupBy { file ->
                file.name
                    .split(" - ")
                    .first()
                    .trim()
            }

        groupedResults.forEach { (name, files) ->
            val sortedList =
                files.sortedBy { file ->
                    val fileName = file.name
                    when {
                        fileName.contains("Portofolio Activity", ignoreCase = true) -> 1
                        fileName.contains("Statement Position", ignoreCase = true) -> 3
                        else -> 2 // 'other' or any other file
                    }
                }

            mergePdfs(
                sourceFiles = sortedList,
                outputDirectoryPath = outputDirectory.absolutePath,
                outputFileName = "$name - Statement $date.pdf",
            )
        }
    }

    fun mergePdfs(
        sourceFiles: List<File>,
        outputDirectoryPath: String,
        outputFileName: String,
    ) {
        val outputDirectory = File(outputDirectoryPath)

        if (!outputDirectory.exists()) {
            val created = outputDirectory.mkdirs()
            if (created) {
                println("Created new directory at: ${outputDirectory.absolutePath}")
            } else {
                println("Warning: Failed to create directory. Path might be invalid or lacking permissions.")
            }
        }

        val finalFileName =
            if (outputFileName.endsWith(".pdf", ignoreCase = true)) {
                outputFileName
            } else {
                "$outputFileName.pdf"
            }

        val destinationFile = File(outputDirectory, finalFileName)
        val merger = PDFMergerUtility()
        merger.destinationFileName = destinationFile.absolutePath

        sourceFiles.forEach { file -> merger.addSource(file) }

        try {
            merger.mergeDocuments(null)
            println("Successfully merged ${sourceFiles.size} PDFs into ${destinationFile.absolutePath}")
        } catch (e: Exception) {
            println("Failed to merge PDFs: ${e.message}")
        }
    }

    fun batchProcess(
        inputFiles: List<File>,
        outputDirectoryPath: String,
        config: PdfConfig,
    ) {
        val outputDirectory = File(outputDirectoryPath)

        if (!outputDirectory.exists()) {
            val created = outputDirectory.mkdirs()
            if (created) {
                println("Created new directory at: ${outputDirectory.absolutePath}")
            } else {
                println("Warning: Failed to create directory. Path might be invalid or lacking permissions.")
            }
        }

        inputFiles.forEach { inputFile ->
            val outputFileName = "${inputFile.nameWithoutExtension}.pdf"
            val outputFile = File(outputDirectory, outputFileName)

            try {
                processSinglePdf(inputFile, outputFile, config)
                println("Successfully processed: ${inputFile.name}")
            } catch (e: Exception) {
                println("Failed to process ${inputFile.name}: ${e.message}")
            }
        }
    }

    private fun processSinglePdf(
        inputFile: File,
        outputFile: File,
        config: PdfConfig,
    ) {
        Loader.loadPDF(inputFile).use { document ->

            val fontType = PDType1Font(config.font)
            val targetPages = resolveTargetPages(document)

            val numberingFontSize = config.numberingFontSize.toFloat()

            val totalPages = document.numberOfPages

            for (index in targetPages) {
                val page = document.getPage(index)
                val resources = page.resources ?: PDResources().also { page.resources = it }
                resources.add(fontType)
                resources.add(fontType)
                val width = page.mediaBox.width
                val height = page.mediaBox.height
                val watermarkText = config.watermarkText

                PDPageContentStream(
                    document,
                    page,
                    PDPageContentStream.AppendMode.APPEND,
                    true,
                    false,
                ).use { contentStream ->
                    if (config.type != ProcessType.Numbering && watermarkText.isNotBlank()) {
                        stampPage(document, page, fontType, config)
//                        stampAsImage(document, page, fontType, config)
                    }

                    if (config.type != ProcessType.Watermark) {
                        contentStream.saveGraphicsState()
                        contentStream.setFont(fontType, numberingFontSize)
                        contentStream.setNonStrokingColor(
                            config.color.red,
                            config.color.green,
                            config.color.blue,
                        )

                        val pageText =
                            config.pageFormat.value
                                .replace("{page}", "${index + 1}")
                                .replace("{total}", "$totalPages")

                        val textWidth = fontType.getStringWidth(pageText) / 1000f * numberingFontSize
                        val textHeight = fontType.fontDescriptor.capHeight / 1000f * numberingFontSize

                        val marginX = width * (config.numberPosition.x / 100f)
                        val marginY = height * (config.numberPosition.y / 100f)

                        val x = width - marginX - textWidth
                        val y = height - marginY - textHeight

                        contentStream.beginText()
                        contentStream.setTextMatrix(Matrix.getTranslateInstance(x, y))
                        contentStream.showText(pageText)
                        contentStream.endText()

                        contentStream.restoreGraphicsState()
                    }
                }
            }

            document.save(outputFile)
        }
    }

    /** Convert 1-based page list to 0-based indices; null/empty → all pages. */
    private fun resolveTargetPages(doc: PDDocument): List<Int> {
        val totalPages = doc.numberOfPages
        return (0 until totalPages).toList()
    }

    /**
     * Stamps a single [page] with the configured watermark text.
     * Uses APPEND mode so existing content is preserved.
     */
    private fun stampPage(
        doc: PDDocument,
        page: PDPage,
        font: PDFont,
        config: PdfConfig,
    ) {
        val mediaBox = page.mediaBox
        val pageWidth = mediaBox.width
        val pageHeight = mediaBox.height

        val text = if (config.type == ProcessType.Watermark) config.watermarkText else config.pageFormat.value
        val fontSize =
            (if (config.type == ProcessType.Watermark) config.watermarkFontSize else config.numberingFontSize).toFloat()

        val textWidth = font.getStringWidth(text) / 1000f * config.watermarkFontSize

        PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true).use { cs ->

            val gs =
                PDExtendedGraphicsState().apply {
                    nonStrokingAlphaConstant = config.opacity
                    strokingAlphaConstant = config.opacity
                }
            cs.setGraphicsStateParameters(gs)

            val color = config.color
            cs.setNonStrokingColor(color.red, color.green, color.blue)

            cs.setFont(font, fontSize)

            val (anchorX, anchorY) = computeAnchor(pageWidth, pageHeight, textWidth, config)
            stampSingle(cs, anchorX, anchorY, config)
        }
    }

    private fun computeAnchor(
        pageWidth: Float,
        pageHeight: Float,
        textWidth: Float,
        config: PdfConfig,
    ): Pair<Float, Float> {
        val fontSize =
            (if (config.type == ProcessType.Watermark) config.watermarkFontSize else config.numberingFontSize).toFloat()
        val radians = Math.toRadians(config.rotation)
        val margin = 36f

        val hw = textWidth / 2f
        val hh = fontSize / 2f

        val cosA = cos(radians).toFloat()
        val sinA = sin(radians).toFloat()

        val startOffsetX = -(hw * cosA - hh * sinA)
        val startOffsetY = -(hw * sinA + hh * cosA)

        val (cx, cy) =
            when (config.position) {
                Position.Center -> Pair(pageWidth / 2f, pageHeight / 2f)

                Position.TopLeft -> Pair(margin, pageHeight - margin)

                Position.Top -> Pair(pageWidth / 2f, pageHeight - margin)

                Position.TopRight -> Pair(pageWidth - margin, pageHeight - margin)

                //            Position.MIDDLE_LEFT   -> Pair(margin,                  pageHeight / 2f)
//            Position.MIDDLE_RIGHT  -> Pair(pageWidth - margin,      pageHeight / 2f)
                Position.BottomLeft -> Pair(margin, margin)

                Position.Bottom -> Pair(pageWidth / 2f, margin)

                Position.BottomRight -> Pair(pageWidth - margin, margin)
//            WatermarkPosition.CUSTOM        -> Pair(config.customX,          config.customY)
            }

        return Pair(cx + startOffsetX, cy + startOffsetY)
    }

    private fun stampSingle(
        cs: PDPageContentStream,
        anchorX: Float,
        anchorY: Float,
        config: PdfConfig,
    ) {
        val theta = Math.toRadians(config.rotation)
        val cosT = cos(theta).toFloat()
        val sinT = sin(theta).toFloat()

        cs.beginText()
        val matrix =
            Matrix(
                // a
                cosT, // b
                sinT,
                // c
                -sinT, // d
                cosT,
                // e
                anchorX, // f
                anchorY,
            )

        cs.setTextMatrix(matrix)
        cs.showText(config.watermarkText)
        cs.endText()
    }
}
