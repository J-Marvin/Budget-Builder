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
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mobdeve.s13.group1.budgetbuilder.dao.FurnitureDAOImpl
import com.mobdeve.s13.group1.budgetbuilder.dao.RoomDAOImpl

/**
 * This function handles the room application
 */
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

        //check if parent is save room fragment
        var saveFragment = this.parentFragmentManager.findFragmentById(R.id.fcv_save_room)

        if(saveFragment != null) {
            roomId = sp.getString(Keys.KEY_OLD_ROOM_ID.toString(), "")!!
        }
        else {
            roomId = sp.getString(Keys.KEY_ROOM_ID.toString(), "")!!
        }

//        roomId = if (arguments != null) {
//            arguments?.getString(Keys.KEY_ROOM_ID.toString())!!
//        } else {
//            sp.getString(Keys.KEY_ROOM_ID.toString(), "")!!
//        }

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

    /**
     * This function implements the screenshot feature of save room
     * @param path the path to where the screenshot will be saved
     */
    fun saveScreenshot(path: String) {
        roomApplication.exporting = true
        roomApplication.path = path
        Gdx.graphics.requestRendering()
    }

    companion object{
        /**
         * This function creates a new instance of RoomFragment given the roomId
         * @param roomId the current roomId
         * @return this fragment
         */
        fun newInstance(roomId: String): RoomFragment{
            val args = Bundle()

            args.putString(Keys.KEY_ROOM_ID.toString(), roomId)
            val fragment = RoomFragment()
            fragment.arguments = args
            return fragment
        }
    }

}