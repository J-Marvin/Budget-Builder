package com.mobdeve.s13.group1.budgetbuilder

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.mobdeve.s13.group1.budgetbuilder.dao.BudgetDAOImpl
import com.mobdeve.s13.group1.budgetbuilder.dao.BudgetModel
import com.mobdeve.s13.group1.budgetbuilder.dao.RoomDAOImpl
import kotlinx.android.synthetic.main.fragment_set_budget.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import java.util.*


class SettingsFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var sp: SharedPreferences
    private lateinit var spEditor: SharedPreferences.Editor
    private lateinit var roomDb: RoomDAOImpl
    private lateinit var budgetDb: BudgetDAOImpl

    // Source: https://gist.github.com/kakajika/a236ba721a5c0ad3c1446e16a7423a63
    fun Spinner.avoidDropdownFocus() {
        try {
            val isAppCompat = this is androidx.appcompat.widget.AppCompatSpinner
            val spinnerClass = if (isAppCompat) androidx.appcompat.widget.AppCompatSpinner::class.java else Spinner::class.java
            val popupWindowClass = if (isAppCompat) androidx.appcompat.widget.ListPopupWindow::class.java else android.widget.ListPopupWindow::class.java

            val listPopup = spinnerClass
                .getDeclaredField("mPopup")
                .apply { isAccessible = true }
                .get(this)
            if (popupWindowClass.isInstance(listPopup)) {
                val popup = popupWindowClass
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.roomDb = RoomDAOImpl(context)
        this.sp = PreferenceManager.getDefaultSharedPreferences(context)
        this.spEditor = this.sp.edit()
        this.budgetDb = BudgetDAOImpl(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        rootView.spinner_settings_currency.avoidDropdownFocus()

        rootView.btn_settings_back.setOnClickListener {
            Navigation.findNavController(rootView).popBackStack()
        }

        rootView.btn_settings_reset.setOnClickListener {
//            Navigation.findNavController(rootView).navigate(R.id.action_settingsFragment_to_resetDialogFragment)

            var fragment = ResetDialogFragment()
            fragment.resetListener = object: ResetListener{
                override fun onReset() {
                    resetSettings()
                }
            }

            fragment.show(requireActivity().supportFragmentManager, "reset_TAG")
        }


        initSpinner(rootView)
        initBudget(rootView)

        return rootView
    }

    private fun resetSettings() {

        val today = Calendar.getInstance()
        val roomId: String
        val budget: Float
        val balance: Int

        var room = roomDb.initRoomFurniture(today.get(Calendar.MONTH), today.get(Calendar.YEAR))
        roomId = room.toString()

        var budgetId = budgetDb.addBudget(
            BudgetModel(
            5000f, FormatHelper.dateFormatterNoTime.format(today.time))
        )

        budget = 5000f
        balance = 20
        spEditor.clear()
        spEditor.putFloat(Keys.KEY_BUDGET.toString(), budget)
        spEditor.putInt(Keys.KEY_BALANCE.toString(), balance)
        spEditor.putString(Keys.KEY_ROOM_ID.toString(), roomId)
        spEditor.putString(Keys.KEY_BUDGET_ID.toString(), budgetId.toString())
        spEditor.putString(Keys.KEY_PREV_DATE.toString(), FormatHelper.dateFormatterNoTime.format(today.time))
        spEditor.commit()
    }

    private fun initSpinner(rootView: View) {
        if (sp.getString(Keys.KEY_CURRENCY.toString(), "$").equals("₱")) {
            rootView.spinner_settings_currency.setSelection(0)
        } else {
            rootView.spinner_settings_currency.setSelection(1)
        }
        rootView.spinner_settings_currency.onItemSelectedListener = this
    }

    private fun initBudget(rootView: View) {
        if (sp.contains(Keys.KEY_DEFAULT_BUDGET.toString())) {
            rootView.etNum_settings_budget.setText(sp.getFloat(Keys.KEY_DEFAULT_BUDGET.toString(), 5000f).toString())
        }

        rootView.etNum_settings_budget.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus()
                val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

                imm.hideSoftInputFromWindow(v.windowToken, 0)
                return@setOnEditorActionListener true
            }
            false
        }
        rootView.etNum_settings_budget.onFocusChangeListener =
            OnFocusChangeListener { v, hasFocus ->
                val et = v as EditText
                if (!hasFocus) {
                    if (et.text.toString().isEmpty()) {
                        spEditor.apply{
                            this.remove(Keys.KEY_DEFAULT_BUDGET.toString())
                            this.apply()
                        }
                    } else if(et.text.toString().toFloatOrNull() == null) {
                        et.error = "Please enter valid number"
                    } else if(et.text.toString().toFloat() < 0) {
                        et.error = "Please enter positive number"
                    } else {
                        val budget = et.text.toString().toFloat()
                        spEditor.putFloat(Keys.KEY_DEFAULT_BUDGET.toString(), String.format("%.2f", budget).toFloat())
                        spEditor.commit()
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setExpenseListener(null)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position == 0) {
            spEditor.putString(Keys.KEY_CURRENCY.toString(), "₱")
        } else {
            spEditor.putString(Keys.KEY_CURRENCY.toString(), "$")
        }

        spEditor.commit()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}