package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseDAOImpl
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseModel
import kotlinx.android.synthetic.main.fragment_line_chart.view.*
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

/**
 * This class handles the line chart in the summary fragment
 */
class LineChartFragment : Fragment() {

    lateinit var expenseDb: ExpenseDAOImpl
    lateinit var entries: ArrayList<Entry>
    var month = Calendar.getInstance().get(Calendar.MONTH)
    var year = Calendar.getInstance().get(Calendar.YEAR)
    lateinit var executorService: ExecutorService
    lateinit var lineChart: LineChart

    override fun onAttach(context: Context) {
        super.onAttach(context)
        expenseDb = ExpenseDAOImpl(context)
        entries = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        month = (parentFragment as SummaryFragment).getMonth()!!
        year = (parentFragment as SummaryFragment).getYear()!!
        executorService = Executors.newSingleThreadExecutor()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_line_chart, container, false)

        lineChart = rootView.chart_line
        initLineChart()

        return rootView
    }

    /**
     * This function initializes the line chart with the data
     */
    fun initLineChart() {
        //hide grid lines
        lineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        lineChart.axisLeft.axisMinimum = -20F

        //remove right y-axis
        lineChart.axisRight.isEnabled = false

        //remove legend
        lineChart.legend.isEnabled = false

        //remove description label
        lineChart.description.isEnabled = false

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
//        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f

        //now draw bar chart with dynamic data
        var dataset = expenseDb.getSumPerDayOfMonth(month, year)
        entries.clear()
        for (expense in dataset) {
            val day = expense.date.get(Calendar.DAY_OF_MONTH)
            entries.add(Entry(day.toFloat(), expense.amount))
        }

        //you can replace this data object with  your custom object
//        for (expense in dataset) {
//            val day = expense.date.get(Calendar.DAY_OF_MONTH)
//            entries.add(Entry(day.toFloat(), expense.amount))
//        }

        val lineDataSet = LineDataSet(entries, "")

        lineDataSet.lineWidth = 5f
        lineDataSet.color = Color.DKGRAY
        lineDataSet.highLightColor = R.color.dark_brown
        lineDataSet.setDrawValues(false)

        //to make the smooth line as the graph is adapt change so smooth curve
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        //to enable the cubic density : if 1 then it will be sharp curve
        lineDataSet.cubicIntensity = 0.2f

        //to fill the below of smooth line in graph
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillDrawable = (ContextCompat.getDrawable(requireContext(), R.drawable.line_chart_fill))

        lineDataSet.setDrawCircles(false)

        val data = LineData(lineDataSet)
        lineChart.data = data
        lineChart.invalidate()
    }

    /**
     * This function updates the line chart when a new expense is added
     * @param month the current month
     * @param year the current year
     */
    fun updateChart(month: Int, year: Int) {
        executorService.run {
            var dataset = expenseDb.getSumPerDayOfMonth(month, year)
            entries.clear()
            for (expense in dataset) {
                val day = expense.date.get(Calendar.DAY_OF_MONTH)
                entries.add(Entry(day.toFloat(), expense.amount))
            }

            lineChart.notifyDataSetChanged()
            lineChart.invalidate()

        }
    }
}