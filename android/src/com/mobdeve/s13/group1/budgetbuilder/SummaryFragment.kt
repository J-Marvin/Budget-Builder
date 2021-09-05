package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseDAOImpl
import kotlinx.android.synthetic.main.fragment_summary.view.*
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SummaryFragment : Fragment(), DatePickerListener {

    private lateinit var db: ExpenseDAOImpl
    private var month: Int? = null
    private var year: Int? = null
    private lateinit var executorService: ExecutorService
    private lateinit var data: ArrayList<CategoryExpense>
    private var isPie: Boolean = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = ExpenseDAOImpl(activity?.applicationContext!!)

        executorService = Executors.newSingleThreadExecutor()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_summary, container, false)
        initRecyclerView(rootView)
        initDate(rootView)
        initDatePicker(rootView)

        rootView.btn_summary_settings.setOnClickListener {
            Navigation.findNavController(rootView).navigate(R.id.action_global_settingsFragment)
        }

        rootView.radiobtn_line.setOnClickListener {
            if(it.radiobtn_line.isChecked) {
                isPie = false
                this.childFragmentManager.commit {
                    replace<LineChartFragment>(R.id.fragView_summary)
                }
            }
        }

        rootView.radiobtn_pie.setOnClickListener {
            if(it.radiobtn_pie.isChecked) {
                isPie = true
                this.childFragmentManager.commit {
                    replace<PieChartFragment>(R.id.fragView_summary)
                }
            }
        }

        return rootView
    }

    private fun initRecyclerView(rootView: View){
        val firstDay = Calendar.getInstance()

        firstDay.apply {
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val lastDay = Calendar.getInstance()

        lastDay.apply {
            set(Calendar.DAY_OF_MONTH, this.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        val expenses = db.getExpensesByDate(FormatHelper.dateFormatterNoTime.format(firstDay.time),
            FormatHelper.dateFormatterNoTime.format(lastDay.time), false)
        data = DataHelper.getCategoryExpenses(expenses)
        data.sortWith { o1, o2 -> o1?.total!!.compareTo(o2?.total!!) * -1}
        rootView.rv_category_expenses.adapter =
            activity?.applicationContext?.let { CategoryExpenseAdapter(data, it) }
        rootView.rv_category_expenses.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initDate(rootView: View) {
        val today = Calendar.getInstance()
        val displayName = today.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)
        month = today.get(Calendar.MONTH)
        year = today.get(Calendar.YEAR)
        rootView.btn_summary_date.text = String.format(resources.getString(R.string.month_year), displayName, year)
    }

    private fun updateDate(month: Int, year: Int) {
        this.month = month
        this.year = year

        val time = Calendar.getInstance()
        time.set(Calendar.MONTH, month)
        time.set(Calendar.YEAR, year)
        val displayName = time.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)
        view?.btn_summary_date?.text = String.format(
            resources.getString(R.string.month_year), displayName, year)
    }

    private fun initDatePicker(rootView: View) {
        rootView.btn_summary_date.setOnClickListener {
            var dialog = DatePickerDialogFragment.newInstance(month!!, year!!)

            dialog.listener = this

            dialog.show(requireActivity().supportFragmentManager, "expenseDatePicker_tag")
        }
    }

    private fun getCategoryExpenses(rootView: View) {

    }

    private fun updateRecyclerView(month: Int, year: Int) {
        executorService.run {
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

            val expenses = db.getExpensesByDate(FormatHelper.dateFormatterNoTime.format(firstDay.time),
                FormatHelper.dateFormatterNoTime.format(lastDay.time), false)
            data.clear()
            data.addAll(DataHelper.getCategoryExpenses(expenses))
            data.sortWith { o1, o2 -> o1?.total!!.compareTo(o2?.total!!) * -1 }

            view?.rv_category_expenses?.adapter?.notifyDataSetChanged()
        }

    }

    override fun onSelectDate(month: Int, year: Int) {
        updateDate(month, year)
        updateRecyclerView(month, year)

        var fragment = childFragmentManager.findFragmentById(R.id.fragView_summary)

        if (isPie) {
            (fragment as PieChartFragment).updatePieChart(month, year)
        } else {

        }
    }

    override fun onCancelDate() {
    }

}