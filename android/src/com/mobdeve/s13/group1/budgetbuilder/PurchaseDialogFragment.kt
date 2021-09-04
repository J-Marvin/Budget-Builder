package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mobdeve.s13.group1.budgetbuilder.dao.FurnitureDAOImpl
import com.mobdeve.s13.group1.budgetbuilder.dao.FurnitureModel
import kotlinx.android.synthetic.main.fragment_purchase_dialog.view.*
import kotlinx.android.synthetic.main.fragment_shop.*

class PurchaseDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(furnitureModel: FurnitureModel): PurchaseDialogFragment{
            val args = Bundle()
            args.putString(Keys.KEY_FURNITURE_NAME.toString(), furnitureModel.name)
            args.putInt(Keys.KEY_FURNITURE_PRICE.toString(), furnitureModel.price)
            args.putInt(Keys.KEY_FURNITURE_IMG.toString(), furnitureModel.imageId)
            args.putString(Keys.KEY_FURNITURE_TYPE.toString(), furnitureModel.type)
            args.putString(Keys.KEY_FURNITURE_ID.toString(), furnitureModel.furnitureId)

            val dialog = PurchaseDialogFragment()
            dialog.arguments = args
            return dialog
        }
    }

    private lateinit var db: FurnitureDAOImpl

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = FurnitureDAOImpl(context)
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
                var furnitureModel: FurnitureModel? = null

                if (type == "chair") {
                    index = parent.chairs.indexOf(this.arguments?.getString(Keys.KEY_FURNITURE_ID.toString(), "")?.let{ id -> FurnitureModel(furnitureId = id) })
                    furnitureModel = parent.chairs[index]
                } else if (type == "bed") {
                    index = parent.beds.indexOf(this.arguments?.getString(Keys.KEY_FURNITURE_ID.toString(), "")?.let{ id -> FurnitureModel(furnitureId = id) })
                    furnitureModel = parent.beds[index]
                }

                furnitureModel?.owned = true
                if (furnitureModel?.type == "chair")
                    parent.rv_chairs.adapter?.notifyItemChanged(index!!)
                else if(furnitureModel?.type == "bed")
                    parent.rv_beds.adapter?.notifyItemChanged(index!!)

                db.updateFurniture(furnitureModel!!)
                parent.updateBalance(parent.getBalance() - arguments?.getInt(Keys.KEY_FURNITURE_PRICE.toString())!!)
                dismiss()
            } else {
                Toast.makeText(this.requireContext(), "Not enough coins!", Toast.LENGTH_SHORT).show()
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