package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mobdeve.s13.group1.budgetbuilder.dao.FurnitureDAOImpl

class RoomFragment: AndroidFragmentApplication() {
    private lateinit var roomApplication : RoomApplication
    lateinit var db: FurnitureDAOImpl

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = FurnitureDAOImpl(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val config = AndroidApplicationConfiguration()

        //TODO: roomID to actual roomID
        roomApplication = RoomApplication(db.findEquippedFurnitureByRoom(""))

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


}