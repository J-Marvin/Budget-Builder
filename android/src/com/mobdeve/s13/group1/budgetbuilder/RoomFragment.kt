package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mobdeve.s13.group1.budgetbuilder.dao.FurnitureDAOImpl
import com.mobdeve.s13.group1.budgetbuilder.dao.RoomDAOImpl

class RoomFragment: AndroidFragmentApplication() {
    private lateinit var roomApplication : RoomApplication
    lateinit var db: FurnitureDAOImpl
    lateinit var roomDb: RoomDAOImpl
    lateinit var roomId: String
    lateinit var sp: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = FurnitureDAOImpl(context)
        roomDb = RoomDAOImpl(context)
        sp = PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val config = AndroidApplicationConfiguration()

        //TODO: roomID to actual roomID
        roomId = if (arguments != null) {
            arguments?.getString(Keys.KEY_ROOM_ID.toString())!!
        } else {
            sp.getString(Keys.KEY_ROOM_ID.toString(), "")!!
        }
        Log.d("ROOM ID", roomId)

        roomApplication = RoomApplication(db.findEquippedFurnitureByRoom(roomId))

        //set transparent bg
        config.r = 8
        config.g = 8
        config.b = 8
        config.a = 8
        config.useGL30 = false

        val roomView = initializeForView(roomApplication, config)
        val glView = graphics.view as SurfaceView
        glView.holder.setFormat(PixelFormat.TRANSLUCENT)
        glView.setZOrderOnTop(true)

        return roomView
    }

    fun saveScreenshot(path: String) {
//        Toast.makeText(requireActivity().applicationContext, "REACHED SCREENSHOT", Toast.LENGTH_SHORT).show()
        roomApplication.saveScreenshot(path)
    }

    companion object{
        fun newInstance(roomId: String): RoomFragment{
            val args = Bundle()

            args.putString(Keys.KEY_ROOM_ID.toString(), roomId)
            val fragment = RoomFragment()
            fragment.arguments = args
            return fragment
        }
    }

}