package com.mobdeve.s13.group1.budgetbuilder

enum class ExpenseType(val iconColor: String, val backColor: String, val iconImg: Int, val textType: String) {
    ENTERTAINMENT("#008A8A", "#BAE5E6", R.drawable.expense_type_entertainment, "Entertainment"),
    FOOD("#CF3B3B", "#EDC4C4", R.drawable.expense_type_food, "Food"),
    TRANSPORTATION("#FF6200EE", "#E3CFFA", R.drawable.expense_type_transpo, "Transportation"),
    UTILITIES("#FB7414", "#FCCFB3", R.drawable.expense_type_util, "Utilities"),
    PERSONAL("#2133BF", "#C2D8FC", R.drawable.expense_type_personal, "Personal Care"),
    MEDICAL("#E81A95", "#EDB4D6", R.drawable.expense_type_med, "Medical Care"),
    OTHERS("#4F9A00", "#CCDEB8", R.drawable.expense_type_others, "Others")
}