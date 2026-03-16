package com.skegworks.mobilepos.utils

interface DateUtility {
    fun getYear(): Int
    fun getDateForToday(): String
    fun formatDate(date: String, inputPattern: String, outputPattern: String): String
    fun getDateTime(): String
    fun getCurrentTimeStamp(): Long
    fun isDateToday(date: String): Boolean
    fun dateToMillis(date: String): Long
    fun millisToDateString(millis: Long): String?
    fun daysAfterInMillis(days: Int): Long
    fun daysAfterInDateString(days: Int, format: String = "yyyy-MM-dd HH:mm:ss"): String
}