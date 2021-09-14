package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
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
    lateinit var roomId: String
    var balance: Int? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = FurnitureDAOImpl(context)
        sp = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
        spEditor = sp.edit()

        roomId = sp.getString(Keys.KEY_ROOM_ID.toString(), "")!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_shop, container, false)
        initRecyclerViews(rootView)

        initBalance()

        rootView.tv_shop_balance.text = getBalance().toString()

        rootView.btn_shop_settings.setOnClickListener {
            Navigation.findNavController(rootView).navigate(R.id.action_global_settingsFragment)
        }
        return rootView
    }

    private fun initRecyclerViews(rootView: View){
        val equipListener = object: EquipListener{
            override fun onEquip() {
                childFragmentManager.commit{
                    replace<RoomFragment>(R.id.fcv_shop_room)
                }
            }
        }
        initData()

        rootView.rv_chairs.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.chairs, activity?.applicationContext!!)
        rootView.rv_chairs.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_beds.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.beds, activity?.applicationContext!!)
        rootView.rv_beds.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_floor.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.floors, activity?.applicationContext!!)
        rootView.rv_floor.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_shelf.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.shelves, activity?.applicationContext!!)
        rootView.rv_shelf.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_endtable.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.endTables, activity?.applicationContext!!)
        rootView.rv_endtable.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_desk.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.desks, activity?.applicationContext!!)
        rootView.rv_desk.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_couch.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.couches, activity?.applicationContext!!)
        rootView.rv_couch.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_coffeetable.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.coffeTables, activity?.applicationContext!!)
        rootView.rv_coffeetable.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initData() {

        this.furnitureModel = db.findAllFurnitureByRoom(roomId)
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
        (requireActivity() as MainActivity).setExpenseListener(null)
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