package com.mobdeve.s13.group1.budgetbuilder

import java.util.*
import kotlin.collections.ArrayList

class DataHelper {
    companion object {
        fun getChairs() : ArrayList<Furniture> {
            val data = ArrayList<Furniture>()

            data.add(Furniture(
                R.drawable.chair_sw,
                10,
                owned = false,
                equipped = false,
                name = "Chair"
            )
            )

            data.add(Furniture(
                R.drawable.cardboardboxclosed_sw,
                10,
                owned = true,
                equipped = true,
                name = "Cardboard Box"
            ))

            data.add(Furniture(
                R.drawable.benchcushion_sw,
                10,
                owned = false,
                equipped = false,
                name = "Bench"
            ))

            data.add(Furniture(
                R.drawable.chairdesk_sw,
                10,
                owned = false,
                equipped = false,
                name = "Desk"
            ))

            data.add(Furniture(
                R.drawable.chairrounded_sw,
                10,
                owned = false,
                equipped = false,
                name = "Chair Rounded"
            ))

            data.add(Furniture(
                R.drawable.loungechair_sw,
                10,
                owned = false,
                equipped = false,
                name = "Lounge Chair"
            ))

            data.add(Furniture(
                R.drawable.stoolbar_sw,
                10,
                owned = false,
                equipped = false,
                name = "Stool"
            ))

            data.add(Furniture(
                R.drawable.stoolbarsquare_sw,
                10,
                owned = false,
                equipped = false,
                name = "Stool Square"
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
                name = "Bunk Bed"
            ))
            data.add(Furniture(
                R.drawable.beddouble_sw,
                100,
                owned = true,
                equipped = false,
                name = "Double Bed"
            ))
            data.add(Furniture(
                R.drawable.bedsingle_sw,
                100,
                owned = false,
                equipped = false,
                name = "Single Bed"
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
    }
}