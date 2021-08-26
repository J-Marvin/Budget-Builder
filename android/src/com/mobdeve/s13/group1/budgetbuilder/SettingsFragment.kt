package com.mobdeve.s13.group1.budgetbuilder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        rootView.btn_settings_back.setOnClickListener {
            Navigation.findNavController(rootView).popBackStack()
        }

        rootView.btn_settings_reset.setOnClickListener {
            Navigation.findNavController(rootView).navigate(R.id.action_settingsFragment_to_resetDialogFragment)
        }

        return rootView
    }
}