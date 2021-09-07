package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.mobdeve.s13.group1.budgetbuilder.dao.BudgetDAOImpl
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseDAOImpl
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class HomeFragment : Fragment(), BudgetHandler {
    lateinit var sp: SharedPreferences
    lateinit var spEditor: SharedPreferences.Editor
    lateinit var budgetDb: BudgetDAOImpl
    lateinit var expensesDb: ExpenseDAOImpl
    lateinit var executorService: ExecutorService
    lateinit var today: Calendar
    var expenses: Float = 0F
    var prevDate: Calendar? = null
    var balance: Int = 0
    var budget: Float = 5000F
    lateinit var currency: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        budgetDb = BudgetDAOImpl(context)
        expensesDb = ExpenseDAOImpl(context)

        sp = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
        spEditor = sp.edit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        today = Calendar.getInstance()
        executorService = Executors.newSingleThreadExecutor()

        loadSettings()
        initDaily()
        loadExpenses()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        rootView.btn_see_all.setOnClickListener{
           Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_expenseFragment)
        }

        rootView.view_goto_gallery.setOnClickListener{
            Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_galleryFragment)
        }

        rootView.view_set_budget.setOnClickListener{
            val dialog = SetBudgetFragment.newInstance(budget)
            dialog.listener = this
            dialog.onDismissListener = DialogInterface.OnDismissListener {
                showBudget()
                showDifference()
            }

            dialog.show(requireActivity().supportFragmentManager, "setBudget_tag")
        }

        rootView.btn_home_settings.setOnClickListener {
            Navigation.findNavController(rootView).navigate(R.id.action_global_settingsFragment)
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()
        loadSettings()
        loadExpenses()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDate()
        showBalance()
        showBudget()
        showDifference()
        showExpenses()
    }

    private fun loadExpenses() {
        executorService.run {
            val strToday = FormatHelper.dateFormatterNoTime.format(today.time)
            val tomorrow = Calendar.getInstance()
            tomorrow.add(Calendar.DAY_OF_MONTH, 1)
            val strTomorrow = FormatHelper.dateFormatterNoTime.format(tomorrow.time)
            expenses = expensesDb.getSumOfDate(strToday, strTomorrow)
        }
    }


    private fun initDate() {
        val month = today.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)
        val year = today.get(Calendar.YEAR)
        val day = today.get(Calendar.DAY_OF_MONTH)
        requireView().tv_curr_date.text = String
            .format(resources.getString(R.string.curr_date), month, day, year)
    }

    private fun loadSettings() {
        if (sp.contains(Keys.KEY_BALANCE.toString())) {
            balance = sp.getInt(Keys.KEY_BALANCE.toString(), 20)
        } else {
            spEditor.putInt(Keys.KEY_BALANCE.toString(), 20)
            spEditor.commit()
            balance = 20
        }

        currency = sp.getString(Keys.KEY_CURRENCY.toString(), "$")!!

        val tempDate = sp.getString(Keys.KEY_PREV_DATE.toString(), "")
        if (tempDate != null && tempDate.isNotEmpty()) {
            prevDate = Calendar.getInstance()
            prevDate?.time = FormatHelper.dateFormatterNoTime.parse(tempDate)!!
        }
    }

    private fun showBudget() {
        requireView().tv_budget_amount.text = String.format(resources.getString(R.string.budget), currency,budget)
    }

    private fun showDifference() {
        requireView()
            .tv_remaining_budget
            .text = String.format(resources.getString(R.string.budget), currency,(budget-expenses))
    }

    private fun showExpenses() {
        requireView()
            .tv_budget_expense
            .text = String.format(resources.getString(R.string.budget), currency, expenses)
    }

    private fun showBalance(){
        requireView().tv_coin_balance.text = balance.toString()
    }

    private fun updateBalance(bal: Int){
        balance += bal
        view?.tv_coin_balance?.text = balance.toString()
        spEditor.putInt(Keys.KEY_BALANCE.toString(), balance)
        spEditor.apply()
    }

    override fun okBudget(budget: Float) {
        val budgetId = sp.getString(Keys.KEY_BUDGET_ID.toString(), null)
        this.budget = budget
        if (budgetId !== null) {
            budgetDb.updateBudget(budgetId, budget)
        }
        spEditor.putFloat(Keys.KEY_BUDGET.toString(), budget)
        spEditor.commit()
    }

    override fun cancelBudget() {
    }

    private fun initDaily() {
        val dateFormatter = FormatHelper.dateFormatterNoTime
        initBudget()
        updateDate(dateFormatter.format(today.time))
    }

    private fun updateDate(date: String) {
        Toast.makeText(activity?.applicationContext, "DATE HERE", Toast.LENGTH_SHORT).show()
        spEditor.putString(Keys.KEY_PREV_DATE.toString(), date)
        spEditor.commit()
    }

    private fun initCoins() {
        if (!isSameDate(today, prevDate) && prevDate != null) {
            executorService.execute {
                val strPrevDate = FormatHelper.DATE_FORMAT.format(prevDate)
                val sum = expensesDb.getSumOfDate(strPrevDate, null)
                val budget = budgetDb.getBudgetByDate(strPrevDate)
                var percent = 0F

                if (budget != null)
                    percent = sum / budget.budget!!

                val total = kotlin.math.floor( 10F + (1 - percent) * 10).toInt()


                requireActivity().runOnUiThread {
                    val dialog = CoinDialogFragment.newInstance(total)
                    dialog.show(requireActivity().supportFragmentManager, "earnCoins_tag")
                    updateBalance(total)
                }
            }

        }
    }

    private fun initBudget() {
        val dateFormatter = FormatHelper.dateFormatterNoTime

        if (sp.contains(Keys.KEY_DEFAULT_BUDGET.toString())) {
            val strToday = dateFormatter.format(today.time)
            val budgetId = budgetDb.addBudget(budget, strToday)
            budget = sp.getFloat(Keys.KEY_DEFAULT_BUDGET.toString(), 5000f)
            spEditor.putString(Keys.KEY_BUDGET_ID.toString(), budgetId.toString())
            spEditor.putFloat(Keys.KEY_BUDGET.toString(), budget)
            spEditor.commit()
            initCoins()
        } else if (!isSameDate(today, prevDate)) {
                // show set budget
                showInitSetBudget()
        } else if (isSameDate(today, prevDate)){
            val budgetId = sp.getString(Keys.KEY_BUDGET_ID.toString(), "")
            if (budgetId?.isNotBlank() == true) {
                budget = budgetDb.getBudgetById(budgetId)?.budget!!
            }
        }

    }

    private fun isSameDate(d1: Calendar, d2: Calendar?): Boolean {
        return if (d2 == null) false
        else d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH) &&
            d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR) &&
            d1.get(Calendar.DAY_OF_MONTH) == d2.get(Calendar.DAY_OF_MONTH)
    }

    private fun showInitSetBudget() {
        val budgetDialog = SetBudgetFragment.newInstance(sp.getFloat(Keys.KEY_BUDGET.toString(), 5000F), false)
        budgetDialog.listener = object: BudgetHandler {
            override fun okBudget(budget: Float) {
                val today = FormatHelper.DATE_NO_TIME_FORMAT.format(Calendar.getInstance().time)
                val budgetId = budgetDb.addBudget(budget, today)
                spEditor.putString(Keys.KEY_BUDGET_ID.toString(), budgetId.toString())
                spEditor.putFloat(Keys.KEY_BUDGET.toString(), budget)
                spEditor.commit()
                this@HomeFragment.budget = budget

                if (this@HomeFragment.view !== null) {
                    showBudget()
                    showDifference()
                }
            }

            override fun cancelBudget() {
                val today = FormatHelper.DATE_NO_TIME_FORMAT.format(Calendar.getInstance().time)
                this@HomeFragment.budget = 5000f

                val budgetId = budgetDb.addBudget(budget, today)
                spEditor.putFloat(Keys.KEY_BUDGET.toString(), budget)
                spEditor.putString(Keys.KEY_BUDGET_ID.toString(), budgetId.toString())
                spEditor.commit()

                if (this@HomeFragment.view !== null) {
                    showBudget()
                    showDifference()
                }
            }

        }

        budgetDialog.onDismissListener = DialogInterface.OnDismissListener {
            initCoins()
        }

        budgetDialog.show(requireActivity().supportFragmentManager, "setBudget_tag")
    }

}