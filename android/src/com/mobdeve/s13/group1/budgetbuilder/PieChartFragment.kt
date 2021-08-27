package com.mobdeve.s13.group1.budgetbuilder

import android.database.DatabaseUtils
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
    lateinit var categoryExpenses: ArrayList<CategoryExpense>
    private lateinit var db: BudgetBuilderDbHelper

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
        expenses = db.findAllExpensesBetween("2019-08-01", "2022-08-31") //should get from db
        categoryExpenses = this.getMonthExpenses("2019-08-01", "2022-08-31")

        var pieExpenses = ArrayList<PieEntry>()
        var chartPie = rootView.chart_pie

        categoryExpenses.forEach {
            pieExpenses.add(PieEntry(it.total, it.name))
        }

        pieExpenses.forEach{
            Log.d("PIE EXPENSES", it.label + " " + it.value)
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

        return rootView
    }

    fun getMonthExpenses(start: String, end:String): ArrayList<CategoryExpense> {
        val aggs = ArrayList<HashMap<String, String>>()
        val sumHash = HashMap<String, String>()
        sumHash[Keys.KEY_AGG_TYPE.toString()] = BudgetBuilderDbHelper.AGG_SUM
        sumHash[Keys.KEY_COLUMN_NAME.toString()] = BudgetBuilderDbHelper.COLUMN_EXPENSE_AMOUNT
        sumHash[Keys.KEY_COLUMN_ALIAS.toString()] = "SUM"
        aggs.add(sumHash)

        val cursor = db.findAggExpensesBetween(
            start,
            end,
            arrayListOf(BudgetBuilderDbHelper.COLUMN_EXPENSE_TYPE),
            aggs,
            arrayListOf<String>(BudgetBuilderDbHelper.COLUMN_EXPENSE_TYPE),
            null,
            null,
            null
        )

        Log.d("DB-DUMP", DatabaseUtils.dumpCursorToString(cursor))

        return DataHelper.getCategoryExpensesSumFromCursor(cursor, "SUM", BudgetBuilderDbHelper.COLUMN_EXPENSE_TYPE)
    }
}