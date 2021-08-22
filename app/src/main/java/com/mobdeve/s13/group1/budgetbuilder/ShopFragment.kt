package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_shop.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopFragment : Fragment() {
    //Auto filled IDK what this is hehe
//    TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    lateinit var chairs: ArrayList<Furniture>
    lateinit var beds: ArrayList<Furniture>
    lateinit var db: BudgetBuilderDbHelper
    lateinit var furniture: ArrayList<Furniture>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Auto filled IDK what this is hehe
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = BudgetBuilderDbHelper(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_shop, container, false)
        initRecyclerViews(rootView)
        return rootView
    }

    //Auto filled IDK what this is hehe
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ShopFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ShopFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }

    private fun initRecyclerViews(rootView: View){
        initData()

        rootView.rv_chairs.adapter = FurnitureAdapter(childFragmentManager, this.chairs, activity?.applicationContext!!)
        rootView.rv_chairs.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        rootView.rv_beds.adapter = FurnitureAdapter(childFragmentManager, this.beds, activity?.applicationContext!!)
        rootView.rv_beds.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initData() {
        this.furniture = db.findAllFurniture()
        this.chairs = ArrayList<Furniture>()
        this.beds = ArrayList<Furniture>()
        for(furniture in this.furniture) {
            if(furniture.type == "bed")
                this.beds.add(furniture)
            else if(furniture.type == "chair")
                this.chairs.add(furniture)
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    override fun onPause() {
        super.onPause()
        hideSystemUI()
    }
    private fun hideSystemUI(){

        activity?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }

    fun purchaseItem(){

    }

}