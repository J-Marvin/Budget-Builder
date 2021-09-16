package com.mobdeve.s13.group1.budgetbuilder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_expense.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*

/** This class represents a Expense Fragment*/
class ExpenseFragment : Fragment(), DatePickerListener {

    private var month: Int? = null
    private var year: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_expense, container, false)

        initDate(rootView)

        // initialize settings button
        rootView.btn_expense_settings.setOnClickListener {
            Navigation.findNavController(rootView).navigate(R.id.action_global_settingsFragment)
        }

        initDatePicker(rootView)

        return rootView
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setExpenseListener(null)
    }

    /** This method initializes the date shown
     *  @param rootView - the view of the fragment
     *  */
    private fun initDate(rootView: View) {
        val today = Calendar.getInstance()
        val displayName = today.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)
        month = today.get(Calendar.MONTH)
        year = today.get(Calendar.YEAR)
        rootView.btn_expense_date.text = String.format(resources.getString(R.string.month_year), displayName, year)
    }

    /** This method initializes the date picker
     *  @param rootView - the view of the fragment
     *  */
    private fun initDatePicker(rootView: View) {
        rootView.btn_expense_date.setOnClickListener {
            var dialog = DatePickerDialogFragment.newInstance(month!!, year!!)

            dialog.listener = this

            dialog.show(requireActivity().supportFragmentManager, "expenseDatePicker_tag")
        }
    }

    /** This method updates the month and year shown
     *  @param month - the month (index 0)
     *  @param year - the year
     * */
    private fun updateDate(month: Int, year: Int) {
        this.month = month
        this.year = year

        val time = Calendar.getInstance()
        time.set(Calendar.MONTH, month)
        time.set(Calendar.YEAR, year)
        val displayName = time.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)
        view?.btn_expense_date?.text = String.format(
            resources.getString(R.string.month_year), displayName, year)
    }

    override fun onSelectDate(month: Int, year: Int) {
        var fragment = this.childFragmentManager
            .findFragmentById(R.id.fragView_expense_list) as ExpenseListFragment
        fragment.refreshRecyclerView(month, year)
        updateDate(month, year)
        Log.d("TIME", "$month, $year")
    }

    override fun onCancelDate() {
    }

}