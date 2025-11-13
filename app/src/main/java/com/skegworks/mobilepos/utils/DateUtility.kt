package com.skegworks.mobilepos.utils

interface DateUtility {
    fun getYear(): Int
    fun getDateTime(): String
    fun getCurrentTimeStamp(): Long
    fun isDateToday(date: String, pattern: String): Boolean
}