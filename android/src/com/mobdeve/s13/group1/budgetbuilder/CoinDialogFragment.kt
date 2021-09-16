package com.mobdeve.s13.group1.budgetbuilder

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_coin_dialog.view.*

/** This class represents the earn coins DialogFragment*/
class CoinDialogFragment : DialogFragment() {
    private  var coins: Int? = null
    var onDismissListener: DialogInterface.OnDismissListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            coins = it.getInt(Keys.KEY_EARNED_COINS.toString(), 0)
        }
    }

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

        return inflater.inflate(R.layout.fragment_coin_dialog, container, false)
    }

    override fun dismiss() {
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        DialogHelper.hideSystemUI(dialog?.window!!)
        super.dismiss()

        onDismissListener?.onDismiss(dialog)
    }

    override fun onResume() {
        super.onResume()
        DialogHelper.hideSystemUI(dialog?.window!!)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.tv_earn_amount.text = coins.toString()
        view.btn_earn_okay.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        @JvmStatic
        /** Creates a new instance of the fragment given some arguments
         *  @param coins - the amount of coins earned
         *  @return returns an instance of the DialogFragment
         *  */
        fun newInstance(coins: Int) =
            CoinDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(Keys.KEY_EARNED_COINS.toString(), coins)
                }
            }
    }
}