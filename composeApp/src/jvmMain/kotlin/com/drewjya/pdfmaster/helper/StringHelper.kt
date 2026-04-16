package com.drewjya.pdfmaster.helper

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDate(
    timestamp: Long,
    format: DatePattern,
): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val zonedDateTime = instant.atZone(ZoneId.systemDefault())

    val formatter = DateTimeFormatter.ofPattern(format.pattern, Locale.ENGLISH)

    return zonedDateTime.format(formatter)
}
