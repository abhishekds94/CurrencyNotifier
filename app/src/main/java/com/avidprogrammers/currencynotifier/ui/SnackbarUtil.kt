package com.avidprogrammers.currencynotifier.ui

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar
import android.R
import android.provider.Settings.Global.getString
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor


object SnackbarUtil {
    /************************************ ShowSnackbar with message, KeepItDisplayedOnScreen for few seconds */
    fun showSnakbarTypeOne(activity: Activity, mMessage: String?) {

        val bottomNavView: BottomNavigationView = activity.bottom_nav!!

        Snackbar.make(bottomNavView, mMessage!!, Snackbar.LENGTH_LONG).apply {
            anchorView = bottomNavView
        }
            .setAction("Action", null)
            .show()
    }

    /************************************ ShowSnackbar with message, KeepItDisplayedOnScreen */
    fun showSnakbarTypeTwo(activity: Activity, mMessage: String?) {

        val bottomNavView: BottomNavigationView = activity.bottom_nav!!

        Snackbar.make(bottomNavView, mMessage!!, Snackbar.LENGTH_INDEFINITE).apply {
            anchorView = bottomNavView
        }
            .setAction("Action", null)
            .show()
    }


    /************************************ ShowSnackbar without message, KeepItDisplayedOnScreen, OnClickOfOk restrat the activity */
    fun showSnakbarTypeThree(rootView: View?, activity: Activity) {

        val bottomNavView: BottomNavigationView = activity.bottom_nav!!

        Snackbar
            .make(rootView!!, "NoInternetConnectivity", Snackbar.LENGTH_INDEFINITE)
            .apply {
                anchorView = bottomNavView
            }
            .setAction("TryAgain") {
                val intent = activity.intent
                activity.finish()
                activity.startActivity(intent)
            }
            .setActionTextColor(Color.CYAN)
            .setCallback(object : Snackbar.Callback() {
                override fun onDismissed(snackbar: Snackbar, event: Int) {
                    super.onDismissed(snackbar, event)
                }

                override fun onShown(snackbar: Snackbar) {
                    super.onShown(snackbar)
                }
            })
            .show()
    }

    /************************************ ShowSnackbar with message, KeepItDisplayedOnScreen, OnClickOfOk restart the activity */
    fun showSnakbarTypeFour(rootView: View?, activity: Activity, mMessage: String?) {

        val bottomNavView: BottomNavigationView = activity.bottom_nav!!

        Snackbar
            .make(rootView!!, mMessage!!, Snackbar.LENGTH_INDEFINITE).apply {
                anchorView = bottomNavView
            }
            .setAction("TryAgain") {
                val intent = activity.intent
                activity.finish()
                activity.startActivity(intent)
            }
            .setActionTextColor(Color.CYAN)
            .setCallback(object : Snackbar.Callback() {
                override fun onDismissed(snackbar: Snackbar, event: Int) {
                    super.onDismissed(snackbar, event)
                }

                override fun onShown(snackbar: Snackbar) {
                    super.onShown(snackbar)
                }
            })
            .show()
    }

    fun showSnack(activity: Activity, text: String?) {

        val bottomNavView: BottomNavigationView = activity.bottom_nav!!

        val sb = Snackbar.make(
            bottomNavView,  //findViewById(R.id.activity_main),
            text!!,  //R.string.permission_denied_explanation,
            Snackbar.LENGTH_LONG
        ).apply {
            anchorView = bottomNavView
        }
        sb.view.setBackgroundColor(Color.parseColor("#1eb2a6"))
        sb.setTextColor(Color.parseColor("#000000"))
        sb.setAction(com.avidprogrammers.currencynotifier.R.string.forexValText, null)
        sb.show()

    }

    fun showSnackLong(activity: Activity, text: String?) {

        val bottomNavView: BottomNavigationView = activity.bottom_nav!!

        val sb = Snackbar.make(
            bottomNavView,  //findViewById(R.id.activity_main),
            text!!,  //R.string.permission_denied_explanation,
            Snackbar.LENGTH_LONG
        ).apply {
            anchorView = bottomNavView
        }
        sb.view.setBackgroundColor(Color.parseColor("#1eb2a6"))
        sb.setTextColor(Color.parseColor("#000000"))
        sb.setAction(com.avidprogrammers.currencynotifier.R.string.forexValText, null)
        sb.show()
    }
}