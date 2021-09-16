package com.mobdeve.s13.group1.budgetbuilder

import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_new_month_dialog.view.*

/**
 * This class handles the view shown after each month
 */
class NewMonthDialogFragment: DialogFragment() {

    var onDismissListener: DialogInterface.OnDismissListener? = null
    var name = ""

    override fun onResume() {
        super.onResume()
        val percent = 85.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        dialog?.window?.decorView?.setOnSystemUiVisibilityChangeListener {
            DialogHelper.hideSystemUI(dialog?.window!!)
        }

        return inflater.inflate(R.layout.fragment_new_month_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.btn_new_month_okay.setOnClickListener {
            dismiss()
        }
    }

    override fun dismiss() {
        super.dismiss()
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        onDismissListener?.onDismiss(dialog)
    }

}