package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.s13.group1.budgetbuilder.dao.RoomDAOImpl

import kotlinx.android.synthetic.main.fragment_gallery.view.*

class GalleryFragment : Fragment() {

    private lateinit var roomDb: RoomDAOImpl

    override fun onAttach(context: Context) {
        super.onAttach(context)
        roomDb = RoomDAOImpl(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_gallery, container, false)
        initRecyclerView(view)

        view.btn_settings_back.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setExpenseListener(null)
    }

    private fun initRecyclerView(rootView:View) {
        var data = roomDb.getAllRooms()
        data.removeLast()

        rootView.rv_prev_rooms.adapter = GalleryAdapter(requireActivity().applicationContext, data)
        rootView.rv_prev_rooms.layoutManager = GridLayoutManager(this.context, 3)
    }


}