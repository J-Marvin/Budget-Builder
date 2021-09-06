package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
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
import com.mobdeve.s13.group1.budgetbuilder.dao.DbReferences
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseDAOImpl
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseModel
import kotlinx.android.synthetic.main.fragment_pie_chart.view.*
import kotlinx.android.synthetic.main.fragment_summary.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class PieChartFragment : Fragment() {
    lateinit var expens: ArrayList<ExpenseModel>
    lateinit var categoryExpenses: ArrayList<CategoryExpense>
    private lateinit var db: ExpenseDAOImpl
    private lateinit var pieExpenses: ArrayList<PieEntry>
    private lateinit var parentFragment: SummaryFragment

    companion object{
        fun newInstance(month: Int, year: Int): SetBudgetFragment{
            val time = Calendar.getInstance()
            time.set(year, month, 1)
            val args = Bundle()
            args.apply {
                putInt(Keys.KEY_MONTH.toString(), month)
                putInt(Keys.KEY_YEAR.toString(), year)
            }
            val dialog = SetBudgetFragment()
            dialog.arguments = args
            return dialog
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = ExpenseDAOImpl(context)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pieExpenses = ArrayList<PieEntry>()

        parentFragment = getParentFragment() as SummaryFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_pie_chart, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPieChart(view)
        val today = Calendar.getInstance()
        var month = today.get(Calendar.MONTH)
        var year = today.get(Calendar.YEAR)

        arguments.let {
            if (it != null) {
                month = it.getInt(Keys.KEY_MONTH.toString(), today.get(Calendar.MONTH))
                year = it.getInt(Keys.KEY_MONTH.toString(), today.get(Calendar.YEAR))
            }
        }
        updatePieChart(month, year)
    }

    fun initPieChart(view: View) {
        var chartPie = view.chart_pie


        var expenseColors = ArrayList<Int>()
        ExpenseType.values().forEach {
            expenseColors.add(Color.parseColor(it.iconColor))
        }

        var pieDataSet = PieDataSet(pieExpenses, "Expenses")
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
    }

    fun updatePieChart(month: Int, year: Int) {
        val firstDay = Calendar.getInstance()

        firstDay.apply {
            set(Calendar.MONTH, month)
            set(Calendar.YEAR, year)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val lastDay = Calendar.getInstance()

        lastDay.apply {
            set(Calendar.MONTH, month)
            set(Calendar.YEAR, year)
            set(Calendar.DAY_OF_MONTH, this.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        pieExpenses.clear()

        val startDate: String = FormatHelper.dateFormatterNoTime.format(firstDay.time)
        val endDate: String = FormatHelper.dateFormatterNoTime.format(lastDay.time)

        categoryExpenses = this.getMonthExpenses(startDate, endDate)

        categoryExpenses.forEach {
            if (it.total > 0)
                pieExpenses.add(PieEntry(it.total, it.name))
        }

        if (pieExpenses.size == 0) {
            view?.chart_pie?.visibility = View.GONE
            parentFragment.setMessageVisibility(View.VISIBLE)
        } else {
            view?.chart_pie?.visibility = View.VISIBLE
            parentFragment.setMessageVisibility(View.GONE)
        }

        view?.chart_pie?.let{
            it.invalidate()
        }
    }

    fun getMonthExpenses(start: String, end:String): ArrayList<CategoryExpense> {
        val aggs = ArrayList<HashMap<String, String>>()
        val sumHash = HashMap<String, String>()
        sumHash[Keys.KEY_AGG_TYPE.toString()] = DbReferences.AGG_SUM
        sumHash[Keys.KEY_COLUMN_NAME.toString()] = DbReferences.COLUMN_EXPENSE_AMOUNT
        sumHash[Keys.KEY_COLUMN_ALIAS.toString()] = "SUM"
        aggs.add(sumHash)

        val cursor = db.getAggExpensesBetween(
            start,
            end,
            arrayListOf(DbReferences.COLUMN_EXPENSE_TYPE),
            aggs,
            arrayListOf<String>(DbReferences.COLUMN_EXPENSE_TYPE),
            null,
            null,
            null
        )

        Log.d("DB-DUMP", DatabaseUtils.dumpCursorToString(cursor))

        return DataHelper.getCategoryExpensesSumFromCursor(cursor, "SUM", DbReferences.COLUMN_EXPENSE_TYPE)
    }
}