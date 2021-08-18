package com.mobdeve.s13.group1.budgetbuilder

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
    }
}