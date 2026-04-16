package com.drewjya.pdfmaster.helper

import androidx.compose.ui.graphics.Color
import java.util.UUID
import kotlinx.serialization.Serializable
import org.openpdf.text.FontFactory

fun getAvailableFonts(): List<String> {
    FontFactory.registerDirectories()
    return (FontFactory.getRegisteredFonts().filter {
        val value = it.trim()
        val isFirstCapital = value.firstOrNull()?.isUpperCase() ?: false
        if (value.isEmpty()) {
            false
        } else {
            isFirstCapital
        }
    }
        .toList()).sortedBy { it }
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
    val variable: String = "Statement",
    val separator: String = " - ",
    val format: String = "{identifier} - {variable} {date}.pdf",
    val listPrefixOrder: List<String> = listOf(
        "Portfolio Activity",
        "2",
        "Statement Position"
    ),
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
