package com.mobdeve.s13.group1.budgetbuilder

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {
    lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        sp = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)

        rootView.btn_see_all.setOnClickListener{
           Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_expenseFragment)
        }

        rootView.iv_home_room.setOnClickListener{
            Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_galleryFragment)
        }

        rootView.view_set_budget.setOnClickListener{
            Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_setBudgetFragment)
        }

        updateBalance(rootView)

        return rootView
    }

    private fun updateBalance(rootView: View){
        val bal = sp.getInt(Keys.KEY_BALANCE.toString(), 100)
        rootView.tv_coin_balance.text = bal.toString()
    }


}