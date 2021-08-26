package com.mobdeve.s13.group1.budgetbuilder

import android.app.Activity
import android.app.Dialog
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Spinner
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks {

    private lateinit var db: BudgetBuilderDbHelper
    private lateinit var sp: SharedPreferences
    public lateinit var roomFragment: RoomFragment

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
//        applicationContext.deleteDatabase("BudgetBuilder.db")
        db = BudgetBuilderDbHelper(this)
        initNavBar()
        this.sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)

       roomFragment = RoomFragment()
    }

    override fun onResume() {
        super.onResume()
        setSystemUI()
        initNavBar()
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

}