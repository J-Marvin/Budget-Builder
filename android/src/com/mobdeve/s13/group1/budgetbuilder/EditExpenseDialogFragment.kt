package com.mobdeve.s13.group1.budgetbuilder

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_edit_expense_dialog.view.*

class EditExpenseDialogFragment : DialogFragment() {
    companion object {
        fun newInstance(amount: Float, categoryType: String, desc: String): EditExpenseDialogFragment{
            val args = Bundle()
            args.putFloat(Keys.KEY_EDIT_EXPENSE_AMOUNT.toString(), amount)
            args.putString(Keys.KEY_EDIT_EXPENSE_TYPE.toString(), categoryType)
            args.putString(Keys.KEY_EDIT_EXPENSE_DESC.toString(), desc)

            val dialog = EditExpenseDialogFragment()
            dialog.arguments = args
            return dialog
        }
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
        var rootView: View = inflater.inflate(R.layout.fragment_edit_expense_dialog, container, false)

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
        view.etStr_edit_expense_desc.setText(requireArguments().getString(Keys.KEY_EDIT_EXPENSE_DESC.toString()))
        view.etNum_edit_expense_amount.setText(requireArguments().getFloat(Keys.KEY_EDIT_EXPENSE_AMOUNT.toString(), 0f).toString())
        var fullCategoryType = requireArguments().getString(Keys.KEY_EDIT_EXPENSE_TYPE.toString())
        view.spinner_edit_expense.setSelection(ExpenseType.valueOf(fullCategoryType!!.uppercase()).ordinal)

        view.btn_edit_expense.setOnClickListener{
            Toast.makeText(view.context, "Edited", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        view.btn_cancel_edit_expense.setOnClickListener {
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