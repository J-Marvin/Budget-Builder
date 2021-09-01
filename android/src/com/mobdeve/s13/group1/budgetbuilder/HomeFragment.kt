package com.mobdeve.s13.group1.budgetbuilder

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment(), BudgetHandler {
    lateinit var sp: SharedPreferences
    lateinit var spEditor: SharedPreferences.Editor
    lateinit var dbHelper: BudgetBuilderDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        sp = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
        spEditor = sp.edit()

        dbHelper = BudgetBuilderDbHelper(requireActivity().applicationContext)

        setBudget(rootView)

        rootView.btn_see_all.setOnClickListener{
           Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_expenseFragment)
        }

        rootView.iv_home_room.setOnClickListener{
            Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_galleryFragment)
        }

        rootView.view_set_budget.setOnClickListener{
//            Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_setBudgetFragment)
            val dialog = SetBudgetFragment.newInstance(sp.getFloat(Keys.KEY_BUDGET.toString(), 5000F))
            dialog.listener = this
            dialog.onDismissListener = DialogInterface.OnDismissListener {
                setBudget(rootView)
            }

            dialog.show(requireActivity().supportFragmentManager, "setBudget_tag")
        }

        rootView.btn_home_settings.setOnClickListener {
            Navigation.findNavController(rootView).navigate(R.id.action_global_settingsFragment)
        }

        updateBalance(rootView)

        return rootView
    }

    private fun setBudget(rootView: View) {
        val currency = sp.getString(Keys.KEY_CURRENCY.toString(), "$")
        val budget = sp.getFloat(Keys.KEY_BUDGET.toString(), 5000f)

        val budgetFloor = kotlin.math.floor(budget).toInt()


        rootView.tv_budget_amount.text = "$currency$budgetFloor"
    }

    private fun updateDifference(rootView: View) {
        val budget = sp.getFloat(Keys.KEY_BUDGET.toString(), 5000f)
    }

    private fun updateBalance(rootView: View){
        val bal = sp.getInt(Keys.KEY_BALANCE.toString(), 100)
        rootView.tv_coin_balance.text = bal.toString()

    }

    override fun setBudget(budget: Float) {
        val budgetId = sp.getString(Keys.KEY_BUDGET_ID.toString(), null)
        if (budgetId !== null) {
            dbHelper.updateBudget(budgetId, budget)
        }
        spEditor.putFloat(Keys.KEY_BUDGET.toString(), budget)
        spEditor.commit()
    }

    override fun cancelBudget() {
    }


}