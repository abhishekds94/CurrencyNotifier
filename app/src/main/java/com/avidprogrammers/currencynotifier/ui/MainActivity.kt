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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var appOpenManager: AppOpenManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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