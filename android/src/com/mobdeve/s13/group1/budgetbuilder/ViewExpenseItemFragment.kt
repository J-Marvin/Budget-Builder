package com.mobdeve.s13.group1.budgetbuilder

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_view_expense_item.view.*


class ViewExpenseItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_view_expense_item, container, false)

        rootView.btn_viewexpense_edit.setOnClickListener {
            var desc = requireArguments().getString(Keys.KEY_VIEW_EXPENSE_DESC.toString())
            var amount = requireArguments().getFloat(Keys.KEY_VIEW_EXPENSE_AMOUNT.toString(), 0f)
            var categoryType = requireArguments().getString(Keys.KEY_VIEW_EXPENSE_TYPE.toString())

            var dialog = EditExpenseDialogFragment.newInstance(amount, categoryType!!, desc!!)
            dialog.show(this.childFragmentManager, "editExpense_tag")
        }

        rootView.btn_viewexpense_delete.setOnClickListener {
            Navigation.findNavController(rootView).navigate(R.id.action_global_deleteExpenseDialogFragment)
        }

        rootView.btn_viewexpense_back.setOnClickListener {
           Navigation.findNavController(rootView).popBackStack()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.tv_viewexpense_desc.text = requireArguments().getString(Keys.KEY_VIEW_EXPENSE_DESC.toString())
        view.tv_viewexpense_amount.text = requireArguments().getFloat(Keys.KEY_VIEW_EXPENSE_AMOUNT.toString(), 0f).toString()
        view.tv_viewexpense_item_date.text = requireArguments().getString(Keys.KEY_VIEW_EXPENSE_DATE.toString())

        var fullCategoryType = requireArguments().getString(Keys.KEY_VIEW_EXPENSE_TYPE.toString())
        var enumCategory = ExpenseType.valueOf(fullCategoryType!!.uppercase())
        view.tv_viewexpense_type.text = enumCategory.textType
        view.iv_viewexpense_item_pic.setImageResource(enumCategory.iconImg)
        view.iv_viewexpense_item_pic.setColorFilter(Color.parseColor(enumCategory.iconColor))
        view.iv_viewexpense_item_pic.backgroundTintList = ColorStateList.valueOf(Color.parseColor(enumCategory.backColor))
    }

}