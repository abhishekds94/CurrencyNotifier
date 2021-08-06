package com.avidprogrammers.currencynotifier.ui.forex

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.data.forex.ForexSharedPrefUtil
import com.avidprogrammers.currencynotifier.data.network.response.ForexResponse
import com.avidprogrammers.currencynotifier.ui.notification.NotificationWorker
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.forex_bottom_sheet.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class MyBottomSheetDialogFragment : BottomSheetDialogFragment(),KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: ForexViewModelFactory by instance()
    private var enteredVal = ""

    private lateinit var viewModel: ForexViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.behavior.skipCollapsed = true
            dialog.behavior.state = STATE_EXPANDED
        }

        return dialog
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        viewModel = ViewModelProvider(requireParentFragment(), viewModelFactory)
            .get(ForexViewModel::class.java)
        return inflater.inflate(R.layout.forex_bottom_sheet, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textInput()

        bt_setNotification.setOnClickListener {

            if (valueOne.text.isNotEmpty() && valueTwo.text.isNotEmpty() && valueThree.text.isNotEmpty() && valueFour.text.isNotEmpty() && valueFive.text.isNotEmpty()){
                enteredVal = when {

                    valueOne.text.toString()=="0" && valueTwo.text.toString()=="0" -> {
                        valueThree.text.toString() + "." + valueFour.text.toString() + valueFive.text.toString()
                    }

                    valueOne.text.toString()=="0" -> {
                        valueTwo.text.toString() + valueThree.text.toString() + "." + valueFour.text.toString() + valueFive.text.toString()
                    }

                    valueTwo.text.toString()=="0" -> {
                        valueThree.text.toString() + "." + valueFour.text.toString() + valueFive.text.toString()
                    }


                    valueFive.text.toString()=="0" -> {
                        valueOne.text.toString() + valueTwo.text.toString() + valueThree.text.toString() + "." + valueFour.text.toString()
                    }

                    else -> {
                        valueOne.text.toString() + valueTwo.text.toString() + valueThree.text.toString() + "." + valueFour.text.toString() + valueFive.text.toString()
                    }
                }
                Toast.makeText(context, "entered val:::$enteredVal", Toast.LENGTH_SHORT).show()
                val code = viewModel.forex.value?.currencyCode

                code?.let {
                    Toast.makeText(context, "code val:::$it", Toast.LENGTH_SHORT).show()
                    val sharedPrefUtil=ForexSharedPrefUtil(requireContext())
                    sharedPrefUtil.setForex("forex", ForexResponse(it,enteredVal))
                }

            } else if (valueOne.text.isEmpty()){
                valueOne.requestFocus()
                Toast.makeText(context, "enter val 1", Toast.LENGTH_SHORT).show()
            } else if (valueTwo.text.isEmpty()){
                valueTwo.requestFocus()
                Toast.makeText(context, "enter val 2", Toast.LENGTH_SHORT).show()
            } else if (valueThree.text.isEmpty()){
                valueThree.requestFocus()
                Toast.makeText(context, "enter val 3", Toast.LENGTH_SHORT).show()
            } else if (valueFour.text.isEmpty()){
                valueFour.requestFocus()
                Toast.makeText(context, "enter val 4", Toast.LENGTH_SHORT).show()
            } else if (valueFive.text.isEmpty()){
                valueFive.requestFocus()
                Toast.makeText(context, "enter val 5", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun textInput() {

        val valueOne = requireView().findViewById(R.id.valueOne) as EditText
        val valueTwo = requireView().findViewById(R.id.valueTwo) as EditText
        val valueThree = requireView().findViewById(R.id.valueThree) as EditText
        val valueFour = requireView().findViewById(R.id.valueFour) as EditText
        val valueFive = requireView().findViewById(R.id.valueFive) as EditText

        valueOne.addTextChangedListener(GenericTextWatcher(valueOne, valueTwo))
        valueTwo.addTextChangedListener(GenericTextWatcher(valueTwo, valueThree))
        valueThree.addTextChangedListener(GenericTextWatcher(valueThree, valueFour))
        valueFour.addTextChangedListener(GenericTextWatcher(valueFour, valueFive))
        valueFive.addTextChangedListener(GenericTextWatcher(valueFive, null))

        valueOne.setOnKeyListener(GenericKeyEvent(valueOne, null))
        valueTwo.setOnKeyListener(GenericKeyEvent(valueTwo, valueOne))
        valueThree.setOnKeyListener(GenericKeyEvent(valueThree, valueTwo))
        valueFour.setOnKeyListener(GenericKeyEvent(valueFour,valueThree))
        valueFive.setOnKeyListener(GenericKeyEvent(valueFive,valueFour))
    }

    class GenericKeyEvent internal constructor(private val currentView: EditText, private val previousView: EditText?) : View.OnKeyListener{
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if(event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.valueOne && currentView.text.isEmpty()) {
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }


    }

    class GenericTextWatcher internal constructor(private val currentView: View, private val nextView: View?) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            return
        }

        override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
            val text = editable.toString()
            when (currentView.id) {
                R.id.valueOne -> if (text.length == 1) nextView!!.requestFocus()
                R.id.valueTwo -> if (text.length == 1) nextView!!.requestFocus()
                R.id.valueThree -> if (text.length == 1) nextView!!.requestFocus()
                R.id.valueFour -> if (text.length == 1) nextView!!.requestFocus()
            }
        }
    }

}