package com.mobdeve.s13.group1.budgetbuilder.dao

interface BudgetDAO {
    fun addBudget(budget: Float, date: String): Long
    fun getBudgetByDate(date: String): BudgetModel?
    fun getBudgetById(id: String): BudgetModel?
    fun getAllBudgets(): ArrayList<BudgetModel>
    fun updateBudget(id: String, budget: Float): Boolean
}