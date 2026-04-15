package com.drewjya.pdfmaster.helper

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import org.apache.pdfbox.pdmodel.font.Standard14Fonts

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
    val fontName: String = Standard14Fonts.FontName.HELVETICA_BOLD.name,
    val fontSize: Int = 60,
    val rotation: Int = 45,
    val opacity: Float = 0.3f,
    val colorLong: ULong = Color.Black.value,
) {
    val font: Standard14Fonts.FontName
        get() = Standard14Fonts.FontName.valueOf(fontName)

    val color: Color
        get() = Color(colorLong)
}

@Serializable
data class NumberingSettings(
    val format: PageFormat = PageFormat.All,
    val fontSize: Int = 12,
    val fontName: String = Standard14Fonts.FontName.HELVETICA_BOLD.name,
    val x: Int = 0,
    val y: Int = 0,
    val colorLong: ULong = Color.Black.value,
) {
    val font: Standard14Fonts.FontName
        get() = Standard14Fonts.FontName.valueOf(fontName)
    val color: Color
        get() = Color(colorLong)
}

@Serializable
data class OutputConfiguration(
    val id: String =
        java.util.UUID
            .randomUUID()
            .toString(),
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
