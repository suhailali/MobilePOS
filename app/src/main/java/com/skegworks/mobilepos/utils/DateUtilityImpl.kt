package com.skegworks.mobilepos.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

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
        val inputFormatter = DateTimeFormatter.ofPattern(inputPattern)
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

    override fun isDateToday(date: String, pattern: String): Boolean {
        // Parse the given date-time string
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val dateTime = LocalDateTime.parse(date, formatter)

        // Compare only the date part
        val inputDate = dateTime.toLocalDate()
        val today = LocalDate.now()

        return inputDate == today
    }
}