package com.mobdeve.s13.group1.budgetbuilder

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_view_expense_item.view.*
import org.w3c.dom.Text


class ViewExpenseItemFragment : Fragment(), UpdateExpenseHandler, DialogInterface.OnDismissListener {
    lateinit var tvDesc: TextView
    lateinit var tvAmt: TextView
    lateinit var tvDate: TextView
    lateinit var tvType: TextView
    lateinit var ivPic: ImageView
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_view_expense_item, container, false)

        tvDesc = rootView.tv_viewexpense_desc
        tvAmt = rootView.tv_viewexpense_amount
        tvDate = rootView.tv_viewexpense_item_date
        tvType = rootView.tv_viewexpense_type
        ivPic = rootView.iv_viewexpense_item_pic


        rootView.btn_viewexpense_edit.setOnClickListener {
            var desc = requireArguments().getString(Keys.KEY_VIEW_EXPENSE_DESC.name)
            var amount = requireArguments().getFloat(Keys.KEY_VIEW_EXPENSE_AMOUNT.name, 0f)
            var categoryType = requireArguments().getString(Keys.KEY_VIEW_EXPENSE_TYPE.name)
            var expenseId = requireArguments().getString(Keys.KEY_VIEW_EXPENSE_ID.name)
            var date = requireArguments().getString(Keys.KEY_VIEW_EXPENSE_DATE.name)

            var dialog = EditExpenseDialogFragment.newInstance(amount, categoryType!!, desc!!, expenseId!!, date!!)
            dialog.show(this.childFragmentManager, "editExpense_tag")

        }

        rootView.btn_viewexpense_delete.setOnClickListener {
            var dialog = DeleteExpenseDialogFragment.newInstance(requireArguments().getString(Keys.KEY_VIEW_EXPENSE_ID.name)!!)
            dialog.show(this.childFragmentManager, "deleteExpense_tag")
        }

        rootView.btn_viewexpense_back.setOnClickListener {
           Navigation.findNavController(rootView).popBackStack()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvDesc.text = requireArguments().getString(Keys.KEY_VIEW_EXPENSE_DESC.toString())
        tvAmt.text = requireArguments().getFloat(Keys.KEY_VIEW_EXPENSE_AMOUNT.toString(), 0f).toString()
        tvDate.text = requireArguments().getString(Keys.KEY_VIEW_EXPENSE_DATE.toString())

        var fullCategoryType = requireArguments().getString(Keys.KEY_VIEW_EXPENSE_TYPE.toString())
        var enumCategory = ExpenseType.valueOf(fullCategoryType!!.uppercase())
        tvType.text = enumCategory.textType
        ivPic.setImageResource(enumCategory.iconImg)
        ivPic.setColorFilter(Color.parseColor(enumCategory.iconColor))
        ivPic.backgroundTintList = ColorStateList.valueOf(Color.parseColor(enumCategory.backColor))
    }

    override fun updateExpenseView(expense: Expense) {
        tvDesc.text = expense.desc
        tvAmt.text = expense.amount.toString()

        var enumCategory = ExpenseType.valueOf(expense.type.uppercase())
        tvType.text = enumCategory.textType
        ivPic.setImageResource(enumCategory.iconImg)
        ivPic.setColorFilter(Color.parseColor(enumCategory.iconColor))
        ivPic.backgroundTintList = ColorStateList.valueOf(Color.parseColor(enumCategory.backColor))

    }

    override fun onDismiss(dialog: DialogInterface?) {
        Navigation.findNavController(rootView).popBackStack()
    }

}