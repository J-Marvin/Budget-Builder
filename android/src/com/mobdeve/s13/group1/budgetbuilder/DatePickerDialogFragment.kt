package com.mobdeve.s13.group1.budgetbuilder

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import java.text.DateFormatSymbols

import kotlinx.android.synthetic.main.fragment_date_picker.view.*
import java.util.*

class DatePickerDialogFragment : DialogFragment() {
    private var month: Int? = null
    private var year: Int? = null
    lateinit var listener: DatePickerListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            month = it.getInt(Keys.KEY_MONTH.toString())
            year = it.getInt(Keys.KEY_YEAR.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_date_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatePicker()
    }

    private fun initDatePicker() {
        var months = DateFormatSymbols.getInstance().months
        this.view?.let { view ->
            view.np_date_month.maxValue = 11
            view.np_date_month.minValue = 0
            view.np_date_month.value = month!!
            view.np_date_month.displayedValues = months
            view.np_date_month.wrapSelectorWheel = false

            view.np_date_year.maxValue = Calendar.getInstance().get(Calendar.YEAR)
            view.np_date_year.minValue = 2010
            view.np_date_year.value = year!!
            view.np_date_year.wrapSelectorWheel = false

            view.btn_date_select.setOnClickListener {
                listener.onSelectDate(view.np_date_month.value, view.np_date_year.value)
                dismiss()
            }

            view.btn_date_cancel.setOnClickListener {
                listener.onCancelDate()
                dismiss()
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(month: Int, year: Int) =
            DatePickerDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(Keys.KEY_MONTH.toString(), month)
                    putInt(Keys.KEY_YEAR.toString(), year)
                }
            }
    }
}