package com.mobdeve.s13.group1.budgetbuilder

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks, SendFragmentData, BudgetHandler {

    private lateinit var db: BudgetBuilderDbHelper
    private lateinit var sp: SharedPreferences
    private lateinit var spEditor: SharedPreferences.Editor
    lateinit var roomFragment: RoomFragment
    lateinit var expenseAdapter: ExpenseAdapter
    lateinit var expenseListCaller: String

    companion object{
        fun initLayoutListener(dialog: Dialog, activity: Activity) {
            val activityRootView = dialog.window?.decorView?.findViewById<View>(android.R.id.content)
            activityRootView?.viewTreeObserver?.addOnGlobalLayoutListener {
                val heightDiff = activity.window?.decorView?.findViewById<View>(android.R.id.content)?.rootView?.height?.minus(activityRootView.height)

                if (heightDiff != null) {
                    if (heightDiff > 100) {
                        hideSystemUI(dialog.window!!)
                    }
                }
            }
        }

        fun hideSystemUI(window: Window) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        applicationContext.deleteDatabase("BudgetBuilder.db")
        db = BudgetBuilderDbHelper(this)
        initNavBar()
        this.sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        this.spEditor = this.sp.edit()
        this.spEditor.clear()
        this.spEditor.commit()

       roomFragment = RoomFragment()
    }

    override fun onResume() {
        super.onResume()
        setSystemUI()
        initNavBar()
        initBudget()
    }

    fun setSystemUI() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }

    fun initNavBar() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view).setupWithNavController(navController)

        bottom_navigation_view.background = null
        bottom_navigation_view.menu.getItem(2).isEnabled = false

        fab_add_expense.setOnClickListener{
            navController.navigate(R.id.action_global_addExpenseFragment)
        }
    }

    fun initBudget() {
        val prevDateString = sp.getString(Keys.KEY_PREV_DATE.toString(), "")
        val today = Calendar.getInstance()
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        var prevDate: Calendar

        if (sp.contains(Keys.KEY_DEFAULT_BUDGET.toString())) {
            val strToday = dateFormatter.format(today)
            val budget = sp.getFloat(Keys.KEY_DEFAULT_BUDGET.toString(), 5000f)
            val budgetId = db.addBudget(budget, strToday)
            spEditor.putString(Keys.KEY_PREV_DATE.toString(), strToday)
            spEditor.putString(Keys.KEY_BUDGET_ID.toString(), budgetId.toString())
            spEditor.putFloat(Keys.KEY_BUDGET.toString(), budget)
            spEditor.commit()
        } else {
            Log.d("Budget", "test")
            if (prevDateString != null) {
                if (prevDateString.isNotEmpty()) {
                    prevDate = Calendar.getInstance()
                    prevDate.time = dateFormatter.parse(prevDateString)

                    // If not the same day
                    if (prevDate.get(Calendar.MONTH) != today.get(Calendar.MONTH) ||
                        prevDate.get(Calendar.YEAR) != today.get(Calendar.YEAR) ||
                        prevDate.get(Calendar.DAY_OF_MONTH) != today.get(Calendar.DAY_OF_MONTH)) {
                        // show set budget
                        val dialog = SetBudgetFragment.newInstance(sp.getFloat(Keys.KEY_BUDGET.toString(), 5000F), false)
                        dialog.listener = this
                        dialog.onDismissListener = DialogInterface.OnDismissListener {

                        }
                        dialog.show(this.supportFragmentManager, "setBudget_tag")
                    }
                } else {
                    val dialog = SetBudgetFragment.newInstance(sp.getFloat(Keys.KEY_BUDGET.toString(), 5000F), false)
                    dialog.listener = this
                    dialog.onDismissListener = DialogInterface.OnDismissListener {

                    }
                    dialog.show(this.supportFragmentManager, "setBudget_tag")
                }
            }
        }
    }

    fun hideBottomNavBar() {
        bottom_navigation_view.visibility = View.GONE
        bottomAppBar.visibility = View.GONE
        fab_add_expense.visibility =View.GONE
    }

    fun showBottomNavBar() {
        bottom_navigation_view.visibility = View.VISIBLE
        bottomAppBar.visibility = View.VISIBLE
        fab_add_expense.visibility =View.VISIBLE
    }


    override fun exit() {
    }

    override fun refreshExpenseAdapter(expense: Expense) {
        this.expenseAdapter.dataSet.add(0, expense)
        if(expenseListCaller.equals("HomeFragment", true))
            this.expenseAdapter.dataSet = this.expenseAdapter.dataSet.take(3) as ArrayList<Expense>

        this.expenseAdapter.notifyDataSetChanged()

        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
    }

    override fun sendExpenseAdapter(expenseAdapter: ExpenseAdapter, fragmentCaller: String) {
        this.expenseAdapter = expenseAdapter
        this.expenseListCaller = fragmentCaller
    }

    override fun setBudget(budget: Float) {
        val today = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        val budgetId = db.addBudget(budget, today)
        spEditor.putString(Keys.KEY_PREV_DATE.toString(), today)
        spEditor.putString(Keys.KEY_BUDGET_ID.toString(), budgetId.toString())
        spEditor.putFloat(Keys.KEY_BUDGET.toString(), budget)
        spEditor.commit()
    }

    override fun cancelBudget() {
        val today = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        var budget = 5000f
        if (sp.contains(Keys.KEY_DEFAULT_BUDGET.toString())) {
            budget = sp.getFloat(Keys.KEY_DEFAULT_BUDGET.toString(), 5000f)
        }
        val budgetId = db.addBudget(budget, today)
        spEditor.putFloat(Keys.KEY_BUDGET.toString(), budget)
        spEditor.putString(Keys.KEY_BUDGET_ID.toString(), budgetId.toString())
        spEditor.putString(Keys.KEY_PREV_DATE.toString(), today)
        spEditor.commit()
    }

}