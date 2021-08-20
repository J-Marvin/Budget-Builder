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
                equipped = false
            )
            )

            data.add(Furniture(
                R.drawable.cardboardboxclosed_sw,
                10,
                owned = true,
                equipped = true
            ))

            data.add(Furniture(
                R.drawable.benchcushion_sw,
                10,
                owned = false,
                equipped = false
            ))

            data.add(Furniture(
                R.drawable.chairdesk_sw,
                10,
                owned = false,
                equipped = false
            ))

            data.add(Furniture(
                R.drawable.chairrounded_sw,
                10,
                owned = false,
                equipped = false
            ))

            data.add(Furniture(
                R.drawable.loungechair_sw,
                10,
                owned = false,
                equipped = false
            ))

            data.add(Furniture(
                R.drawable.stoolbar_sw,
                10,
                owned = false,
                equipped = false
            ))

            data.add(Furniture(
                R.drawable.stoolbarsquare_sw,
                10,
                owned = false,
                equipped = false
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
                equipped = false
            ))
            data.add(Furniture(
                R.drawable.beddouble_sw,
                100,
                owned = true,
                equipped = false
            ))
            data.add(Furniture(
                R.drawable.bedsingle_sw,
                100,
                owned = false,
                equipped = false
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
                150,
                "Netflix subscription"
            ))

            data.add(Expense(
                cal.time,
                "Food",
                200,
                "Ramen"
            ))

            cal.set(2020, 1, 2, 8, 30, 0)
            data.add(Expense(
                cal.time,
                "Transportation",
                1000,
                "Fuel"
            ))

            data.add(Expense(
                cal.time,
                "Utilities",
                500,
                "Electricity"
            ))

            data.add(Expense(
                cal.time,
                "Personal",
                3000,
                "Skin care"
            ))

            data.add(Expense(
                cal.time,
                "Medical",
                500,
                "Vitamin D"
            ))

            data.add(Expense(
                cal.time,
                "Others",
                1000,
                "Birthday gift"
            ))

            data.add(Expense(
                cal.time,
                "Food",
                1000,
                "Coffee Beans"
            ))
            return data
        }
    }
}