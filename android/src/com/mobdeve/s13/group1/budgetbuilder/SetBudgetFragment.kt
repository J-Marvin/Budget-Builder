package com.mobdeve.s13.group1.budgetbuilder
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_set_budget.view.*

class SetBudgetFragment: DialogFragment() {

    private lateinit var sp: SharedPreferences
    private lateinit var spEditor: SharedPreferences.Editor

    lateinit var onDismissListener: DialogInterface.OnDismissListener
    lateinit var listener: BudgetHandler

    companion object {
        fun newInstance(budget: Float, canCancel: Boolean = true): SetBudgetFragment{
            val args = Bundle()
            args.putFloat(Keys.KEY_BUDGET.toString(), budget)
            args.putBoolean(Keys.KEY_DIALOG_CANCEL.toString(), canCancel)
            val dialog = SetBudgetFragment()
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.fragment_set_budget, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        dialog?.window?.decorView?.setOnSystemUiVisibilityChangeListener {
            MainActivity.hideSystemUI(dialog?.window!!)
        }

        this.sp = PreferenceManager.getDefaultSharedPreferences(requireActivity().applicationContext)
        this.spEditor = sp.edit()
        this.requireDialog().setCanceledOnTouchOutside(requireArguments().getBoolean(Keys.KEY_DIALOG_CANCEL.toString()))
        return rootView
    }

    override fun dismiss() {
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        MainActivity.hideSystemUI(dialog?.window!!)
        super.dismiss()

        this.onDismissListener.onDismiss(dialog)
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.etNum_set_budget_amt.setText( requireArguments().getFloat(Keys.KEY_BUDGET.toString(), 5000F).toString())

        view. btn_set_budget.setOnClickListener {
            if (view.etNum_set_budget_amt.text.toString().isEmpty()) {
                view.etNum_set_budget_amt.error = "Required"
            } else if(view.etNum_set_budget_amt.text.toString().toFloatOrNull() == null) {
                view.etNum_set_budget_amt.error = "Please enter a valid number"
            } else if(view.etNum_set_budget_amt.text.toString().toFloat() < 0) {
                view.etNum_set_budget_amt.error = "Budget cannot be negative"
            } else {
                Toast.makeText(this.requireContext(), "Set", Toast.LENGTH_SHORT).show()
                val budget = view.etNum_set_budget_amt.text.toString()
                listener.setBudget(budget.toFloat())
                dismiss()
            }
        }

        view.btn_cancel_set_budget.setOnClickListener{
            listener.cancelBudget()
            dismiss()
        }

    }

    private fun hideSystemUI(){
        dialog?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }
}