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
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * This class handles the pie chart in the summary fragment
 */
class PieChartFragment : Fragment() {
    lateinit var expens: ArrayList<ExpenseModel>
    private lateinit var categoryExpenses: ArrayList<CategoryExpense>
    private lateinit var db: ExpenseDAOImpl
    private lateinit var pieExpenses: ArrayList<PieEntry>
    private lateinit var parentFragment: SummaryFragment
    private lateinit var executorService: ExecutorService
    private lateinit var expenseColors: ArrayList<Int>

    companion object{
        /**
         * This function creates a new instance of the SetBudgetFragment given the parameters
         * @param month the current month
         * @param year the current year
         */
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
        executorService = Executors.newSingleThreadExecutor()
        categoryExpenses = ArrayList()
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
        var month = parentFragment.getMonth()
        var year = parentFragment.getYear()

        arguments.let {
            if (it != null) {
                month = it.getInt(Keys.KEY_MONTH.toString(), today.get(Calendar.MONTH))
                year = it.getInt(Keys.KEY_MONTH.toString(), today.get(Calendar.YEAR))
            }
        }
        var expenses = db.getSumPerCategoryBetweenDate(DateHelper.getStartDateString(month!!, year!!),
            DateHelper.getEndDateString(month!!, year!!)
        )
        updatePieChart(expenses)
    }

    /**
     * This function initializes the pie chart for the current date
     * @param view the parent view of this view
     */
    fun initPieChart(view: View) {
        var chartPie = view.chart_pie


        expenseColors = ArrayList<Int>()
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

    /**
     * This function updates the pie chart if a new expense is added
     * @param expenses the array list for the expenses grouped by category
     */
    fun updatePieChart(expenses: ArrayList<CategoryExpense>) {
        executorService.run {
            pieExpenses.clear()
            categoryExpenses.clear()
            categoryExpenses.addAll(expenses)
            updateColors()
            categoryExpenses.forEach {
                if (it.total > 0)
                    pieExpenses.add(PieEntry(it.total, it. type))
            }
            requireActivity().runOnUiThread {
                if (pieExpenses.size == 0) {
                    view?.chart_pie?.visibility = View.GONE
                    parentFragment.setMessageVisibility(View.VISIBLE)
                } else {
                    view?.chart_pie?.visibility = View.VISIBLE
                    parentFragment.setMessageVisibility(View.GONE)
                }
                view?.chart_pie?.notifyDataSetChanged()
                view?.chart_pie?.invalidate()
            }
        }

    }

    /**
     * This function gets the expenses by month
     * @param start the start date
     * @param end the end date
     * @return an array list of expenses per category
     */
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

    /**
     * This function updates the colors of the chart
     */
    private fun updateColors() {
        expenseColors.clear()

        for (expense in categoryExpenses) {
            if (expense.total > 0) {
                expenseColors.add( Color.parseColor(
                    when (expense.type) {
                    ExpenseType.ENTERTAINMENT.textType -> ExpenseType.ENTERTAINMENT.iconColor
                    ExpenseType.FOOD.textType -> ExpenseType.FOOD.iconColor
                    ExpenseType.MEDICAL.textType -> ExpenseType.MEDICAL.iconColor
                    ExpenseType.PERSONAL.textType -> ExpenseType.PERSONAL.iconColor
                    ExpenseType.TRANSPORTATION.textType -> ExpenseType.TRANSPORTATION.iconColor
                    ExpenseType.UTILITIES.textType -> ExpenseType.UTILITIES.iconColor
                    else -> ExpenseType.OTHERS.iconColor
                }))
            }
        }
    }
}