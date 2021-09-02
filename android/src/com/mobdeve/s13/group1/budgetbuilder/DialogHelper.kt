package com.mobdeve.s13.group1.budgetbuilder

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.Window

class DialogHelper {
    companion object{
        fun initLayoutListener(dialog: Dialog, activity: Activity) {
            val activityRootView = dialog.window?.decorView?.findViewById<View>(android.R.id.content)
            activityRootView?.viewTreeObserver?.addOnGlobalLayoutListener {
                val heightDiff = activity.window?.decorView?.findViewById<View>(android.R.id.content)?.rootView?.height?.minus(activityRootView.height)

                if (heightDiff != null) {
                    if (heightDiff > 100) {
                        hideSystemUI(dialog.window!!)
                    }
                }
            }
        }

        fun hideSystemUI(window: Window) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
    }
}