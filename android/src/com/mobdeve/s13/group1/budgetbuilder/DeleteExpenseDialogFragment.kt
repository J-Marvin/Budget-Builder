package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseDAOImpl
import kotlinx.android.synthetic.main.fragment_delete_expense_dialog.view.*

class DeleteExpenseDialogFragment : DialogFragment() {
    lateinit var db: ExpenseDAOImpl
    var buttonPressed: String? = null

    companion object {
        fun newInstance(rowId: String): DeleteExpenseDialogFragment{
            val args = Bundle()
            args.putString(Keys.KEY_VIEW_EXPENSE_ID.name, rowId)

            val dialog = DeleteExpenseDialogFragment()
            dialog.arguments = args
            return dialog
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = ExpenseDAOImpl(context)
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
        var rootView = inflater.inflate(R.layout.fragment_delete_expense_dialog, container, false)

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

        view.btn_date_select.setOnClickListener{
            val result = db.deleteExpense(requireArguments().getString(Keys.KEY_VIEW_EXPENSE_ID.name)!!)

            if(result) {
                Toast.makeText(view.context, "Deleted", Toast.LENGTH_SHORT).show()
                buttonPressed = "delete"
                dismiss()
            }
        }

        view.btn_date_cancel.setOnClickListener {
            buttonPressed = "cancel"
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(buttonPressed == "delete")
            (parentFragment as DialogInterface.OnDismissListener).onDismiss(dialog)
    }
}