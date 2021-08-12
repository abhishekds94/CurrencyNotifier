package com.avidprogrammers.currencynotifier.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.data.forex.ForexValue
import com.avidprogrammers.currencynotifier.ui.ads.AppOpenManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import android.R.id




class MainActivity : AppCompatActivity() {

    var appOpenManager: AppOpenManager? = null
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAnalytics = Firebase.analytics

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "splash")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        appOpenManager = AppOpenManager(applicationContext as ForexValue)

        privacyPolicy.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse("https://currency-notifier.flycricket.io/privacy.html")
            startActivity(browserIntent)
            finish()
        }

        terms.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse("https://currency-notifier.flycricket.io/terms.html")
            startActivity(browserIntent)
            finish()
        }

        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            appOpenManager!!.showAdIfAvailable()
        }, 3000)
    }
}