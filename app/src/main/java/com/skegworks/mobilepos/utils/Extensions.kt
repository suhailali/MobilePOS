package com.skegworks.mobilepos.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Long.toDateString(locale: Locale): String {
    val instant = Instant.ofEpochMilli(this)
    val formatter = DateTimeFormatter.ofLocalizedDate(java.time.format.FormatStyle.MEDIUM)
        .withLocale(locale)
        .withZone(ZoneId.systemDefault()) // Use the system's default time zone

    return formatter.format(instant)
}