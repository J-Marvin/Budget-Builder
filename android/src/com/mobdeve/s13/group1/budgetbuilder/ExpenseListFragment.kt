package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_expense_list.view.*

class ExpenseListFragment : Fragment() {
    lateinit var expenses: ArrayList<Expense>
    lateinit var db: BudgetBuilderDbHelper
    lateinit var expenseAdapter: ExpenseAdapter
    lateinit var sendFragmentData: SendFragmentData

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = BudgetBuilderDbHelper(context)
        sendFragmentData = context as SendFragmentData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_expense_list, container, false)

        //initialize recycler view
        initExpenseRecyclerView(rootView)

        return rootView
    }

    private fun initExpenseRecyclerView(rootView: View){

        //determine which fragment this list is called
        var homeFrag = this.parentFragmentManager.findFragmentById(R.id.fragView_expenselist_home)
        var fragmentCaller: String

        //if not home fragment, then display all expense items
        if(homeFrag == null) {
            this.expenses = db.findAllExpensesBetween("2019-08-01", "2022-08-31") //date should be from selected month and year
            fragmentCaller = "ExpenseFragment"
        }
        //if home fragment, display only 3 latest expense items
        else {
            this.expenses = db.findRecentExpenses()
            fragmentCaller = "HomeFragment"
        }

        this.expenseAdapter = ExpenseAdapter(childFragmentManager, this.expenses)
        rootView.rv_expenses.adapter = this.expenseAdapter
        sendFragmentData.sendExpenseAdapter(this.expenseAdapter, fragmentCaller)
        rootView.rv_expenses.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    }

}