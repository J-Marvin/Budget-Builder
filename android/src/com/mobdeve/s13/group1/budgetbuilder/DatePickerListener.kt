package com.mobdeve.s13.group1.budgetbuilder

/** This interface represents a listener for when the date is set*/
interface DatePickerListener {

    /** The method to be executed when a date is chosen
     *  @param month - the month (index 0)
     *  @parm year - the year
     *  */
    fun onSelectDate(month: Int, year: Int)

    /** The method to be executed when the date is not chosen*/
    fun onCancelDate()
}