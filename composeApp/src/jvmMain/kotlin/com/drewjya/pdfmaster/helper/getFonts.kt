package com.drewjya.pdfmaster.helper

import java.awt.GraphicsEnvironment

fun getDeviceFonts(): List<String> {
    val graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment()
    return graphicsEnvironment.availableFontFamilyNames.toList()
}
