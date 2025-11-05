package com.skegworks.mobilepos.utils

import java.util.Calendar

class DateUtilityImpl: DateUtility {
    override fun getYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    override fun getDate(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        return "${day.toString().padStart(2, '0')}/$month/$year"
    }
}