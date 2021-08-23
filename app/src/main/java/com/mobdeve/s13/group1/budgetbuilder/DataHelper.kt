package com.mobdeve.s13.group1.budgetbuilder

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor

class DataHelper {
    companion object {
        fun getFurniture(): ArrayList<Furniture> {
            val data = getChairs()
            data.addAll(getBeds())

            return data
        }
        fun getChairs() : ArrayList<Furniture> {
            val data = ArrayList<Furniture>()

            data.add(Furniture(
                R.drawable.chair_sw,
                10,
                owned = false,
                equipped = false,
                name="Ol\' Fashion",
                type= "chair"
            )
            )

            data.add(Furniture(
                R.drawable.cardboardboxclosed_sw,
                10,
                owned = true,
                equipped = true,
                name="Cardboard Chair",
                type= "chair"
            ))

            data.add(Furniture(
                R.drawable.benchcushion_sw,
                10,
                owned = false,
                equipped = false,
                name="Bench",
                type= "chair"
            ))

            data.add(Furniture(
                R.drawable.chairdesk_sw,
                10,
                owned = false,
                equipped = false,
                name="Roller",
                type= "chair"
            ))

            data.add(Furniture(
                R.drawable.chairrounded_sw,
                10,
                owned = false,
                equipped = false,
                name="All around",
                type= "chair"
            ))

            data.add(Furniture(
                R.drawable.loungechair_sw,
                10,
                owned = false,
                equipped = false,
                name="Lounger",
                type = "chair"
            ))

            data.add(Furniture(
                R.drawable.stoolbar_sw,
                10,
                owned = false,
                equipped = false,
                name="Stool",
                type = "chair"
            ))

            data.add(Furniture(
                R.drawable.stoolbarsquare_sw,
                10,
                owned = false,
                equipped = false,
                name="Rounded Stool",
                type = "chair"
            ))

            data.add(Furniture(
                R.drawable.toilet_sw,
                20,
                owned = false,
                equipped = false,
                name="The Thinker",
                type = "chair"
            ))

            return data
        }

        fun getBeds() : ArrayList<Furniture> {
            val data = ArrayList<Furniture>()

            data.add(
                Furniture(
                R.drawable.bedbunk_sw,
                100,
                owned = false,
                equipped = false,
                name = "Bunker",
                type = "bed"
            ))

            data.add(Furniture(
                R.drawable.beddouble_sw,
                100,
                owned = true,
                equipped = true,
                name="Double Bed",
                type = "bed"
            ))

            data.add(Furniture(
                R.drawable.bedsingle_sw,
                100,
                owned = false,
                equipped = false,
                name="The Loner",
                type = "bed"
            ))

            data.add(Furniture(
                R.drawable.bathtub_sw,
                100,
                owned = false,
                equipped =false,
                name="Tub-o\'-bed",
                type = "bed"
            ))

            return data
        }

        fun getExpenses(): ArrayList<Expense> {
            val data = ArrayList<Expense>()
            var cal = Calendar.getInstance()

            cal.set(2021, 8, 20, 16, 27, 0)
            data.add(Expense(
                cal.time,
                "Entertainment",
                150.toFloat(),
                "Netflix subscription"
            ))

            data.add(Expense(
                cal.time,
                "Food",
                200.toFloat(),
                "Ramen"
            ))

            cal.set(2020, 1, 2, 8, 30, 0)
            data.add(Expense(
                cal.time,
                "Transportation",
                1000.toFloat(),
                "Fuel"
            ))

            data.add(Expense(
                cal.time,
                "Utilities",
                500.toFloat(),
                "Electricity"
            ))

            data.add(Expense(
                cal.time,
                "Personal",
                3000.toFloat(),
                "Skin care"
            ))

            data.add(Expense(
                cal.time,
                "Medical",
                500.toFloat(),
                "Vitamin D"
            ))

            data.add(Expense(
                cal.time,
                "Others",
                1000.toFloat(),
                "Birthday gift"
            ))

            data.add(Expense(
                cal.time,
                "Food",
                1000.toFloat(),
                "Coffee Beans"
            ))
            return data
        }

        fun getCategoryExpenses(): ArrayList<CategoryExpense>{
            val ENT_INDEX = 0
            val FOOD_INDEX = 1
            val TRANS_INDEX = 2
            val UTIL_INDEX = 3
            val PERS_INDEX = 4
            val MED_INDEX = 5
            val OTHERS_INDEX = 6

            val data = ArrayList<CategoryExpense>()
            var total = 0F


            data.add(CategoryExpense("Entertainment", R.color.category_entertainment, '$'))
            data.add(CategoryExpense("Food", R.color.category_food, '$'))
            data.add(CategoryExpense("Transportation", R.color.category_transportation, '$'))
            data.add(CategoryExpense("Utilities", R.color.category_utilities, '$'))
            data.add(CategoryExpense("Personal",  R.color.category_personal, '$'))
            data.add(CategoryExpense("Medical", R.color.category_medical, '$'))
            data.add(CategoryExpense("Others", R.color.category_others, '$'))

            for (expense in getExpenses()) {
                var index = -1
                total += expense.amount
                when(expense.type) {
                    "Entertainment" -> index = ENT_INDEX
                    "Food" -> index = FOOD_INDEX
                    "Transportation" -> index = TRANS_INDEX
                    "Utilities" -> index = UTIL_INDEX
                    "Personal" -> index = PERS_INDEX
                    "Medical" -> index = MED_INDEX
                    "Others" -> index = OTHERS_INDEX
                }

                if (index != -1) {
                    data[index].total += expense.amount
                }
            }

            for (category in data){
                category.percent = floor((category.total / total).toDouble() * 100).toInt()
            }

            return data
        }
    }
}