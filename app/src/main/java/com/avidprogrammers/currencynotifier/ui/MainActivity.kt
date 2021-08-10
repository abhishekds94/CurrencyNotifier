package com.avidprogrammers.currencynotifier.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.data.forex.ForexValue
import com.avidprogrammers.currencynotifier.ui.ads.AppOpenManager

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

        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            appOpenManager!!.showAdIfAvailable()
        }, 3000)
    }
}