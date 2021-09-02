package com.mobdeve.s13.group1.budgetbuilder

import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks, SendFragmentData {

    private lateinit var db: BudgetBuilderDbHelper
    private lateinit var sp: SharedPreferences
    private lateinit var spEditor: SharedPreferences.Editor
    lateinit var roomFragment: RoomFragment
    lateinit var expenseAdapter: ExpenseAdapter
    lateinit var expenseListCaller: String

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        applicationContext.deleteDatabase("BudgetBuilder.db")
        db = BudgetBuilderDbHelper(this)
        initNavBar()
        this.sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        this.spEditor = this.sp.edit()

//        this.spEditor.clear()
//        this.spEditor.apply()

       roomFragment = RoomFragment()
    }

    override fun onResume() {
        super.onResume()
        setSystemUI()
        initNavBar()
        initPreferences()
    }

    fun setSystemUI() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        window.decorView.setOnSystemUiVisibilityChangeListener {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
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

    override fun refreshAddExpenseAdapter(expense: Expense) {
        this.expenseAdapter.dataSet.add(0, expense)
        if(expenseListCaller.equals("HomeFragment", true))
            this.expenseAdapter.dataSet.removeLast()

        this.expenseAdapter.notifyDataSetChanged()

        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
    }

    override fun sendExpenseAdapter(expenseAdapter: ExpenseAdapter, fragmentCaller: String) {
        this.expenseAdapter = expenseAdapter
        this.expenseListCaller = fragmentCaller
    }

    private fun initPreferences() {
        if (!sp.contains(Keys.KEY_CURRENCY.toString())) {
            spEditor.putString(Keys.KEY_CURRENCY.toString(), "$")
        }
    }
}