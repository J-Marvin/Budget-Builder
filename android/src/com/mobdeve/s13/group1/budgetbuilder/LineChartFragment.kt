package com.mobdeve.s13.group1.budgetbuilder

import android.graphics.Color
import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_line_chart.view.*

class LineChartFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_line_chart, container, false)

        initLineChart(rootView.chart_line)

        return rootView
    }

    fun initLineChart(lineChart: LineChart) {
        var dataset = DataHelper.getExpenses()

        //hide grid lines
        lineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

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
        val entries: ArrayList<Entry> = ArrayList()

        //you can replace this data object with  your custom object
        for (i in dataset.indices) {
            val expense = dataset[i]
            entries.add(Entry(i.toFloat(), expense.amount!!))
        }

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

//    inner class MyAxisFormatter : IndexAxisValueFormatter() {
//        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
//            val index = value.toInt()
//            return if (index < scoreList.size) {
//                scoreList[index].name
//            } else {
//                ""
//            }
//        }
//    }
}