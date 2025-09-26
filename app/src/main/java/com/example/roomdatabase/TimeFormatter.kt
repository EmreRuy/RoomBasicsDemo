package com.example.roomdatabase

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatTimestamp(
    epochMillis: Long?,
    pattern: String = "d MMM, HH:mm",
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone = TimeZone.getDefault()
): String {
    if (epochMillis == null) return ""
    return try {
        val sdf = SimpleDateFormat(pattern, locale)
        sdf.timeZone = timeZone
        sdf.format(Date(epochMillis))
    } catch (_: Exception) {
        epochMillis.toString()
    }
}