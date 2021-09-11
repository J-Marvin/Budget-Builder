package com.mobdeve.s13.group1.budgetbuilder

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_new_month_dialog.view.*

class NewMonthDialogFragment: DialogFragment() {

    lateinit var listener: RoomNameHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        dialog?.window?.decorView?.setOnSystemUiVisibilityChangeListener {
            DialogHelper.hideSystemUI(dialog?.window!!)
        }

        return inflater.inflate(R.layout.fragment_new_month_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.btn_save_room_name.setOnClickListener {
            val text = view.et_room_name.text.toString()

            if (text.isEmpty()) {
                view.et_room_name.error = "Please enter room name"
            } else {
                listener.onSetRoomName(text)
                dismiss()
            }
        }
    }

}