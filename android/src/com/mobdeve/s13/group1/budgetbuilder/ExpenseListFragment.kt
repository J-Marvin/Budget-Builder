package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s13.group1.budgetbuilder.dao.BudgetBuilderDbHelper
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseDAOImpl
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseModel
import kotlinx.android.synthetic.main.fragment_expense_list.view.*

class ExpenseListFragment : Fragment() {
    lateinit var expens: ArrayList<ExpenseModel>
    lateinit var db: ExpenseDAOImpl
    lateinit var expenseAdapter: ExpenseAdapter
    lateinit var sendFragmentData: SendFragmentData

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = ExpenseDAOImpl(context)
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
            this.expens = db.getExpensesByDate("2019-08-01", "2022-08-31", false) //date should be from selected month and year
            fragmentCaller = "ExpenseFragment"
        }
        //if home fragment, display only 3 latest expense items
        else {
            this.expens = db.getRecentExpenses()
            fragmentCaller = "HomeFragment"
        }

        this.expenseAdapter = ExpenseAdapter(childFragmentManager, this.expens)
        rootView.rv_expenses.adapter = this.expenseAdapter
        sendFragmentData.sendExpenseAdapter(this.expenseAdapter, fragmentCaller)
        rootView.rv_expenses.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    }

}