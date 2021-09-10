package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
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

class SummaryFragment : Fragment(), DatePickerListener, ExpenseHandler {

    private lateinit var db: ExpenseDAOImpl
    private var month: Int? = null
    private var year: Int? = null
    private lateinit var executorService: ExecutorService
    private lateinit var categoryExpenses: ArrayList<CategoryExpense>
    private var isPie: Boolean = true
    private lateinit var sp: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = ExpenseDAOImpl(activity?.applicationContext!!)
        sp = PreferenceManager.getDefaultSharedPreferences(context)
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

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setExpenseListener(this)
    }
    private fun initRecyclerView(rootView: View){
        val firstDay = Calendar.getInstance()

        firstDay.apply {
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val lastDay = Calendar.getInstance()

        lastDay.apply {
            set(Calendar.DAY_OF_MONTH, this.getActualMaximum(Calendar.DAY_OF_MONTH))
            add(Calendar.DAY_OF_MONTH, 1)
        }

        categoryExpenses = db.getSumPerCategoryBetweenDate(
            FormatHelper.dateFormatterNoTime.format(firstDay.time),
            FormatHelper.dateFormatterNoTime.format(lastDay.time)
        )
        categoryExpenses.sortWith { o1, o2 -> o1?.total!!.compareTo(o2?.total!!) * -1}
        rootView.rv_category_expenses.adapter =
            activity?.applicationContext?.let { CategoryExpenseAdapter(categoryExpenses, it, sp.getString(Keys.KEY_CURRENCY.toString(), "$")!!) }
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

    private fun updateRecyclerView(expenses: ArrayList<CategoryExpense>) {
        executorService.run {
            categoryExpenses.clear()
            categoryExpenses.addAll(expenses)

            view?.rv_category_expenses?.adapter?.notifyDataSetChanged()
        }
    }

    override fun onSelectDate(month: Int, year: Int) {
        updateDate(month, year)

        var fragment = childFragmentManager.findFragmentById(R.id.fragView_summary)

        var expenses = db.getSumPerCategoryBetweenDate(
            DateHelper.getStartDateString(month, year),
           DateHelper.getEndDateString(month, year)
        )
        expenses.sortWith {o1, o2 -> o1?.total!!.compareTo(o2?.total!!) * -1}

        if (isPie) {
            (fragment as PieChartFragment).updatePieChart(expenses)
            updateRecyclerView(expenses)
        } else {
            (fragment as LineChartFragment).updateChart(month, year)
            setMessageVisibility(View.GONE)
        }
    }

    override fun onCancelDate() {
    }

    fun setMessageVisibility(visibility: Int) {
        view?.tv_summary_no_data?.visibility = visibility
    }

    fun getMonth() = month
    fun getYear() = year
    override fun onAddExpense(amount: Float, type: String) {
        onSelectDate(month!!, year!!)
    }

    override fun onCancelExpense() {
    }

}