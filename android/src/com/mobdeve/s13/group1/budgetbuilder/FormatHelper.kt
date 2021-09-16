package com.mobdeve.s13.group1.budgetbuilder

import java.text.SimpleDateFormat

/** This class is a helper for formatting dates */
class FormatHelper {
    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        // A date formatter with the format (yyyy-MM-dd HH:mm:ss)
        val dateFormatterComplete = SimpleDateFormat(DATE_FORMAT)
        const val DATE_NO_TIME_FORMAT = "yyyy-MM-dd"
        // A date formatter with the format (yyyy-MM-dd)
        val dateFormatterNoTime = SimpleDateFormat(DATE_NO_TIME_FORMAT)
    }
}