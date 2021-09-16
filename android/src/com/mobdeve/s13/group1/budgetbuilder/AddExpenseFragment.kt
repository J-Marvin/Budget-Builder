package com.mobdeve.s13.group1.budgetbuilder
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.mobdeve.s13.group1.budgetbuilder.dao.BudgetBuilderDbHelper
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseDAOImpl
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseModel
import kotlinx.android.synthetic.main.fragment_add_expense.view.*
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/** This class represents the Add Expense Dialog fragment*/
class AddExpenseFragment: DialogFragment() {

    private lateinit var mainActivity: MainActivity
    lateinit var db: ExpenseDAOImpl
    lateinit var sendFragmentData: SendFragmentData
    lateinit var executorService: ExecutorService
    var listener: ExpenseHandler? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = ExpenseDAOImpl(context)
        sendFragmentData = context as SendFragmentData
        executorService = Executors.newSingleThreadExecutor()
    }

    // Source: https://gist.github.com/kakajika/a236ba721a5c0ad3c1446e16a7423a63
    /** This function prevents focus to the dropdown when shown */
    private fun Spinner.avoidDropdownFocus() {
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
        val rootView: View = inflater.inflate(R.layout.fragment_add_expense, container, false)
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
        view?.etNum_add_expense_amount?.setText("")
        view?.etStr_add_expense_description?.setText("")
        view?.spinner_add_expense_category?.setSelection(0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add on click listener to add expense
        view.btn_add_expense.setOnClickListener {
            var categorySpinner = view.spinner_add_expense_category.selectedItem.toString()
            var expenseDesc = view.etStr_add_expense_description.text.toString()
            var expenseAmt = view.etNum_add_expense_amount.text.toString()

            // if expense description is blank
            if(expenseDesc.isBlank()) {
                view.etStr_add_expense_description.error = "Please enter description"
            }
            else if (expenseAmt.isBlank()) { // if amount is blank
                view.etNum_add_expense_amount.error = "Please enter amount"
            }
            else {
                //add expense to db
                val categoryType = categorySpinner.split(" ")
                executorService.run {
                    val expense = ExpenseModel(Calendar.getInstance().time, categoryType[0], expenseAmt.toFloat(), expenseDesc)

                    val sp = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)

                    expense.budgetId = sp.getString(Keys.KEY_BUDGET_ID.toString(), null)

                    val result = db.addExpense(expense)

                    if(result != -1L) {
                        //update expenseId of newly inserted object because expenseId will be null if find expense is not called for the adapter
                        //i.e. if view is not changed, recycler view will not call findExpense
                        expense.expenseId = result.toString()
                        sendFragmentData.refreshAddExpenseAdapter(expense)
                    }
                }

                listener?.onAddExpense(expenseAmt.toFloat(), categoryType[0])
                dismiss()
            }
        }

        view.btn_cancel_edit_expense.setOnClickListener{
            listener?.onCancelExpense()
            dismiss()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.setSystemUI()
    }

    /** This function hides the system UI */
    fun hideSystemUI(){
        dialog?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }
}