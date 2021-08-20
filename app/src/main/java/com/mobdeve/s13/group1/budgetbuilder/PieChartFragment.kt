package com.mobdeve.s13.group1.budgetbuilder

import android.graphics.Color
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_pie_chart, container, false)

        expenses = DataHelper.getExpenses()
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
        chartPie.setHoleRadius(30f)
        chartPie.setTransparentCircleRadius(35f)

        //set to true to see label of each expense in chart itself
        pieDataSet.setValueFormatter(PercentFormatter())
        chartPie.setDrawEntryLabels(false)

        //set to true to see value of each expense in chart itself
        pieDataSet.setDrawValues(true)

        //set to true to show percent values
        chartPie.setUsePercentValues(true)

        //set to true to show legends
        chartPie.legend.isEnabled = false

        return rootView
    }

}