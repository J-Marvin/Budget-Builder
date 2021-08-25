package com.mobdeve.s13.group1.budgetbuilder

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        this.expenses = DataHelper.getExpenses()

        //determine which fragment this list is called
        var homeFrag = this.parentFragmentManager.findFragmentById(R.id.fragView_expenselist_home)

        //if not home fragment, then display all expense items
        //if less than 3 items, display all
        if(homeFrag == null || this.expenses.size <= 3) {
            rootView.rv_expenses.adapter = ExpenseAdapter(this.expenses)
        }
        //if home fragment, display only 3 latest expense items
        else if(this.expenses.size > 3){
            rootView.rv_expenses.adapter = ExpenseAdapter(this.expenses.take(3) as ArrayList<Expense>)
        }

        rootView.rv_expenses.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    }

}