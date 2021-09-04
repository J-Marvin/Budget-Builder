package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseDAOImpl
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseModel
import kotlinx.android.synthetic.main.fragment_add_expense.view.*
import kotlinx.android.synthetic.main.fragment_edit_expense_dialog.*
import kotlinx.android.synthetic.main.fragment_edit_expense_dialog.view.*
import kotlinx.android.synthetic.main.fragment_edit_expense_dialog.view.btn_cancel_edit_expense
import java.util.*

class EditExpenseDialogFragment : DialogFragment() {
    lateinit var db : ExpenseDAOImpl
    lateinit var updateExpenseHandler: UpdateExpenseHandler

    companion object {
        fun newInstance(amount: Float, categoryType: String, desc: String, sentexpenseId: String, sentdate: String): EditExpenseDialogFragment{
            val args = Bundle()
            args.putFloat(Keys.KEY_EDIT_EXPENSE_AMOUNT.name, amount)
            args.putString(Keys.KEY_EDIT_EXPENSE_TYPE.name, categoryType)
            args.putString(Keys.KEY_EDIT_EXPENSE_DESC.name, desc)
            args.putString(Keys.KEY_EDIT_EXPENSE_ID.name, sentexpenseId)
            args.putString(Keys.KEY_EDIT_EXPENSE_DATE.name, sentdate)

            val dialog = EditExpenseDialogFragment()
            dialog.arguments = args
            return dialog
        }
    }

    // Source: https://gist.github.com/kakajika/a236ba721a5c0ad3c1446e16a7423a63
    fun Spinner.avoidDropdownFocus() {
        try {
            val isAppCompat = this is androidx.appcompat.widget.AppCompatSpinner
            val spinnerClass = if (isAppCompat) androidx.appcompat.widget.AppCompatSpinner::class.java else Spinner::class.java
            val popupWindowClass = if (isAppCompat) androidx.appcompat.widget.ListPopupWindow::class.java else android.widget.ListPopupWindow::class.java

            val listPopup = spinnerClass
                .getDeclaredField("mPopup")
                .apply { isAccessible = true }
                .get(this)
            if (popupWindowClass.isInstance(listPopup)) {
                val popup = popupWindowClass
                    .getDeclaredField("mPopup")
                    .apply { isAccessible = true }
                    .get(listPopup)
                if (popup is PopupWindow) {
                    popup.isFocusable = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = ExpenseDAOImpl(context)
        updateExpenseHandler = parentFragment as UpdateExpenseHandler
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
        dialog?.window?.decorView?.setOnSystemUiVisibilityChangeListener {
            DialogHelper.hideSystemUI(dialog?.window!!)
        }
        mainActivity = activity as MainActivity

        DialogHelper.initLayoutListener(dialog!!, this.requireActivity())
        rootView.spinner_edit_expense.avoidDropdownFocus()

        return rootView
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.setSystemUI()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.etStr_edit_expense_desc.setText(requireArguments().getString(Keys.KEY_EDIT_EXPENSE_DESC.toString()))
        view.etNum_edit_expense_amount.setText(requireArguments().getFloat(Keys.KEY_EDIT_EXPENSE_AMOUNT.toString(), 0f).toString())
        var fullCategoryType = requireArguments().getString(Keys.KEY_EDIT_EXPENSE_TYPE.toString())
        view.spinner_edit_expense.setSelection(ExpenseType.valueOf(fullCategoryType!!.uppercase()).ordinal)

        view.btn_edit_expense.setOnClickListener{
            var expenseDesc = etStr_edit_expense_desc.text.toString()
            var expenseAmt = etNum_edit_expense_amount.text.toString()

            if(expenseDesc.isBlank()) {
                view.etStr_edit_expense_desc.error = "Please enter description"
            }
            else if (expenseAmt.isBlank()) {
                view.etNum_edit_expense_amount.error = "Please enter amount"
            }
            else {
                val spinnerType = view.spinner_edit_expense.selectedItem.toString()
                var categoryType = spinnerType.split(" ")

                var date = Date(requireArguments().getString(Keys.KEY_EDIT_EXPENSE_DATE.name))

                var expense = ExpenseModel(date, categoryType[0], expenseAmt.toFloat(), expenseDesc)

                var sp = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)

                expense.budgetId = sp.getString(Keys.KEY_BUDGET_ID.toString(), null)
                expense.expenseId = requireArguments().getString(Keys.KEY_EDIT_EXPENSE_ID.name)

                var result = db.updateExpense(expense)

                if(result) {
                    updateExpenseHandler.updateExpenseView(expense)
                    Toast.makeText(view.context, "Edited", Toast.LENGTH_SHORT).show()
                    dismiss()
                }

            }
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