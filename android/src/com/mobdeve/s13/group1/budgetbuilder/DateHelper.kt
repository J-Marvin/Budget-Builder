package com.mobdeve.s13.group1.budgetbuilder

import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*

class DateHelper {
    companion object{
        fun getDisplayMonthYear(month: Int, year: Int): String {
            val date = Calendar.getInstance()
            date.set(Calendar.MONTH, month)
            val month = date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)
            return "$month, $year"
        }

        fun getStartDate(month: Int, year:Int): Calendar {
            val date = Calendar.getInstance()
            date.apply{
                this.set(Calendar.MONTH, month)
                this.set(Calendar.YEAR, year)
                this.set(Calendar.DAY_OF_MONTH, 1)
            }

            return date
        }

        fun getEndDate(month: Int, year: Int): Calendar {
            val date = Calendar.getInstance()
            date.apply{
                this.set(Calendar.MONTH, month)
                this.set(Calendar.YEAR, year)
                this.set(Calendar.DAY_OF_MONTH, this.getActualMaximum(Calendar.DAY_OF_MONTH))
                this.add(Calendar.DAY_OF_MONTH, 1)
            }

            return date
        }

        fun getStartDateString(month: Int, year: Int): String {
            val date = getStartDate(month, year)
            return FormatHelper.dateFormatterNoTime.format(date.time)
        }

        fun getEndDateString(month: Int, year:Int): String {
            val date = getEndDate(month, year)
            return FormatHelper.dateFormatterNoTime.format(date.time)
        }
    }
}