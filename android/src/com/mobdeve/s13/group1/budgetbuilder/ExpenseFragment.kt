package com.mobdeve.s13.group1.budgetbuilder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_expense.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*

class ExpenseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_expense, container, false)

        initDate(rootView)

        rootView.btn_expense_settings.setOnClickListener {
            Navigation.findNavController(rootView).navigate(R.id.action_global_settingsFragment)
        }

        return rootView
    }

    private fun initDate(rootView: View) {
        val today = Calendar.getInstance()
        val month = today.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)
        val year = today.get(Calendar.YEAR)
        rootView.btn_expense_date.text = String.format(resources.getString(R.string.month_year), month, year)
    }

}