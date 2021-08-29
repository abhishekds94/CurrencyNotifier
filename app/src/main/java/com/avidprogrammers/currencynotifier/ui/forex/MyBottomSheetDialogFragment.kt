package com.avidprogrammers.currencynotifier.ui.forex

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.ui.SnackbarUtil
import com.avidprogrammers.currencynotifier.ui.notification.Forex
import com.avidprogrammers.currencynotifier.ui.notification.ForexNotificationViewModel
import com.avidprogrammers.currencynotifier.ui.notification.ForexNotificationViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.forex_bottom_sheet.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class MyBottomSheetDialogFragment : BottomSheetDialogFragment(),KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: ForexViewModelFactory by instance()
    private var enteredVal = ""

    private val notifViewModelFactory:ForexNotificationViewModelFactory by instance()
    private lateinit var viewModel: ForexViewModel
    private lateinit var notificationViewModel:ForexNotificationViewModel

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
        notificationViewModel=ViewModelProvider(requireParentFragment(),notifViewModelFactory).get(ForexNotificationViewModel::class.java)
        return inflater.inflate(R.layout.forex_bottom_sheet, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textInput()
        notificationViewModel.inProgress.observe(viewLifecycleOwner){
            it?.let {
                if(it){
                    bt_setNotification.visibility=View.GONE
                    progress_not.visibility=View.VISIBLE

                    //For testing one time notification
                   /* val periodicWorkRequest= PeriodicWorkRequestBuilder<NotificationWorker>(15,
                        TimeUnit.MINUTES).build()

                    WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                        "forexxnotification",
                        ExistingPeriodicWorkPolicy.KEEP,
                        periodicWorkRequest
                    )*/

                }else{
                    bt_setNotification.visibility=View.VISIBLE
                    progress_not.visibility=View.GONE
                }
            }
        }

        firebaseAnalytics = Firebase.analytics

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT, "bottomsheet opened")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

        bt_setNotification.setOnClickListener {

            if (valueOne.text.isNotEmpty() && valueTwo.text.isNotEmpty() && valueThree.text.isNotEmpty() && valueFour.text.isNotEmpty() && valueFive.text.isNotEmpty()){
                enteredVal = when {

                    valueOne.text.toString()=="0" && valueTwo.text.toString()=="0" -> {
                        valueThree.text.toString() + "." + valueFour.text.toString() + valueFive.text.toString()
                    }

                    valueOne.text.toString()=="0" -> {
                        valueTwo.text.toString() + valueThree.text.toString() + "." + valueFour.text.toString() + valueFive.text.toString()
                    }

                    valueFive.text.toString()=="0" -> {
                        valueOne.text.toString() + valueTwo.text.toString() + valueThree.text.toString() + "." + valueFour.text.toString()
                    }

                    else -> {
                        valueOne.text.toString() + valueTwo.text.toString() + valueThree.text.toString() + "." + valueFour.text.toString() + valueFive.text.toString()
                    }
                }
                val f= viewModel.forex.value

                f?.let {
                    if(it.currencyCode!=null&&it.currencyVal!=null){
                    //For Date
                    val localDate: LocalDate = LocalDate.now()
                    val formatter: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("dd LLLL yyyy")
                    val formattedString: String = localDate.format(formatter)

                    //For Time
                    val currentTime = System.currentTimeMillis()
                    val simpleDateFormat = SimpleDateFormat("hh:mm:ss")
                    val date = Date(currentTime)
                    val time: String = simpleDateFormat.format(date)

                    notificationViewModel.insertForex(Forex(null,it.currencyCode,it.currencyVal,enteredVal,"In Progress",
                        "$formattedString $time",System.currentTimeMillis()))

                    SnackbarUtil.showActionSnack(requireActivity(),
                        "Alert created with a value: $enteredVal"
                    )

                    firebaseAnalytics = Firebase.analytics

                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.CONTENT, "entered val: $enteredVal")
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
                }}
                dismiss()

            } else if (valueOne.text.isEmpty()){
                valueOne.requestFocus()
                SnackbarUtil.showErrorSnackBottomSheet(view, "Enter value in first blank!")
            } else if (valueTwo.text.isEmpty()){
                valueTwo.requestFocus()
                SnackbarUtil.showErrorSnackBottomSheet(view, "Enter value in second blank!")
            } else if (valueThree.text.isEmpty()){
                valueThree.requestFocus()
                SnackbarUtil.showErrorSnackBottomSheet(view, "Enter value in third blank!")
            } else if (valueFour.text.isEmpty()){
                valueFour.requestFocus()
                SnackbarUtil.showErrorSnackBottomSheet(view, "Enter value in fourth blank!")
            } else if (valueFive.text.isEmpty()){
                valueFive.requestFocus()
                SnackbarUtil.showErrorSnackBottomSheet(view, "Enter value in fifth blank!")
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