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
import android.widget.Toast
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
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class SaveRoomFragment: Fragment() {

    private var month: Int? = null
    private var year: Int? = null
    private lateinit var roomId: String
    private lateinit var sp: SharedPreferences
    private lateinit var spEditor: SharedPreferences.Editor
    private lateinit var roomDb: RoomDAOImpl
    private lateinit var furnitureDb: FurnitureDAOImpl
    private lateinit var budgetDb: BudgetDAOImpl
    private lateinit var room: RoomModel

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
            room.name = view?.et_save_room_name?.text.toString()
            roomDb.updateRoom(room)
            Navigation.findNavController(view).popBackStack()
        }
        view.btn_save_room.setOnClickListener {
            room.name = view?.et_save_room_name?.text.toString()
            roomDb.updateRoom(room)
            Navigation.findNavController(view).popBackStack()
        }
        initData()
        initRecyclerViews(view)
    }

    override fun onResume() {
        super.onResume()

        val prevRoomId = sp.getString(Keys.KEY_ROOM_ID.toString(), "")
        room = RoomModel(month!!, year!!, prevRoomId!!)
        room.path = path

        room.name = view?.et_save_room_name?.text.toString()
        Log.d("PATH", path)
        Log.d("Name", room.name!!)
        roomDb.updateRoom(room)

        var dialog = NewMonthDialogFragment()
        dialog.onDismissListener = DialogInterface.OnDismissListener {
            val fragment = childFragmentManager.findFragmentById(R.id.fcv_save_room)

            if (fragment != null) {
                (fragment as RoomFragment).saveScreenshot(path)
            } else {
                Log.d("fragment status", "null")
            }
        }
        dialog.show(requireActivity().supportFragmentManager, "setRoomName_TAG")
    }

    override fun onPause() {
        super.onPause()
        room.name = view?.et_save_room_name?.text.toString()
        roomDb.updateRoom(room)
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

                val exec = Executors.newSingleThreadScheduledExecutor()
                exec.schedule({
                    val fragment = childFragmentManager.findFragmentById(R.id.fcv_save_room)
                    if (fragment != null) {
                        (fragment as RoomFragment).saveScreenshot(path)
                    } else {
                        Log.d("fragment status", "null")
                    }
                }, 500, TimeUnit.MILLISECONDS)

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