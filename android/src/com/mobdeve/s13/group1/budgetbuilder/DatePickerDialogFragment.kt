package com.mobdeve.s13.group1.budgetbuilder

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import java.text.DateFormatSymbols

import kotlinx.android.synthetic.main.fragment_date_picker.view.*
import java.util.*

/** This class represents the DatePicker dialog*/
class DatePickerDialogFragment : DialogFragment() {
    private var month: Int? = null // the month chosen
    private var year: Int? = null // the year chosen
    lateinit var listener: DatePickerListener // the onchange listener

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
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        return inflater.inflate(R.layout.fragment_date_picker, container, false)
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatePicker()
    }

    /** This function initializes the date picker*/
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

    fun hideSystemUI(){
        dialog?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }

    companion object {

        /** This method returns a new instance of the DialogFragment given some arguments
         *  @param month - the month (index 0)
         *  @param year - the year
         *  @return returns a new instance of the DialogFragment given some arguments
         * */
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