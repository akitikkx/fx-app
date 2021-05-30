package com.ahmedtikiwa.fxapp.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar
import java.util.Date

class DateUtil {

    companion object {
        private const val DAYS_IN_PAST_TO_EXCL_WEEKENDS =
            41 // accounting for weekends, 41 ensures the return value is 30 work days
        private const val DATE_PATTERN = "yyyy-MM-dd"

        fun getPastThirtyDateExclWeekends(): List<String> {
            val simpleDateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
            val days = mutableListOf<String>()

            // accounting for
            for (i in 1..DAYS_IN_PAST_TO_EXCL_WEEKENDS) {
                val date = Calendar.getInstance()
                date.add(Calendar.DATE, -i)
                val dayOfWeek = date.get(Calendar.DAY_OF_WEEK)
                // the FXMarket API expects dates to not include weekend days
                if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                    days.add(simpleDateFormat.format(date.timeInMillis))
                }
            }

            return days;
        }

        fun getDateFromString(date: String?): Date? {
            val simpleDateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
            return if (date.isNullOrEmpty()) {
                null
            } else {
                simpleDateFormat.parse(date)
            }
        }
    }
}