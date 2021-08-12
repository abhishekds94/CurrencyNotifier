package com.avidprogrammers.currencynotifier.ui

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar
import android.R
import android.content.Intent
import android.provider.Settings.Global.getString
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.FragmentContainerView
import com.avidprogrammers.currencynotifier.ui.notification.NotificationActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_home.bottom_nav
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.forex_bottom_sheet.*
import kotlinx.coroutines.withContext


object SnackbarUtil {

    fun showActionSnack(activity: Activity, text: String?) {

        val bottomNavView: BottomNavigationView = activity.bottom_nav!!

        val sb = Snackbar.make(bottomNavView, text!!, Snackbar.LENGTH_LONG)
            .apply { anchorView = bottomNavView }
            .setAction("Check Status!") {
                val intent = Intent(activity, NotificationActivity::class.java)
                activity.startActivity(intent)
            }
            .setActionTextColor(Color.parseColor("#F9AA33"))
            .setCallback(object : Snackbar.Callback() {
                override fun onDismissed(snackbar: Snackbar, event: Int) {
                    super.onDismissed(snackbar, event)
                }

                override fun onShown(snackbar: Snackbar) {
                    super.onShown(snackbar)
                }
            })
        sb.view.setBackgroundColor(Color.parseColor("#005b4f"))
        sb.setTextColor(Color.parseColor("#ffffff"))
        sb.show()
    }

    fun showErrorSnackBottomSheet(rootView:View? , text: String?) {
//        val bottomNavView: BottomNavigationView = activity.bottom_nav!!
        val sb = Snackbar.make(rootView!!, text!!, Snackbar.LENGTH_LONG)
            .apply { anchorView = rootView }
        sb.view.setBackgroundColor(Color.parseColor("#ba000d"))
        sb.setTextColor(Color.parseColor("#ffffff"))
        sb.setAction(com.avidprogrammers.currencynotifier.R.string.forexValText, null)
        sb.show()
    }

    fun showErrorSnack(activity: Activity, text: String?) {
        val bottomNavView: BottomNavigationView = activity.bottom_nav!!
        val sb = Snackbar.make(bottomNavView, text!!, Snackbar.LENGTH_LONG)
            .apply { anchorView = bottomNavView }
        sb.view.setBackgroundColor(Color.parseColor("#ba000d"))
        sb.setTextColor(Color.parseColor("#ffffff"))
        sb.setAction(com.avidprogrammers.currencynotifier.R.string.forexValText, null)
        sb.show()
    }

    fun showSuccessSnack(activity: Activity, text: String?) {
        val bottomNavView: BottomNavigationView = activity.bottom_nav!!
        val sb = Snackbar.make(bottomNavView, text!!, Snackbar.LENGTH_LONG)
            .apply { anchorView = bottomNavView }
        sb.view.setBackgroundColor(Color.parseColor("#087f23"))
        sb.setTextColor(Color.parseColor("#ffffff"))
        sb.setAction(com.avidprogrammers.currencynotifier.R.string.forexValText, null)
        sb.show()
    }
}