package com.skegworks.mobilepos.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale

class DateUtilityImpl : DateUtility {
    override fun getYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    override fun getDateForToday(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        return "${day.toString().padStart(2, '0')}/$month/$year"
    }

    override fun formatDate(
        date: String,
        inputPattern: String,
        outputPattern: String
    ): String {
        val inputFormatter = DateTimeFormatterBuilder()
            .appendPattern(inputPattern)
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true) // optional .SSS...
            .toFormatter()

        val outputFormatter = DateTimeFormatter.ofPattern(outputPattern)

        val dateTime = LocalDateTime.parse(date, inputFormatter)
        return dateTime.format(outputFormatter)
    }

    override fun getDateTime(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val hour = calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
        val minute = calendar.get(Calendar.MINUTE).toString().padStart(2, '0')

        return "${day.toString().padStart(2, '0')}/$month/$year $hour:$minute"
    }

    override fun getCurrentTimeStamp(): Long {
        return System.currentTimeMillis()
    }

    override fun isDateToday(date: String): Boolean {
        // Parse the given date-time string


        val dateTime = LocalDateTime.parse(date) // ISO-8601 → parses automatically
        return dateTime.toLocalDate() == LocalDate.now()
    }

    override fun dateToMillis(date: String): Long {
        // Define the pattern matching "dd-MM-yyyy"
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        // Parse the string into a LocalDate object
        val localDate = LocalDate.parse(date, formatter)

        // Convert the LocalDate to a ZonedDateTime at the start of the day in the system's default time zone
        val zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault())

        // Get the milliseconds from the epoch
        return zonedDateTime.toInstant().toEpochMilli()
    }

    override fun millisToDateString(millis: Long): String? {
        return try {
            val pattern = "dd-MM-yyyy"
            // 1. Create an Instant from the epoch milliseconds
            val instant = Instant.ofEpochMilli(millis)

            // 2. Apply a time zone (using the system default here, or specify one)
            val zonedDateTime = instant.atZone(ZoneId.systemDefault())

            // 3. Format the date into a string using a specific pattern and locale
            val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
            zonedDateTime.format(formatter)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun daysAfterInMillis(days: Int): Long {
        val today = LocalDate.now()
        val zoneId = ZoneId.systemDefault()

        // Calculate the date n days from today
        val dateLater = today.plusDays(days.toLong())

        // Get the end of that day (23:59:59.999) and its milliseconds
        // Note: The generally recommended approach is to use the start of the *next* day for an exclusive end boundary.
        val endOfDay10DaysExclusive = dateLater.plusDays(1).atStartOfDay()
        val endOfDayMillisExclusive =
            endOfDay10DaysExclusive.atZone(zoneId).toInstant().toEpochMilli()

        return endOfDayMillisExclusive
    }

    override fun daysBeforeInMillis(days: Int, today: LocalDate): Long {
        return today.minusDays(7)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    override fun daysAfterInDateString(days: Int, format: String): String {
        val now = Instant.now()

        // 2. Add days to the current instant
        val daysLater = now.plus(days.toLong(), ChronoUnit.DAYS)

        // 4. Format the date to a readable string in the default system time zone
        val formatter = DateTimeFormatter.ofPattern(format)
            .withZone(ZoneId.systemDefault())
        val dateString: String = formatter.format(daysLater)
        return dateString
    }
}