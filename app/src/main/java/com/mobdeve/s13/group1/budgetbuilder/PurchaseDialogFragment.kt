package com.mobdeve.s13.group1.budgetbuilder

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_purchase_dialog.view.*
import kotlinx.android.synthetic.main.fragment_shop.*

class PurchaseDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(furniture: Furniture): PurchaseDialogFragment{
            val args = Bundle()
            args.putString(Keys.KEY_FURNITURE_NAME.toString(), furniture.name)
            args.putInt(Keys.KEY_FURNITURE_PRICE.toString(), furniture.price)
            args.putInt(Keys.KEY_FURNITURE_IMG.toString(), furniture.imageId)
            args.putString(Keys.KEY_FURNITURE_ID.toString(), furniture.furnitureId)

            val dialog = PurchaseDialogFragment()
            dialog.arguments = args
            return dialog
        }
    }

    override fun onStart() {
        super.onStart()
        hideSystemUI()
    }

    override fun onPause() {
        super.onPause()
        hideSystemUI()
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        hideSystemUI()
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.fragment_purchase_dialog, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.iv_purchase_furniture.setImageResource(requireArguments().getInt(Keys.KEY_FURNITURE_IMG.toString(), 0))
        view.tv_purchase_name.text = requireArguments().getString(Keys.KEY_FURNITURE_NAME.toString())
        view.tv_purchase_price.text = requireArguments().getInt(Keys.KEY_FURNITURE_PRICE.toString()).toString()

        view.btn_purchase_confirm.setOnClickListener {
            val parent: ShopFragment = parentFragment as ShopFragment

            val index = parent.furniture.indexOf(this.arguments?.getString(Keys.KEY_FURNITURE_ID.toString(), "")?.let { it1 -> Furniture(furnitureId = it1) })
            val furniture = parent.furniture[index]
            furniture.owned = true
            if (furniture.type == "chair")
                parent.rv_chairs.adapter?.notifyItemChanged(index)
            else if(furniture.type == "bed")
                parent.rv_beds.adapter?.notifyItemChanged(index)

            parent.db.updateFurniture(furniture)
            dismiss()
        }

        view.btn_purchase_cancel.setOnClickListener{
            dismiss()
        }
    }

    fun hideSystemUI(){
        dialog?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }

}