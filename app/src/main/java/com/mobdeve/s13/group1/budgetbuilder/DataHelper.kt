package com.mobdeve.s13.group1.budgetbuilder

import java.util.*
import kotlin.collections.ArrayList

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
                equipped = false,
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
    }
}