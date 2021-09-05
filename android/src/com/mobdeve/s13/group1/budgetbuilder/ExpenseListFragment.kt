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
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class ExpenseListFragment : Fragment() {
    lateinit var expens: ArrayList<ExpenseModel>
    lateinit var db: ExpenseDAOImpl
    lateinit var expenseAdapter: ExpenseAdapter
    lateinit var sendFragmentData: SendFragmentData
    lateinit var executorService: ExecutorService

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = ExpenseDAOImpl(context)
        sendFragmentData = context as SendFragmentData
        executorService = Executors.newSingleThreadExecutor()
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
            val firstDay = Calendar.getInstance()

            firstDay.apply {
                set(Calendar.DAY_OF_MONTH, 1)
            }

            val lastDay = Calendar.getInstance()

            lastDay.apply {
                set(Calendar.DAY_OF_MONTH, this.getActualMaximum(Calendar.DAY_OF_MONTH))
            }
            this.expens = db.getExpensesByDate(FormatHelper.dateFormatterNoTime.format(firstDay.time),
                FormatHelper.dateFormatterNoTime.format(lastDay.time), false)
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

    fun refreshRecyclerView(month: Int, year: Int) {
        view?.let {
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
                expens.clear()
                expens.addAll(
                    db.getExpensesByDate(FormatHelper.dateFormatterNoTime.format(firstDay.time),
                    FormatHelper.dateFormatterNoTime.format(lastDay.time), false))
                expenseAdapter.notifyDataSetChanged()
            }
        }
    }

}