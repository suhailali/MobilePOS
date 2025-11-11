package com.skegworks.mobilepos.utils

import java.util.Calendar

class DateUtilityImpl: DateUtility {
    override fun getYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
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
}