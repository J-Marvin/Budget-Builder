package com.mobdeve.s13.group1.budgetbuilder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_summary.view.*

class SummaryFragment : Fragment() {

    private lateinit var db: BudgetBuilderDbHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = BudgetBuilderDbHelper(activity?.applicationContext!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_summary, container, false)
        initRecyclerView(rootView)
        return rootView
    }

    private fun initRecyclerView(rootView: View){
        val expenses = db.findAllExpensesBetween("2019-01-01", "2022-01-01")
        val data = DataHelper.getCategoryExpenses(expenses)
        rootView.rv_category_expenses.adapter =
            activity?.applicationContext?.let { CategoryExpenseAdapter(data, it) }
        rootView.rv_category_expenses.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun getCategoryExpenses(rootView: View) {

    }

}