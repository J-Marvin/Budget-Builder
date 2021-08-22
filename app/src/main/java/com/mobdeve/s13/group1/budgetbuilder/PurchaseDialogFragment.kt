package com.mobdeve.s13.group1.budgetbuilder

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
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
            args.putString(Keys.KEY_FURNITURE_TYPE.toString(), furniture.type)
            args.putString(Keys.KEY_FURNITURE_ID.toString(), furniture.furnitureId)

            val dialog = PurchaseDialogFragment()
            dialog.arguments = args
            return dialog
        }
    }

    override fun dismiss() {
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        super.dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.fragment_purchase_dialog, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

        return rootView
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.iv_purchase_furniture.setImageResource(requireArguments().getInt(Keys.KEY_FURNITURE_IMG.toString(), 0))
        view.tv_purchase_name.text = requireArguments().getString(Keys.KEY_FURNITURE_NAME.toString())
        view.tv_purchase_price.text = requireArguments().getInt(Keys.KEY_FURNITURE_PRICE.toString()).toString()

        view.btn_purchase_confirm.setOnClickListener {
            val parent: ShopFragment = parentFragment as ShopFragment

            if(parent.getBalance() >= arguments?.getInt(Keys.KEY_FURNITURE_PRICE.toString())!!) {
                val type = arguments?.getString(Keys.KEY_FURNITURE_TYPE.toString())

                var index: Int? = null
                var furniture: Furniture? = null

                if (type == "chair") {
                    index = parent.chairs.indexOf(this.arguments?.getString(Keys.KEY_FURNITURE_ID.toString(), "")?.let{ id -> Furniture(furnitureId = id)})
                    furniture = parent.chairs[index]
                } else if (type == "bed") {
                    index = parent.beds.indexOf(this.arguments?.getString(Keys.KEY_FURNITURE_ID.toString(), "")?.let{ id -> Furniture(furnitureId = id)})
                    furniture = parent.beds[index]
                }

                furniture?.owned = true
                if (furniture?.type == "chair")
                    parent.rv_chairs.adapter?.notifyItemChanged(index!!)
                else if(furniture?.type == "bed")
                    parent.rv_beds.adapter?.notifyItemChanged(index!!)

                parent.db.updateFurniture(furniture!!)
                parent.updateBalance(parent.getBalance() - arguments?.getInt(Keys.KEY_FURNITURE_PRICE.toString())!!)
                dismiss()
            } else {
                Log.d("SLAPSOIL", "NOT ENOUGH COINS DUMBASS")
                Toast.makeText(this.requireContext(), "Not enough coins!", Toast.LENGTH_LONG).show()
            }
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