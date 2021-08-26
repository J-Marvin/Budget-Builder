package com.mobdeve.s13.group1.budgetbuilder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidFragmentApplication

class RoomFragment(): AndroidFragmentApplication() {
    private lateinit var roomApplication : RoomApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val config = AndroidApplicationConfiguration()
        roomApplication = RoomApplication()
        return initializeForView(roomApplication)
    }

}