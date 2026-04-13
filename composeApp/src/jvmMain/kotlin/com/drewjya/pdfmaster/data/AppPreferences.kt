package com.drewjya.pdfmaster.data

import androidx.compose.ui.graphics.Color
import com.drewjya.pdfmaster.helper.DatePattern
import com.drewjya.pdfmaster.helper.PageFormat
import com.drewjya.pdfmaster.helper.Position
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import java.util.prefs.Preferences
import kotlin.time.Clock

class AppPreferences {
    private val prefs = Preferences.userRoot().node("com.drewjya.pdfmaster.prefs")

    var selectedDirectory: String
        get() = prefs.get("selectedDirectory", "")
        set(value) = prefs.put("selectedDirectory", value)

    var monthlyDirectory: String
        get() = prefs.get("monthlyDirectory", "")
        set(value) = prefs.put("monthlyDirectory", value)

    var inputDirectory: String
        get() = prefs.get("inputDirectory", "")
        set(value) = prefs.put("inputDirectory", value)
    var selectedName: String
        get() = prefs.get("selectedName", "")
        set(value) = prefs.put("selectedName", value)

    var x: String
        get() = prefs.get("x", "0")
        set(value) = prefs.put("x", value)

    var y: String
        get() = prefs.get("y", "0")
        set(value) = prefs.put("y", value)

    // Save Color as a Long (ULong representation)
    var color: Long
        get() = prefs.getLong("color", Color.Red.value.toLong())
        set(value) = prefs.putLong("color", value)

    var opacity: Float
        get() = prefs.getFloat("opacity", 0f)
        set(value) = prefs.putFloat("opacity", value)

    var rotation: Double
        get() = prefs.getDouble("rotation", 45.0)
        set(value) = prefs.putDouble("rotation", value)

    var watermarkFontSize: Int
        get() = prefs.getInt("watermarkFontSize", 60)
        set(value) = prefs.putInt("watermarkFontSize", value)

    var numberingFontSize: Int
        get() = prefs.getInt("numberingFontSize", 12)
        set(value) = prefs.putInt("numberingFontSize", value)

    var watermarkText: String
        get() = prefs.get("watermarkText", "CONFIDENTIAL")
        set(value) = prefs.put("watermarkText", value)

    // Enums are saved as Strings
    var position: Position
        get() = Position.valueOf(prefs.get("position", Position.Center.name))
        set(value) = prefs.put("position", value.name)

    var pageFormat: PageFormat
        get() = PageFormat.valueOf(prefs.get("pageFormat", PageFormat.Page.name))
        set(value) = prefs.put("pageFormat", value.name)

    var font: Standard14Fonts.FontName
        get() = Standard14Fonts.FontName.valueOf(prefs.get("font", Standard14Fonts.FontName.HELVETICA_BOLD.name))
        set(value) = prefs.put("font", value.name)

    var dateFormat: DatePattern
        get() = DatePattern.valueOf(prefs.get("dateFormat", DatePattern.Iso.name))
        set(value) = prefs.put("dateFormat", value.name)

    var selectedDate: Long
        get() = prefs.getLong("selectedDate", Clock.System.now().toEpochMilliseconds())
        set(value) = prefs.putLong("selectedDate", value)
}
