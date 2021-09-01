package com.mobdeve.s13.group1.budgetbuilder
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.ListPopupWindow
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_add_expense.view.*
import java.util.*


class AddExpenseFragment: DialogFragment() {

    private lateinit var mainActivity: MainActivity
    lateinit var db: BudgetBuilderDbHelper
    lateinit var sendFragmentData: SendFragmentData

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = BudgetBuilderDbHelper(context)
        sendFragmentData = context as SendFragmentData
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.fragment_add_expense, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

        val activity = requireActivity() as MainActivity
        mainActivity = activity

        rootView.etNum_add_expense_amount.setOnFocusChangeListener { v, hasFocus ->
            dialog?.window?.decorView?.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }

        DialogHelper.initLayoutListener(this.dialog!!, this.requireActivity())
        rootView.spinner_add_expense_category.avoidDropdownFocus()

        return rootView
    }

    override fun dismiss() {
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        super.dismiss()
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view. btn_add_expense.setOnClickListener {
            var categorySpinner = view.spinner_add_expense_category.selectedItem.toString()
            var expenseDesc = view.etStr_add_expense_description.text.toString()
            var expenseAmt = view.etNum_add_expense_amount.text.toString()

            if(expenseDesc.isBlank()) {
                view.etStr_add_expense_description.error = "Please enter description"
            }
            else if (expenseAmt.isBlank()) {
                view.etNum_add_expense_amount.error = "Please enter amount"
            }
            else {
                //add expense to db
                var categoryType = categorySpinner.split(" ")
                var expense = Expense(Calendar.getInstance().time, categoryType[0], expenseAmt.toFloat(), expenseDesc)

                var result = db.addExpense(expense)

                if(result != -1L)
                    sendFragmentData.refreshExpenseAdapter(expense)

                dismiss()
            }

        }

        view.btn_cancel_edit_expense.setOnClickListener{
            dismiss()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.setSystemUI()
    }

    fun hideSystemUI(){
        dialog?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }
}