package com.mobdeve.s13.group1.budgetbuilder

import android.database.Cursor
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseModel
import com.mobdeve.s13.group1.budgetbuilder.dao.FurnitureModel
import com.mobdeve.s13.group1.budgetbuilder.dao.RoomModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor

class DataHelper {
    companion object {
        fun getFurniture(): ArrayList<FurnitureModel> {
            val data = getChairs()
            data.addAll(getBeds())
            data.addAll(getFloors())
            data.addAll(getShelves())
            data.addAll(getEndTables())
            data.addAll(getDesks())
            data.addAll(getCouches())
            data.addAll(getCoffeeTables())

            return data
        }
        fun getChairs() : ArrayList<FurnitureModel> {
            val data = ArrayList<FurnitureModel>()

            data.add(
                FurnitureModel(
                R.drawable.chair_sw,
                10,
                owned = false,
                equipped = false,
                name="Ol\' Fashion",
                type= "chair",
                path="furniture/chairs/chair_SW.png",
                roompath="deskchair/deskchair_simple.png"
            )
            )

            data.add(
                FurnitureModel(
                R.drawable.cardboardboxclosed_sw,
                0,
                owned = true,
                equipped = true,
                name="Cardboard Chair",
                type= "chair",
                path="furniture/tables/cardboardBoxClosed_SW.png",
                roompath="deskchair/deskchair_box.png"
            )
            )

            data.add(
                FurnitureModel(
                    R.drawable.chairrounded_sw,
                    10,
                    owned = false,
                    equipped = false,
                    name="All around",
                    type= "chair",
                    path = "furniture/chairs/chairRounded_SW.png",
                    roompath="deskchair/deskchair_rounded.png"
                )
            )

            data.add(
                FurnitureModel(
                    R.drawable.chaircushion_sw,
                    20,
                    owned = false,
                    equipped = false,
                    name="All around",
                    type= "chair",
                    path = "furniture/chairs/chairCushion_SW.png",
                    roompath="deskchair/deskchair_cushions.png"
                )
            )

            data.add(
                FurnitureModel(
                R.drawable.chairdesk_sw,
                30,
                owned = false,
                equipped = false,
                name="Roller",
                type= "chair",
                path = "furniture/chairs/chairDesk_SW.png",
                roompath="deskchair/deskchair_wheels.png"
            )
            )

            data.add(
                FurnitureModel(
                R.drawable.toilet_sw,
                20,
                owned = false,
                equipped = false,
                name="The Thinker",
                type = "chair",
                path="furniture/chairs/toilet_SW.png",
                roompath="deskchair/deskchair_toilet.png"
            )
            )

            return data
        }

        fun getBeds() : ArrayList<FurnitureModel> {
            val data = ArrayList<FurnitureModel>()

            data.add(
                FurnitureModel(
                R.drawable.bedbunk_sw,
                150,
                owned = false,
                equipped = false,
                name = "Bunker",
                type = "bed",
                path="furniture/beds/bedBunk_SW.png",
                roompath="bed/bed_bunk.png"
            )
            )

            data.add(
                FurnitureModel(
                R.drawable.beddouble_sw,
                200,
                owned = false,
                equipped = false,
                name="Double Bed",
                type = "bed",
                path = "furniture/beds/bedDouble_SW.png",
                roompath="bed/bed_double.png"
            )
            )

            data.add(
                FurnitureModel(
                R.drawable.bedsingle_sw,
                0,
                owned = true,
                equipped = true,
                name="The Loner",
                type = "bed",
                path="furniture/beds/bedSingle_SW.png",
                roompath="bed/bed_single.png"
            )
            )

            data.add(
                FurnitureModel(
                R.drawable.bathtub_se,
                150,
                owned = false,
                equipped =false,
                name="Tub-o\'-bed",
                type = "bed",
                path="furniture/beds/bathtub_SE.png",
                roompath="bed/bed_tub.png"
            )
            )
            return data
        }

        fun getFloors() : ArrayList<FurnitureModel> {
            val data = ArrayList<FurnitureModel>()

            data.add(
                FurnitureModel(
                    R.drawable.floor_default,
                    0,
                    owned = true,
                    equipped =true,
                    name="Default floor",
                    type = "floor",
                    path="furniture/floors/floor_default.png",
                    roompath="floor/floor_default.png"
                )
            )

            data.add(
                FurnitureModel(
                    R.drawable.floor_blue,
                    50,
                    owned = false,
                    equipped = false,
                    name = "Blue Floor",
                    type = "floor",
                    path="furniture/floors/floor_blue.png",
                    roompath="floor/floor_blue.png"
                )
            )

            data.add(
                FurnitureModel(
                    R.drawable.floor_gray,
                    50,
                    owned = false,
                    equipped = false,
                    name = "Gray Floor",
                    type = "floor",
                    path="furniture/floors/floor_gray.png",
                    roompath="floor/floor_gray.png"
                )
            )

            data.add(
                FurnitureModel(
                    R.drawable.floor_red,
                    50,
                    owned = false,
                    equipped = false,
                    name = "Red Floor",
                    type = "floor",
                    path="furniture/floors/floor_red.png",
                    roompath="floor/floor_red.png"
                )
            )

            return data
        }

        fun getShelves() : ArrayList<FurnitureModel> {
            val data = ArrayList<FurnitureModel>()

            data.add(
                FurnitureModel(
                    R.drawable.bookcaseclosed_sw,
                    100,
                    owned = false,
                    equipped = false,
                    name = "Closed Bookshelf",
                    type = "shelf",
                    path = "furniture/bookcases/bookcaseClosed_SW.png",
                    roompath = "shelf/shelf_book.png"
                )
            )

            data.add(
                FurnitureModel(
                    R.drawable.bookcaseopen_sw,
                    50,
                    owned = false,
                    equipped = false,
                    name = "Open Bookshelf",
                    type = "shelf",
                    path = "furniture/bookcases/bookcaseOpen_SW.png",
                    roompath = "shelf/shelf_open.png"
                )
            )

            data.add(
                FurnitureModel(
                    R.drawable.bookcaseclosed_sw,
                    100,
                    owned = false,
                    equipped = false,
                    name = "Cabinet",
                    type = "shelf",
                    path = "furniture/bookcases/bookcaseClosedDoors_SW.png",
                    roompath = "shelf/shelf_cabinet.png"
                )
            )

            return data
        }

        fun getEndTables() : ArrayList<FurnitureModel> {
            val data = ArrayList<FurnitureModel>()

            data.add(
                FurnitureModel(
                    R.drawable.cabinetbeddrawertable_sw,
                    50,
                    owned = false,
                    equipped = false,
                    name = "Half End Table",
                    type = "endtable",
                    path = "furniture/cabinets/cabinetBedDrawerTable_SW.png",
                    roompath = "endtable/endtable_half.png"
                )
            )

            data.add(
                FurnitureModel(
                    R.drawable.cabinetbeddrawer_sw,
                    50,
                    owned = false,
                    equipped = false,
                    name = "Full End Table",
                    type = "endtable",
                    path = "furniture/cabinets/cabinetBedDrawer_SW.png",
                    roompath = "endtable/endtable_full.png"
                )
            )

            return data
        }

        fun getDesks() : ArrayList<FurnitureModel> {
            val data = ArrayList<FurnitureModel>()

            data.add(
                FurnitureModel(
                    R.drawable.tablecloth_sw,
                    0,
                    owned = true,
                    equipped = true,
                    name = "Simple Table",
                    type = "desk",
                    path = "furniture/tables/table_SW.png",
                    roompath = "desk/desk_simple.png"
                )
            )

            data.add(
                FurnitureModel(
                    R.drawable.tablecloth_sw,
                    50,
                    owned = false,
                    equipped = false,
                    name = "Covered Table",
                    type = "desk",
                    path = "furniture/tables/tableCloth_SW.png",
                    roompath = "desk/desk_covered.png"
                )
            )

            data.add(
                FurnitureModel(
                    R.drawable.desk_sw,
                    80,
                    owned = false,
                    equipped = false,
                    name = "Desk",
                    type = "desk",
                    path = "furniture/desks/desk_SW.png",
                    roompath = "desk/desk_plain.png"
                )
            )

            data.add(
                FurnitureModel(
                    R.drawable.tableglass_sw,
                    80,
                    owned = false,
                    equipped = false,
                    name = "Glass Table",
                    type = "desk",
                    path = "furniture/tables/tableGlass_SW.png",
                    roompath = "desk/desk_glass.png"
                )
            )

            return data
        }

        fun getCouches() : ArrayList<FurnitureModel> {
            val data = ArrayList<FurnitureModel>()

            data.add(
                FurnitureModel(
                    R.drawable.benchcushionlow_sw,
                    25,
                    owned = false,
                    equipped = false,
                    name = "Low Bench",
                    type = "couch",
                    path = "furniture/chairs/benchCushionLow_SW.png",
                    roompath = "couch/couch_benchlow.png"
                )
            )

            data.add(
                FurnitureModel(
                    R.drawable.bench_sw,
                    35,
                    owned = false,
                    equipped = false,
                    name = "Bench",
                    type = "couch",
                    path = "furniture/chairs/bench_SW.png",
                    roompath = "couch/couch_bench.png"
                )
            )

            data.add(
                FurnitureModel(
                    R.drawable.benchcushion_sw,
                    55,
                    owned = false,
                    equipped = false,
                    name = "Cushioned Bench",
                    type = "couch",
                    path = "furniture/chairs/benchCushion_SW.png",
                    roompath = "couch/couch_benchcushion.png"
                )
            )

            data.add(
                FurnitureModel(
                    R.drawable.loungesofa_sw,
                    90,
                    owned = false,
                    equipped = false,
                    name = "Sofa",
                    type = "couch",
                    path = "furniture/chairs/loungeSofa_SW.png",
                    roompath = "couch/couch_lounge.png"
                )
            )

            return data
        }

        fun getCoffeeTables() : ArrayList<FurnitureModel> {
            val data = ArrayList<FurnitureModel>()

            data.add(
                FurnitureModel(
                    R.drawable.tablecoffee_sw,
                    70,
                    owned = false,
                    equipped = false,
                    name = "Wooden Coffee Table",
                    type = "coffeetable",
                    path = "furniture/tables/tableCoffee_SW.png",
                    roompath = "coffeetable/coffeetable_simple.png"
                )
            )

            data.add(
                FurnitureModel(
                    R.drawable.tableglass_sw,
                    100,
                    owned = false,
                    equipped = false,
                    name = "Glass Coffee Table",
                    type = "coffeetable",
                    path = "furniture/tables/tableCoffeeGlass_SW.png",
                    roompath = "coffeetable/coffeetable_glass.png"
                )
            )

            return data
        }

        fun getExpenses(): ArrayList<ExpenseModel> {
            val data = ArrayList<ExpenseModel>()
            var cal = Calendar.getInstance()

            cal.set(2019, 8, 20, 16, 27, 0)
            data.add(
                ExpenseModel(
                cal.time,
                "Entertainment",
                150.toFloat(),
                "Netflix subscription"
            )
            )

            data.add(
                ExpenseModel(
                cal.time,
                "Food",
                200.toFloat(),
                "Ramen"
            )
            )

            cal.set(2020, 1, 2, 8, 30, 0)
            data.add(
                ExpenseModel(
                cal.time,
                "Transportation",
                1000.toFloat(),
                "Fuel"
            )
            )

            data.add(
                ExpenseModel(
                cal.time,
                "Utilities",
                500.toFloat(),
                "Electricity"
            )
            )

            data.add(
                ExpenseModel(
                cal.time,
                "Personal",
                3000.toFloat(),
                "Skin care"
            )
            )

            data.add(
                ExpenseModel(
                cal.time,
                "Medical",
                500.toFloat(),
                "Vitamin D"
            )
            )

            data.add(
                ExpenseModel(
                cal.time,
                "Others",
                1000.toFloat(),
                "Birthday gift"
            )
            )

            data.add(
                ExpenseModel(
                cal.time,
                "Food",
                1000.toFloat(),
                "Coffee Beans"
            )
            )
            return data
        }

        fun getCategoryExpenses(expens : ArrayList<ExpenseModel>): ArrayList<CategoryExpense>{
            val ENT_INDEX = 0
            val FOOD_INDEX = 1
            val TRANS_INDEX = 2
            val UTIL_INDEX = 3
            val PERS_INDEX = 4
            val MED_INDEX = 5
            val OTHERS_INDEX = 6

            val data = ArrayList<CategoryExpense>()
            var total = 0F


            data.add(CategoryExpense("Entertainment", R.color.category_entertainment))
            data.add(CategoryExpense("Food", R.color.category_food))
            data.add(CategoryExpense("Transportation", R.color.category_transportation))
            data.add(CategoryExpense("Utilities", R.color.category_utilities))
            data.add(CategoryExpense("Personal",  R.color.category_personal))
            data.add(CategoryExpense("Medical", R.color.category_medical))
            data.add(CategoryExpense("Others", R.color.category_others))

            for (expense in expens) {
                var index = -1
                total += expense.amount!!
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
                    data[index].total += expense.amount!!
                }
            }

            for (category in data){
                category.percent = floor((category.total / total).toDouble() * 100).toInt()
            }

            return data
        }

        fun getCategoryExpensesSumFromCursor(cursor: Cursor?, sumColumn: String, groupColumn: String): ArrayList<CategoryExpense> {
            val data = ArrayList<CategoryExpense>()

            val ENT_INDEX = 0
            val FOOD_INDEX = 1
            val TRANS_INDEX = 2
            val UTIL_INDEX = 3
            val PERS_INDEX = 4
            val MED_INDEX = 5
            val OTHERS_INDEX = 6

            var total = 0F

            data.add(CategoryExpense("Entertainment", R.color.category_entertainment))
            data.add(CategoryExpense("Food", R.color.category_food))
            data.add(CategoryExpense("Transportation", R.color.category_transportation))
            data.add(CategoryExpense("Utilities", R.color.category_utilities))
            data.add(CategoryExpense("Personal",  R.color.category_personal))
            data.add(CategoryExpense("Medical", R.color.category_medical))
            data.add(CategoryExpense("Others", R.color.category_others))

            if(cursor != null && cursor.moveToFirst()) {
                val groupIndex = cursor.getColumnIndex(groupColumn)
                val sumIndex = cursor.getColumnIndex(sumColumn)
                if(groupIndex != -1 && sumIndex != -1) {
                    do {
                        var index = -1
                        when(cursor.getString(groupIndex)) {
                            "Entertainment" -> index = ENT_INDEX
                            "Food" -> index = FOOD_INDEX
                            "Transportation" -> index = TRANS_INDEX
                            "Utilities" -> index = UTIL_INDEX
                            "Personal" -> index = PERS_INDEX
                            "Medical" -> index = MED_INDEX
                            "Others" -> index = OTHERS_INDEX
                        }
                        if (index != -1) {
                            val amount = cursor.getFloat(sumIndex)
                            data[index].total += amount
                            total += amount
                        }
                    } while(cursor.moveToNext())

                   cursor.close()
                }
            }

            for (category in data){
                category.percent = floor((category.total / total).toDouble() * 100).toInt()
            }

            return data
        }

        fun getRooms(): ArrayList<RoomModel> {
            val data = ArrayList<RoomModel>()

            data.add(RoomModel(R.drawable.temp_room, "King"))
            data.add(RoomModel(R.drawable.temp_room, "Not the Best"))
            data.add(RoomModel(R.drawable.temp_room, "Peasant"))
            data.add(RoomModel(R.drawable.temp_room, "Meh"))
            data.add(RoomModel(R.drawable.temp_room, "Pog"))
            data.add(RoomModel(R.drawable.temp_room, "Cringe"))

            return data
        }
    }
}