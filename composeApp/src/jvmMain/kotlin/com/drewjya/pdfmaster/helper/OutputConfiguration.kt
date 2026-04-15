package com.drewjya.pdfmaster.helper

import androidx.compose.ui.graphics.Color
import com.lowagie.text.FontFactory
import java.util.UUID
import kotlinx.serialization.Serializable


fun getAvailableFonts(): List<String> {
    // 1. Scan standard system font directories (Windows, macOS, Linux)
    FontFactory.registerDirectories()

    // 2. Return the names of all discovered fonts
    return (FontFactory.getRegisteredFonts().toList()).sortedBy { it }
}

@Serializable
enum class Position {
    Top,
    Bottom,
    Center,
    TopLeft,
    TopRight,
    BottomLeft,
    BottomRight,
}

@Serializable
enum class PageFormat(
    val value: String,
) {
    Page("{page}"),
    PageSize("{page} of {total}"),
    All("Page {page} of {total}"),
}

@Serializable
data class NumberPosition(
    val x: Int = 0,
    val y: Int = 0,
)

@Serializable
enum class DatePattern(
    val pattern: String,
) {
    Date("dd-MM-yyyy"),
    Long("dd MMMM yyyy"),
    Iso("yyyy-MM-dd"),
}

@Serializable
enum class ProcessMode {
    Batch,
    Merge,
    None,
}

@Serializable
data class BatchSettings(
    val dateFormat: DatePattern = DatePattern.Date,
    val selectedDate: Long = System.currentTimeMillis(),
)

@Serializable
data class MergeSettings(
    val mergeName: String = "",
)

@Serializable
enum class EnhancementType {
    Identity,
    Numbering,
}

@Serializable
data class IdentitySettings(
    val text: String = "",
    val position: Position = Position.Center,
    val fontName: String = "",
    val fontSize: Int = 60,
    val rotation: Int = 45,
    val opacity: Float = 0.3f,
    val colorLong: ULong = Color.Black.value,
) {

    val color: Color
        get() = Color(colorLong)
}

@Serializable
data class NumberingSettings(
    val format: PageFormat = PageFormat.All,
    val fontSize: Int = 12,
    val fontName: String = "",
    val x: Int = 0,
    val y: Int = 0,
    val colorLong: ULong = Color.Black.value,
) {

    val color: Color
        get() = Color(colorLong)
}

@Serializable
data class OutputConfiguration(
    val id: String = UUID.randomUUID().toString(),
    val mode: ProcessMode = ProcessMode.None,
    val activeEnhancements: Set<EnhancementType> = emptySet(),
    val name: String,
    val targetDirectory: String,
    val batchSettings: BatchSettings = BatchSettings(),
    val mergeSettings: MergeSettings = MergeSettings(),
    val identitySettings: IdentitySettings = IdentitySettings(),
    val numberingSettings: NumberingSettings = NumberingSettings(),
)

@Serializable
data class UserConfigs(
    val activeConfigId: String,
    val savedConfigs: Map<String, OutputConfiguration> = emptyMap(), // Name/ID -> Config
)
