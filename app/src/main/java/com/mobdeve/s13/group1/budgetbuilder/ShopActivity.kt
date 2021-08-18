package com.mobdeve.s13.group1.budgetbuilder

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_shop.*

class ShopActivity : AppCompatActivity() {

    lateinit var chairs: ArrayList<Furniture>
    lateinit var beds: ArrayList<Furniture>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        initChairRecyclerView()
        initBedRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setSystemUI()
    }

    private fun initChairRecyclerView(){
        this.chairs = DataHelper.getChairs()
        rv_chairs.adapter = FurnitureAdapter(this.chairs)
        rv_chairs.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initBedRecyclerView(){
        this.beds = DataHelper.getBeds()
        rv_beds.adapter = FurnitureAdapter(this.beds)
        rv_beds.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    fun setSystemUI() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }
}