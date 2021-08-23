package com.mobdeve.s13.group1.budgetbuilder

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.fragment_pie_chart.view.*


class PieChartFragment : Fragment() {
    lateinit var expenses: ArrayList<Expense>
    private lateinit var db: BudgetBuilderDbHelper

    private companion object {
        const val ENT_INDEX = 0
        const val FOOD_INDEX = 1
        const val TRANS_INDEX = 2
        const val UTIL_INDEX = 3
        const val PERS_INDEX = 4
        const val MED_INDEX = 5
        const val OTHERS_INDEX = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_pie_chart, container, false)

        db = BudgetBuilderDbHelper(activity?.applicationContext!!)
//        expenses = DataHelper.getExpenses() //should get from db
        expenses = db.findAllExpensesBetween("2019-08-01", "2022-08-31")
        var pieExpenses = ArrayList<PieEntry>()
        var chartPie = rootView.chart_pie

        expenses.forEach {
            pieExpenses.add(PieEntry(it.amount, it.type))
        }

        var pieDataSet = PieDataSet(pieExpenses, "Expenses")

        var expenseColors = ArrayList<Int>()

        ExpenseType.values().forEach {
            expenseColors.add(Color.parseColor(it.iconColor))
        }

        pieDataSet.colors = expenseColors
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 16f

        chartPie.data = PieData(pieDataSet)
        chartPie.description.isEnabled = false
        chartPie.centerText = "Expenses"
        chartPie.setHoleColor(Color.argb(0,0,0,0))
        chartPie.holeRadius = 30f
        chartPie.transparentCircleRadius = 35f

        //set to true to see label of each expense in chart itself
        pieDataSet.valueFormatter = PercentFormatter()
        chartPie.setDrawEntryLabels(false)

        //set to true to see value of each expense in chart itself
        pieDataSet.setDrawValues(true)

        //set to true to show percent values
        chartPie.setUsePercentValues(true)

        //set to true to show legends
        chartPie.legend.isEnabled = false

        //set to true to enable rotation of chart
        chartPie.isRotationEnabled = false
        var sum = 0f
        for(expense in getExpensesByCategory()){
            Log.d("Expenses", expense.toString())
            sum+=expense
        }

        for(expense in getExpensesByCategory()){
            val percent = expense / sum * 100
            Log.d("Percent", percent.toString())
        }
        return rootView
    }

    fun getExpensesByCategory(): FloatArray{
        val expenses = floatArrayOf(0F, 0F, 0F, 0F, 0F, 0F, 0F)

        for (expense in this.expenses) {
            when(expense.type) {
                "Entertainment" -> expenses[ENT_INDEX] += expense.amount
                "Food" -> expenses[FOOD_INDEX] += expense.amount
                "Transportation" -> expenses[TRANS_INDEX] += expense.amount
                "Utilities" -> expenses[UTIL_INDEX] += expense.amount
                "Personal" -> expenses[PERS_INDEX] += expense.amount
                "Medical" -> expenses[MED_INDEX] += expense.amount
                "Others" -> expenses[OTHERS_INDEX] += expense.amount
            }
        }

        return expenses
    }
}