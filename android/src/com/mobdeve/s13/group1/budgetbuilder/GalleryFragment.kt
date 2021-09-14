package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.GdxRuntimeException
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
        val onSelectListener = object: OnSelectListener{
            override fun onSelect(name: String, path: String) {
                rootView.tv_gallery_name.text = name
                try {
                    val file = Gdx.files.local(path)
                    val ims = file.read()
                    rootView.iv_gallery_room.setImageDrawable(Drawable.createFromStream(ims, null))
                    ims.close()
                } catch (ex: GdxRuntimeException){

                }
            }

        }
        var data = roomDb.getPreviousRooms()

        if (data.size > 0) {
            rootView.tv_no_rooms.visibility = View.GONE
            rootView.rv_prev_rooms.adapter = GalleryAdapter(onSelectListener, requireActivity().applicationContext, data)
            rootView.rv_prev_rooms.layoutManager = GridLayoutManager(this.context, 3)
            rootView.tv_gallery_name.text = data[0].name
            try {
                val file = Gdx.files.local(data[0].path!!)
                val ims = file.read()
                rootView.iv_gallery_room.setImageDrawable(Drawable.createFromStream(ims, null))
                ims.close()
            } catch (ex: GdxRuntimeException){

            }
        } else {
            rootView.tv_no_rooms.visibility = View.VISIBLE
            rootView.tv_gallery_name.visibility = View.GONE
            rootView.iv_gallery_room.visibility = View.GONE
        }
    }


}