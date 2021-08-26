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
import kotlinx.android.synthetic.main.fragment_shop.view.*

class ShopFragment : Fragment(), AndroidFragmentApplication.Callbacks{
    lateinit var chairs: ArrayList<Furniture>
    lateinit var beds: ArrayList<Furniture>
    lateinit var db: BudgetBuilderDbHelper
    lateinit var furniture: ArrayList<Furniture>
    lateinit var sp: SharedPreferences
    lateinit var spEditor: SharedPreferences.Editor
    lateinit var room: RoomFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = BudgetBuilderDbHelper(context)
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
//        room = activity.roomFragment
//        activity?.supportFragmentManager?.beginTransaction()?.add(R.id.fcv_shop_room, room)?.commit()
        return rootView
    }

    private fun initRecyclerViews(rootView: View){
        initData()

        rootView.rv_chairs.adapter = FurnitureAdapter(childFragmentManager, this.chairs, activity?.applicationContext!!)
        rootView.rv_chairs.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_beds.adapter = FurnitureAdapter(childFragmentManager, this.beds, activity?.applicationContext!!)
        rootView.rv_beds.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initData() {
        this.furniture = db.findAllFurniture()
        this.chairs = ArrayList<Furniture>()
        this.beds = ArrayList<Furniture>()
        for(furniture in this.furniture) {
            if(furniture.type == "bed")
                this.beds.add(furniture)
            else if(furniture.type == "chair")
                this.chairs.add(furniture)
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
        if(!sp.contains(Keys.KEY_DEFAULT_BALANCE.toString())){
            spEditor.putInt(Keys.KEY_DEFAULT_BALANCE.toString(), 100)
            spEditor.apply()
            updateBalance(100)
        }
    }

    fun updateBalance(bal: Int){
        spEditor.putInt(Keys.KEY_BALANCE.toString(), bal)
        spEditor.apply()
        view?.tv_shop_balance?.text = bal.toString()
    }

    override fun exit() {
        TODO("Not yet implemented")
    }

}