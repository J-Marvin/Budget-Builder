package com.mobdeve.s13.group1.budgetbuilder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_expense.view.*
import kotlinx.android.synthetic.main.fragment_shop.view.*
import kotlinx.android.synthetic.main.fragment_shop.view.rv_chairs

class ExpenseFragment : Fragment() {

    lateinit var expenses: ArrayList<Expense>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_expense, container, false)
        //initialize recycler view
        initExpenseRecyclerView(rootView)

        return rootView
    }

    private fun initExpenseRecyclerView(rootView: View){
        this.expenses = DataHelper.getExpenses()
        rootView.rv_expenses.adapter = ExpenseAdapter(this.expenses)
        rootView.rv_expenses.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    }
}