package com.drewjya.pdfmaster.helper
//package com.drewjya.pdfmaster.helper
//
//import androidx.compose.ui.graphics.Color
//import com.drewjya.pdfmaster.components.MessageType
//import com.drewjya.pdfmaster.components.SnackbarMessage
//import org.apache.pdfbox.Loader
//import org.apache.pdfbox.multipdf.PDFMergerUtility
//import org.apache.pdfbox.pdmodel.PDDocument
//import org.apache.pdfbox.pdmodel.PDPageContentStream
//import org.apache.pdfbox.pdmodel.font.PDType1Font
//import org.apache.pdfbox.pdmodel.font.Standard14Fonts
//import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState
//import org.apache.pdfbox.util.Matrix
//import java.io.File
//import java.nio.file.Files
//import java.nio.file.Paths
//import kotlin.math.cos
//import kotlin.math.sin
//
//enum class ProcessType {
//    Watermark,
//    Numbering,
//    All,
//}
//
//class PdfConfig(
//    val type: ProcessType = ProcessType.All,
//    val watermarkText: String = "",
//    val watermarkFontSize: Int = 60,
//    val numberingFontSize: Int = 12,
//    val pageFormat: PageFormat = PageFormat.All,
//    val color: Color = Color.Black,
//    val rotation: Double = 45.toDouble(),
//    val font: Standard14Fonts.FontName = Standard14Fonts.FontName.HELVETICA_BOLD,
//    val position: Position = Position.Center,
//    val opacity: Float = 0.3f,
//    val numberPosition: NumberPosition = NumberPosition(),
//)
//
//object PdfProcessor {
//    private fun handleDirectory(directory: File): SnackbarMessage? {
//        val directoryPath = directory.absolutePath
//
//        try {
//            Files.createDirectories(Paths.get(directoryPath))
//            return null
//        } catch (e: Exception) {
//            return SnackbarMessage(
//                MessageType.Error,
//                "Failed to create directory",
//                "Failed to create directory: ${e.message}",
//            )
//        }
//    }
//
//    fun batchMergePdfs(
//        sourceFiles: List<File>,
//        outputDirectoryPath: String,
//        pattern: DatePattern,
//        selectedDate: Long,
//    ): SnackbarMessage {
//        val date = formatDate(selectedDate, pattern)
//        val parentDir = File(outputDirectoryPath)
//        val outputDirectory = File(parentDir, date)
//        val message = handleDirectory(outputDirectory)
//        if (message != null) {
//            return message
//        }
//
//        val groupedResults =
//            sourceFiles.groupBy { file ->
//                file.name
//                    .split(" - ")
//                    .first()
//                    .trim()
//            }
//
//        groupedResults.forEach { (name, files) ->
//            val sortedList =
//                files.sortedBy { file ->
//                    val fileName = file.name
//                    when {
//                        fileName.contains("Portfolio Activity", ignoreCase = true) -> 1
//                        fileName.contains("Statement Position", ignoreCase = true) -> 3
//                        else -> 2 // 'other' or any other file
//                    }
//                }
//
//            mergePdfs(
//                sourceFiles = sortedList,
//                outputDirectoryPath = outputDirectory.absolutePath,
//                outputFileName = "$name - Statement $date.pdf",
//            )
//        }
//        return SnackbarMessage(
//            MessageType.Success,
//            "Merge Success",
//            "Successfully merged PDFs to ${outputDirectory.absolutePath}",
//        )
//    }
//
//    fun mergePdfs(
//        sourceFiles: List<File>,
//        outputDirectoryPath: String,
//        outputFileName: String,
//    ): SnackbarMessage {
//        val outputDirectory = File(outputDirectoryPath)
//
//        val message = handleDirectory(outputDirectory)
//        if (message != null) {
//            return message
//        }
//
//        val finalFileName =
//            if (outputFileName.endsWith(".pdf", ignoreCase = true)) {
//                outputFileName
//            } else {
//                "$outputFileName.pdf"
//            }
//
//        val destinationFile = File(outputDirectory, finalFileName)
//        val merger = PDFMergerUtility()
//        merger.destinationFileName = destinationFile.absolutePath
//
//        sourceFiles.forEach { file -> merger.addSource(file) }
//
//        // Use a document to manage merging to ensure resources are handled correctly in PDFBox 3
//        try {
//            PDDocument().use { destinationDoc ->
//                sourceFiles.forEach { file ->
//                    Loader.loadPDF(file).use { sourceDoc ->
//                        merger.appendDocument(destinationDoc, sourceDoc)
//                    }
//                }
//                destinationDoc.save(destinationFile)
//            }
//        } catch (e: Exception) {
//            return SnackbarMessage(MessageType.Error, "Failed to merge PDFs", "Failed to merge PDFs: ${e.message}")
//        }
//
//        return SnackbarMessage(
//            MessageType.Success,
//            "Merge Success",
//            "Successfully merged ${sourceFiles.size} PDFs into ${destinationFile.absolutePath}",
//        )
//    }
//
//    fun batchProcess(
//        inputFiles: List<File>,
//        outputDirectoryPath: String,
//        config: PdfConfig,
//    ): SnackbarMessage {
//        val outputDirectory = File(outputDirectoryPath)
//
//        val message = handleDirectory(outputDirectory)
//        if (message != null) {
//            return message
//        }
//
//        var success = 0
//        var failed: String? = null
//
//        inputFiles.forEach { inputFile ->
//            val outputFileName = "${inputFile.nameWithoutExtension}.pdf"
//            val outputFile = File(outputDirectory, outputFileName)
//
//            try {
//                processSinglePdf(inputFile, outputFile, config)
//                success += 1
//            } catch (e: Exception) {
//                failed = "Failed to process ${inputFile.name}: ${e.message}"
//            }
//        }
//
//        return SnackbarMessage(
//            if (failed == null) MessageType.Success else MessageType.Error,
//            title = if (failed == null) "Process Success" else "Process Failed",
//            message = failed ?: "Successfully processed $success PDFs",
//        )
//    }
//
//    private fun processSinglePdf(
//        inputFile: File,
//        outputFile: File,
//        config: PdfConfig,
//    ) {
//        Loader.loadPDF(inputFile).use { document ->
//            val fontType = PDType1Font(config.font)
//            val targetPages = resolveTargetPages(document)
//            val totalPages = document.numberOfPages
//
//            for (index in targetPages) {
//                val page = document.getPage(index)
//
//                val rotation = page.rotation
//
//                // Get actual visible dimensions
//                val pageSize = page.mediaBox
//                val actualWidth = if (rotation == 90 || rotation == 270) pageSize.height else pageSize.width
//                val actualHeight = if (rotation == 90 || rotation == 270) pageSize.width else pageSize.height
//
//                PDPageContentStream(
//                    document,
//                    page,
//                    PDPageContentStream.AppendMode.APPEND,
//                    true,
//                    true, // Set resetContext to true to prevent state bleeding
//                ).use { contentStream ->
//
//                    // Explicit matrices for mapping the visual coordinate system
//                    if (rotation != 0) {
//                        when (rotation) {
//                            90 -> contentStream.transform(Matrix(0f, 1f, -1f, 0f, pageSize.width, 0f))
//                            180 -> contentStream.transform(Matrix(-1f, 0f, 0f, -1f, pageSize.width, pageSize.height))
//                            270 -> contentStream.transform(Matrix(0f, -1f, 1f, 0f, 0f, pageSize.height))
//                        }
//                    }
//
//                    // --- WATERMARK SECTION ---
//                    if (config.type != ProcessType.Numbering && config.watermarkText.isNotBlank()) {
//                        contentStream.saveGraphicsState()
//
//                        val gs =
//                            PDExtendedGraphicsState().apply {
//                                nonStrokingAlphaConstant = config.opacity
//                                strokingAlphaConstant = config.opacity
//                            }
//                        contentStream.setGraphicsStateParameters(gs)
//                        contentStream.setNonStrokingColor(config.color.red, config.color.green, config.color.blue)
//                        contentStream.setFont(fontType, config.watermarkFontSize.toFloat())
//
//                        val textWidth = fontType.getStringWidth(config.watermarkText) / 1000f * config.watermarkFontSize
//                        val (anchorX, anchorY) =
//                            computeAnchor(
//                                actualWidth, // Using normalized width
//                                actualHeight, // Using normalized height
//                                textWidth,
//                                config,
//                            )
//
//                        applyStampToStream(contentStream, anchorX, anchorY, config)
//
//                        contentStream.restoreGraphicsState()
//                    }
//
//                    // --- NUMBERING SECTION ---
//                    if (config.type != ProcessType.Watermark) {
//                        contentStream.saveGraphicsState()
//
//                        val numberingFontSize = config.numberingFontSize.toFloat()
//                        contentStream.setFont(fontType, numberingFontSize)
//                        contentStream.setNonStrokingColor(config.color.red, config.color.green, config.color.blue)
//
//                        val pageText =
//                            config.pageFormat.value
//                                .replace("{page}", "${index + 1}")
//                                .replace("{total}", "$totalPages")
//
//                        val textWidth = fontType.getStringWidth(pageText) / 1000f * numberingFontSize
//                        val textHeight = fontType.fontDescriptor.capHeight / 1000f * numberingFontSize
//
//                        // FIXED: Use actualWidth and actualHeight here!
//                        val x = actualWidth - (actualWidth * (config.numberPosition.x / 100f)) - textWidth
//                        val y = actualHeight - (actualHeight * (config.numberPosition.y / 100f)) - textHeight
//
//                        contentStream.beginText()
//                        contentStream.setTextMatrix(Matrix.getTranslateInstance(x, y))
//                        contentStream.showText(pageText)
//                        contentStream.endText()
//
//                        contentStream.restoreGraphicsState()
//                    }
//                }
//            }
//            document.save(outputFile)
//        }
//    }
//
//    /** Convert 1-based page list to 0-based indices; null/empty → all pages. */
//    private fun resolveTargetPages(doc: PDDocument): List<Int> {
//        val totalPages = doc.numberOfPages
//        return (0 until totalPages).toList()
//    }
//
//    private fun computeAnchor(
//        pageWidth: Float,
//        pageHeight: Float,
//        textWidth: Float,
//        config: PdfConfig,
//    ): Pair<Float, Float> {
//        val fontSize =
//            (if (config.type == ProcessType.Watermark) config.watermarkFontSize else config.numberingFontSize).toFloat()
//        val radians = Math.toRadians(config.rotation)
//        val margin = 36f
//
//        val hw = textWidth / 2f
//        val hh = fontSize / 2f
//
//        val cosA = cos(radians).toFloat()
//        val sinA = sin(radians).toFloat()
//
//        // This perfectly calculates the offset needed to center the bounding box of the rotated text
//        val startOffsetX = -(hw * cosA - hh * sinA)
//        val startOffsetY = -(hw * sinA + hh * cosA)
//
//        val (cx, cy) =
//            when (config.position) {
//                Position.Center -> Pair(pageWidth / 2f, pageHeight / 2f)
//
//                // Keep it simple!
//                Position.Top -> Pair(pageWidth / 2f, pageHeight - margin)
//
//                Position.TopLeft -> Pair(margin, pageHeight - margin)
//
//                Position.TopRight -> Pair(pageWidth - margin, pageHeight - margin)
//
//                Position.BottomLeft -> Pair(margin, margin)
//
//                Position.Bottom -> Pair(pageWidth / 2f, margin)
//
//                Position.BottomRight -> Pair(pageWidth - margin, margin)
//            }
//
//        return Pair(cx + startOffsetX, cy + startOffsetY)
//    }
//
//    private fun applyStampToStream(
//        cs: PDPageContentStream,
//        anchorX: Float,
//        anchorY: Float,
//        config: PdfConfig,
//    ) {
//        val theta = Math.toRadians(config.rotation)
//        val cosT = cos(theta).toFloat()
//        val sinT = sin(theta).toFloat()
//
//        cs.beginText()
//        val matrix = Matrix(cosT, sinT, -sinT, cosT, anchorX, anchorY)
//        cs.setTextMatrix(matrix)
//        cs.showText(config.watermarkText)
//        cs.endText()
//    }
//}
