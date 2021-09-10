package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
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
import java.io.IOException

class PurchaseDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(furnitureModel: FurnitureModel): PurchaseDialogFragment{
            val args = Bundle()
            args.putString(Keys.KEY_FURNITURE_NAME.toString(), furnitureModel.name)
            args.putInt(Keys.KEY_FURNITURE_PRICE.toString(), furnitureModel.price)
            args.putInt(Keys.KEY_FURNITURE_IMG.toString(), furnitureModel.imageId)
            args.putString(Keys.KEY_FURNITURE_TYPE.toString(), furnitureModel.type)
            args.putString(Keys.KEY_FURNITURE_ID.toString(), furnitureModel.furnitureId)
            args.putString(Keys.KEY_FURNITURE_PATH.toString(), furnitureModel.path)

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
        try {
            val ims = requireContext().assets.open(requireArguments().getString(Keys.KEY_FURNITURE_PATH.toString(), null))
            view.iv_purchase_furniture.setImageDrawable(Drawable.createFromStream(ims, null))
            ims.close()
        } catch (ex: IOException) {
//            view.iv_purchase_furniture.setImageResource(requireArguments().getInt(Keys.KEY_FURNITURE_IMG.toString(), 0))
        }
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
                } else if (type == "floor") {
                    index = parent.floors.indexOf(this.arguments?.getString(Keys.KEY_FURNITURE_ID.toString(), "")?.let{ id -> FurnitureModel(furnitureId = id) })
                    furnitureModel = parent.floors[index]
                } else if (type == "endtable") {
                    index = parent.endTables.indexOf(this.arguments?.getString(Keys.KEY_FURNITURE_ID.toString(), "")?.let{ id -> FurnitureModel(furnitureId = id) })
                    furnitureModel = parent.endTables[index]
                } else if (type == "shelf") {
                    index = parent.shelves.indexOf(this.arguments?.getString(Keys.KEY_FURNITURE_ID.toString(), "")?.let{ id -> FurnitureModel(furnitureId = id) })
                    furnitureModel = parent.shelves[index]
                } else if (type == "desk") {
                    index = parent.desks.indexOf(this.arguments?.getString(Keys.KEY_FURNITURE_ID.toString(), "")?.let{ id -> FurnitureModel(furnitureId = id) })
                    furnitureModel = parent.desks[index]
                } else if (type == "couch") {
                    index = parent.couches.indexOf(this.arguments?.getString(Keys.KEY_FURNITURE_ID.toString(), "")?.let{ id -> FurnitureModel(furnitureId = id) })
                    furnitureModel = parent.couches[index]
                } else if (type == "coffeetable") {
                    index = parent.coffeTables.indexOf(this.arguments?.getString(Keys.KEY_FURNITURE_ID.toString(), "")?.let{ id -> FurnitureModel(furnitureId = id) })
                    furnitureModel = parent.coffeTables[index]
                }

                furnitureModel?.owned = true
                if (furnitureModel?.type == "chair")
                    parent.rv_chairs.adapter?.notifyItemChanged(index!!)
                else if(furnitureModel?.type == "bed")
                    parent.rv_beds.adapter?.notifyItemChanged(index!!)
                else if(furnitureModel?.type == "floor")
                    parent.rv_floor.adapter?.notifyItemChanged(index!!)
                else if(furnitureModel?.type == "endtable")
                    parent.rv_endtable.adapter?.notifyItemChanged(index!!)
                else if(furnitureModel?.type == "shelf")
                    parent.rv_shelf.adapter?.notifyItemChanged(index!!)
                else if(furnitureModel?.type == "desk")
                    parent.rv_desk.adapter?.notifyItemChanged(index!!)
                else if(furnitureModel?.type == "couch")
                    parent.rv_couch.adapter?.notifyItemChanged(index!!)
                else if(furnitureModel?.type == "coffeetable")
                    parent.rv_coffeetable.adapter?.notifyItemChanged(index!!)

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