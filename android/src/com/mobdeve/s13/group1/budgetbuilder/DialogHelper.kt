package com.mobdeve.s13.group1.budgetbuilder

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.Window

/** This class is a helper for the layouts of dialogs*/
class DialogHelper {
    companion object{
        /** This method initializes the layout listener and hides the system UI after
         *  the soft keyboard is hidden
         *  @param dialog - the Dialog
         *  @param activity - the activity the dialog is attached to
         *  */
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

        /** This method initializes the window and hies the navigation bar
         *  @param window - the window of the dialog
         * */
        fun hideSystemUI(window: Window) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
    }
}