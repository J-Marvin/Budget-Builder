package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mobdeve.s13.group1.budgetbuilder.dao.BudgetBuilderDbHelper
import kotlinx.android.synthetic.main.fragment_reset_dialog.view.*

/**
 * This class handles the reset dialog box
 */
class ResetDialogFragment : DialogFragment() {
    private lateinit var mainActiviy: MainActivity
    lateinit var db: BudgetBuilderDbHelper
    lateinit var resetListener: ResetListener

    override fun onAttach(context: Context){
        super.onAttach(context)
        db = BudgetBuilderDbHelper.getInstance(context)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun dismiss() {
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        super.dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_reset_dialog, container, false)

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

        view.btn_reset_dialog.setOnClickListener{
            val result = db.resetDb()
            if(result) {
                dismiss()
                resetListener.onReset()
            }
        }

        view.btn_cancel_reset_dialog.setOnClickListener {
            dismiss()
        }
    }

    /**
     * This function hides the navigation bar
     */
    fun hideSystemUI(){
        dialog?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }

}