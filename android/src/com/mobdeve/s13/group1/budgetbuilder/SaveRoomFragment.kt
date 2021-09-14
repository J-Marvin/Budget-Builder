package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s13.group1.budgetbuilder.dao.*
import kotlinx.android.synthetic.main.fragment_save_room.*
import kotlinx.android.synthetic.main.fragment_save_room.view.*
import kotlinx.android.synthetic.main.fragment_shop.view.*
import java.util.*

class SaveRoomFragment: Fragment() {

    private var month: Int? = null
    private var year: Int? = null
    private lateinit var roomId: String
    private lateinit var sp: SharedPreferences
    private lateinit var spEditor: SharedPreferences.Editor
    private lateinit var roomDb: RoomDAOImpl
    private lateinit var furnitureDb: FurnitureDAOImpl
    private lateinit var budgetDb: BudgetDAOImpl

    lateinit var chairs: ArrayList<FurnitureModel>
    lateinit var beds: ArrayList<FurnitureModel>
    lateinit var floors: ArrayList<FurnitureModel>
    lateinit var shelves: ArrayList<FurnitureModel>
    lateinit var endTables: ArrayList<FurnitureModel>
    lateinit var desks: ArrayList<FurnitureModel>
    lateinit var couches: ArrayList<FurnitureModel>
    lateinit var coffeTables: ArrayList<FurnitureModel>
    lateinit var furnitureModel: ArrayList<FurnitureModel>
    lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            month = this.getInt(Keys.KEY_MONTH.toString())
            year = this.getInt(Keys.KEY_YEAR.toString())
            roomId = this.getString(Keys.KEY_ROOM_ID.toString())!!
            path = "rooms/$month-$year.png"
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        sp = PreferenceManager.getDefaultSharedPreferences(context)
        spEditor = sp.edit()
        roomDb = RoomDAOImpl(context)
        furnitureDb = FurnitureDAOImpl(context)
        budgetDb = BudgetDAOImpl(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_save_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.et_save_room_name.setText(DataHelper.getRandomName())
        view.btn_save_back.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }
        initData()
        initRecyclerViews(view)
    }

    override fun onResume() {
        super.onResume()

        val prevRoomId = sp.getString(Keys.KEY_ROOM_ID.toString(), "")
        val prevRoom = RoomModel(month!!, year!!, prevRoomId!!)
        prevRoom.path = path

        prevRoom.name = view?.et_save_room_name?.text.toString()
        Log.d("PATH", path)
        Log.d("Name", prevRoom.name!!)
        roomDb.updateRoom(prevRoom)

        var dialog = NewMonthDialogFragment()
        dialog.onDismissListener = object: DialogInterface.OnDismissListener{
            override fun onDismiss(dialog: DialogInterface?) {
                val fragment = childFragmentManager.findFragmentById(R.id.fcv_save_room)

                if (fragment != null) {
                    (fragment as RoomFragment).saveScreenshot(path)
                } else {
                    Log.d("fragment status", "null")
                }
            }
        }
        dialog.show(requireActivity().supportFragmentManager, "setRoomName_TAG")
    }

    fun initData() {
        this.furnitureModel = furnitureDb.findOwnedFurnitureByRoom(roomId)
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

    private fun initRecyclerViews(rootView: View){
        val equipListener = object: EquipListener{
            override fun onEquip() {
                childFragmentManager.commit{
                    replace<RoomFragment>(R.id.fcv_save_room)
                }

                val fragment = childFragmentManager.findFragmentById(R.id.fcv_save_room)
                if (fragment != null) {
                    (fragment as RoomFragment).saveScreenshot(path)
                } else {
                    Log.d("fragment status", "null")
                }
            }
        }
        initData()

        rootView.rv_save_chairs.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.chairs, activity?.applicationContext!!)
        rootView.rv_save_chairs.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_save_beds.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.beds, activity?.applicationContext!!)
        rootView.rv_save_beds.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_save_floor.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.floors, activity?.applicationContext!!)
        rootView.rv_save_floor.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_save_shelf.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.shelves, activity?.applicationContext!!)
        rootView.rv_save_shelf.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_save_endtable.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.endTables, activity?.applicationContext!!)
        rootView.rv_save_endtable.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_save_desk.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.desks, activity?.applicationContext!!)
        rootView.rv_save_desk.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_save_couch.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.couches, activity?.applicationContext!!)
        rootView.rv_save_couch.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_save_coffeetable.adapter = FurnitureAdapter(equipListener, childFragmentManager, this.coffeTables, activity?.applicationContext!!)
        rootView.rv_save_coffeetable.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        if (this.endTables.size == 0) {
            rootView.rv_save_endtable.visibility = View.GONE
            rootView.tv_save_endtable.visibility = View.GONE
        }

        if (this.couches.size == 0) {
            rootView.rv_save_couch.visibility = View.GONE
            rootView.tv_save_couch.visibility = View.GONE
        }

        if (this.coffeTables.size == 0) {
            rootView.rv_save_coffeetable.visibility = View.GONE
            rootView.tv_save_coffeetable.visibility = View.GONE
        }

        if (this.shelves.size == 0) {
            rootView.rv_save_shelf.visibility = View.GONE
            rootView.tv_save_shelf.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(month: Int, year: Int, roomId: String): SaveRoomFragment{
            val args = Bundle()
            args.putInt(Keys.KEY_MONTH.toString(), month)
            args.putInt(Keys.KEY_YEAR.toString(), year)
            args.putString(Keys.KEY_ROOM_ID.toString(), roomId)
            val fragment = SaveRoomFragment()
            fragment.arguments = args
            return fragment
        }
    }
}