package com.mobdeve.s13.group1.budgetbuilder

import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*

/** This class is a helper for formatting dates */
class DateHelper {
    companion object{
        /** This method gets the first day of a given month and year
         *  @param month - the month (index 0)
         *  @param year - the year
         *  @return returns the Calendar set to the first day of the given month and year
         *  */
        fun getStartDate(month: Int, year:Int): Calendar {
            val date = Calendar.getInstance()
            date.apply{
                this.set(Calendar.MONTH, month)
                this.set(Calendar.YEAR, year)
                this.set(Calendar.DAY_OF_MONTH, 1)
            }

            return date
        }

        /** This method gets the last day of a given month and year
         *  @param month - the month (index 0)
         *  @param year - the year
         *  @return returns the Calendar set to the last day of the given month and year
         *  */
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

        /** This method gets the first day of a given month and year as a string
         *  @param month - the month (index 0)
         *  @param year - the year
         *  @return returns the string set to the first day of the given month and year
         *  */
        fun getStartDateString(month: Int, year: Int): String {
            val date = getStartDate(month, year)
            return FormatHelper.dateFormatterNoTime.format(date.time)
        }

        /** This method gets the last day of a given month and year as a string
         *  @param month - the month (index 0)
         *  @param year - the year
         *  @return returns the string set to the last day of the given month and year
         *  */
        fun getEndDateString(month: Int, year:Int): String {
            val date = getEndDate(month, year)
            return FormatHelper.dateFormatterNoTime.format(date.time)
        }
    }
}