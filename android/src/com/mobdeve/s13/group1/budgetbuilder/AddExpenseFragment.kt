package com.mobdeve.s13.group1.budgetbuilder
import android.app.Activity
import android.app.Dialog
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_add_expense.view.*

class AddExpenseFragment: DialogFragment() {

    private lateinit var mainActivity: MainActivity

    fun Spinner.avoidDropdownFocus() {
        try {
            val listPopup = Spinner::class.java
                .getDeclaredField("mPopup")
                .apply { isAccessible = true }
                .get(this)
            Toast.makeText(activity?.applicationContext, "LISTPOPUP", Toast.LENGTH_SHORT)
            if (listPopup is ListPopupWindow) {
                val popup = ListPopupWindow::class.java
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

        rootView.btn_add_expense.setOnClickListener {

        }

        MainActivity.initLayoutListener(this.dialog!!, this.requireActivity())
        rootView.spinner_add_expense_category?.avoidDropdownFocus()

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
            Toast.makeText(this.requireContext(), "Added", Toast.LENGTH_SHORT).show()
            dismiss()
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