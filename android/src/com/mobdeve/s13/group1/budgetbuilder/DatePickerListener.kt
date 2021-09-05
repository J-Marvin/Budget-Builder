package com.mobdeve.s13.group1.budgetbuilder

interface DatePickerListener {
    fun onSelectDate(month: Int, year: Int)
    fun onCancelDate()
}