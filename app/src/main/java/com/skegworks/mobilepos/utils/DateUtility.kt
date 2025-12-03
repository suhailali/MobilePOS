package com.skegworks.mobilepos.utils

interface DateUtility {
    fun getYear(): Int
    fun getDateForToday(): String
    fun formatDate(date: String, inputPattern: String, outputPattern: String): String
    fun getDateTime(): String
    fun getCurrentTimeStamp(): Long
    fun isDateToday(date: String): Boolean
}