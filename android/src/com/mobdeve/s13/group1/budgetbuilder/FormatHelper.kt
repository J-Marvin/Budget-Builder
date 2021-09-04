package com.mobdeve.s13.group1.budgetbuilder

import java.text.SimpleDateFormat

class FormatHelper {
    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        val dateFormatterComplete = SimpleDateFormat(DATE_FORMAT)
        const val DATE_NO_TIME_FORMAT = "yyyy-MM-dd"
        val dateFormatterNoTime = SimpleDateFormat(DATE_NO_TIME_FORMAT)
    }
}