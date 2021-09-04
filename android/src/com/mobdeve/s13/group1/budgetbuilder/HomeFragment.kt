package com.mobdeve.s13.group1.budgetbuilder

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.mobdeve.s13.group1.budgetbuilder.dao.BudgetDAOImpl
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*


class HomeFragment : Fragment(), BudgetHandler {
    lateinit var sp: SharedPreferences
    lateinit var spEditor: SharedPreferences.Editor
    lateinit var budgetDb: BudgetDAOImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        budgetDb = BudgetDAOImpl(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        sp = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
        spEditor = sp.edit()

        setBudget(rootView)
        initBudget()
        initDate(rootView)

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

    private fun initDate(rootView: View) {
        val today = Calendar.getInstance()
        val month = today.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)
        val year = today.get(Calendar.YEAR)
        val day = today.get(Calendar.DAY_OF_MONTH)
        rootView.tv_curr_date.text = String.format(resources.getString(R.string.curr_date), month, day, year)
    }

    private fun setBudget(rootView: View) {
        val currency = sp.getString(Keys.KEY_CURRENCY.toString(), "$")
        val budget = sp.getFloat(Keys.KEY_BUDGET.toString(), 5000f)

        val budgetFloor = kotlin.math.floor(budget).toInt()

        rootView.tv_budget_amount.text = String.format(resources.getString(R.string.budget), currency,budget)
    }

    private fun updateDifference(rootView: View) {
        val budget = sp.getFloat(Keys.KEY_BUDGET.toString(), 5000f)
    }

    private fun updateBalance(rootView: View){
        val bal = sp.getInt(Keys.KEY_BALANCE.toString(), 100)
        rootView.tv_coin_balance.text = bal.toString()
    }

    fun setBudget() {
        val currency = sp.getString(Keys.KEY_CURRENCY.toString(), "$")
        val budget = sp.getFloat(Keys.KEY_BUDGET.toString(), 5000f)

        val budgetFloor = kotlin.math.floor(budget).toInt()

        this.view?.tv_budget_amount?.text = String.format(resources.getString(R.string.budget), currency,budget)
    }

    override fun setBudget(budget: Float) {
        val budgetId = sp.getString(Keys.KEY_BUDGET_ID.toString(), null)
        val db = BudgetDAOImpl(requireActivity().applicationContext)
        if (budgetId !== null) {
            db.updateBudget(budgetId, budget)
        }
        spEditor.putFloat(Keys.KEY_BUDGET.toString(), budget)
        spEditor.commit()
    }

    override fun cancelBudget() {
    }

    fun initBudget() {
        val prevDateString = sp.getString(Keys.KEY_PREV_DATE.toString(), "")
        val today = Calendar.getInstance()
        val dateFormatter = FormatHelper.dateFormatterNoTime
        var prevDate: Calendar

        if (sp.contains(Keys.KEY_DEFAULT_BUDGET.toString())) {
            val strToday = dateFormatter.format(today.time)
            val budget = sp.getFloat(Keys.KEY_DEFAULT_BUDGET.toString(), 5000f)
            val budgetId = budgetDb.addBudget(budget, strToday)
            spEditor.putString(Keys.KEY_PREV_DATE.toString(), strToday)
            spEditor.putString(Keys.KEY_BUDGET_ID.toString(), budgetId.toString())
            spEditor.putFloat(Keys.KEY_BUDGET.toString(), budget)
            spEditor.commit()
        } else {
            if (prevDateString != null) {
                if (prevDateString.isNotEmpty()) {
                    prevDate = Calendar.getInstance()

                    // If not the same day
                    if (isSameDate(prevDate, today)) {
                        // show set budget
                        showInitSetBudget()
                    }
                } else {
                    showInitSetBudget()
                }
            }
        }
    }

    fun isSameDate(d1: Calendar, d2: Calendar): Boolean {
        return d1.get(Calendar.MONTH) != d2.get(Calendar.MONTH) ||
        d1.get(Calendar.YEAR) != d2.get(Calendar.YEAR) ||
        d1.get(Calendar.DAY_OF_MONTH) != d2.get(Calendar.DAY_OF_MONTH)
    }

    fun showInitSetBudget() {
        val budgetDialog = SetBudgetFragment.newInstance(sp.getFloat(Keys.KEY_BUDGET.toString(), 5000F), false)
        budgetDialog.listener = object: BudgetHandler {
            override fun setBudget(budget: Float) {
                val today = FormatHelper.DATE_NO_TIME_FORMAT.format(Calendar.getInstance().time)
                val budgetId = budgetDb.addBudget(budget, today)
                spEditor.putString(Keys.KEY_PREV_DATE.toString(), today)
                spEditor.putString(Keys.KEY_BUDGET_ID.toString(), budgetId.toString())
                spEditor.putFloat(Keys.KEY_BUDGET.toString(), budget)
                spEditor.commit()
                setBudget(view!!)
            }

            override fun cancelBudget() {
                val today = FormatHelper.DATE_NO_TIME_FORMAT.format(Calendar.getInstance().time)
                var budget = 5000f
                if (sp.contains(Keys.KEY_DEFAULT_BUDGET.toString())) {
                    budget = sp.getFloat(Keys.KEY_DEFAULT_BUDGET.toString(), 5000f)
                }
                val budgetId = budgetDb.addBudget(budget, today)
                spEditor.putFloat(Keys.KEY_BUDGET.toString(), budget)
                spEditor.putString(Keys.KEY_BUDGET_ID.toString(), budgetId.toString())
                spEditor.putString(Keys.KEY_PREV_DATE.toString(), today)
                spEditor.commit()
                setBudget(view!!)
            }

        }
        budgetDialog.onDismissListener = DialogInterface.OnDismissListener {
        }
        budgetDialog.show(requireActivity().supportFragmentManager, "setBudget_tag")
    }

}