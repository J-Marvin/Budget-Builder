package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.mobdeve.s13.group1.budgetbuilder.dao.FurnitureDAOImpl
import com.mobdeve.s13.group1.budgetbuilder.dao.FurnitureModel
import kotlinx.android.synthetic.main.fragment_shop.view.*

class ShopFragment : Fragment(), AndroidFragmentApplication.Callbacks{
    lateinit var chairs: ArrayList<FurnitureModel>
    lateinit var beds: ArrayList<FurnitureModel>
    lateinit var floors: ArrayList<FurnitureModel>
    lateinit var shelves: ArrayList<FurnitureModel>
    lateinit var endTables: ArrayList<FurnitureModel>
    lateinit var desks: ArrayList<FurnitureModel>
    lateinit var couches: ArrayList<FurnitureModel>
    lateinit var coffeTables: ArrayList<FurnitureModel>

    lateinit var db: FurnitureDAOImpl
    lateinit var furnitureModel: ArrayList<FurnitureModel>
    lateinit var sp: SharedPreferences
    lateinit var spEditor: SharedPreferences.Editor
    var balance: Int? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = FurnitureDAOImpl(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_shop, container, false)
        initRecyclerViews(rootView)
        sp = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
        spEditor = sp.edit()
        initBalance()

        rootView.tv_shop_balance.text = getBalance().toString()

        rootView.btn_shop_settings.setOnClickListener {
            Navigation.findNavController(rootView).navigate(R.id.action_global_settingsFragment)
        }

        val activity = activity as MainActivity
        return rootView
    }

    private fun initRecyclerViews(rootView: View){
        initData()

        rootView.rv_chairs.adapter = FurnitureAdapter(childFragmentManager, this.chairs, activity?.applicationContext!!)
        rootView.rv_chairs.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_beds.adapter = FurnitureAdapter(childFragmentManager, this.beds, activity?.applicationContext!!)
        rootView.rv_beds.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_floor.adapter = FurnitureAdapter(childFragmentManager, this.floors, activity?.applicationContext!!)
        rootView.rv_floor.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_shelf.adapter = FurnitureAdapter(childFragmentManager, this.shelves, activity?.applicationContext!!)
        rootView.rv_shelf.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_endtable.adapter = FurnitureAdapter(childFragmentManager, this.endTables, activity?.applicationContext!!)
        rootView.rv_endtable.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_desk.adapter = FurnitureAdapter(childFragmentManager, this.desks, activity?.applicationContext!!)
        rootView.rv_desk.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_couch.adapter = FurnitureAdapter(childFragmentManager, this.couches, activity?.applicationContext!!)
        rootView.rv_couch.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_coffeetable.adapter = FurnitureAdapter(childFragmentManager, this.coffeTables, activity?.applicationContext!!)
        rootView.rv_coffeetable.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initData() {
        this.furnitureModel = db.findAllFurniture()
        this.chairs = ArrayList()
        this.beds = ArrayList()
        this.floors = ArrayList()
        this.shelves = ArrayList()
        this.endTables = ArrayList()
        this.desks = ArrayList()
        this.couches = ArrayList()
        this.coffeTables = ArrayList()

        for(furniture in this.furnitureModel) {
            when(furniture.type) {
                "bed" -> this.beds.add(furniture)
                "chair" -> this.chairs.add(furniture)
                "floor" -> this.floors.add(furniture)
                "shelf" -> this.shelves.add(furniture)
                "endtable" -> this.endTables.add(furniture)
                "desk" -> this.desks.add(furniture)
                "couch" -> this.couches.add(furniture)
                "coffeetable" -> this.coffeTables.add(furniture)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    override fun onPause() {
        super.onPause()
        hideSystemUI()
    }

    override fun onDetach() {
        super.onDetach()
        hideSystemUI()
    }


    private fun hideSystemUI(){

        activity?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }

    fun getBalance(): Int{
        return sp.getInt(Keys.KEY_BALANCE.toString(), 100)
    }

    private fun initBalance() {
        if (sp.contains(Keys.KEY_BALANCE.toString())) {
            balance = sp.getInt(Keys.KEY_BALANCE.toString(), 20)
        } else {
            balance = 20
            spEditor.putInt(Keys.KEY_BALANCE.toString(), 20)
            spEditor.commit()
        }
    }

    fun updateBalance(bal: Int){
        spEditor.putInt(Keys.KEY_BALANCE.toString(), bal)
        spEditor.apply()
        view?.tv_shop_balance?.text = bal.toString()
    }

    override fun exit() {
    }

}