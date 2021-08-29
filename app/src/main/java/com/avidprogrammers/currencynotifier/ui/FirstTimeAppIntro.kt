package com.avidprogrammers.currencynotifier.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.data.forex.ForexValue
import com.avidprogrammers.currencynotifier.ui.ads.AppOpenManager
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroCustomLayoutFragment
import com.github.appintro.AppIntroPageTransformerType

class FirstTimeAppIntro: AppIntro() {

    var appOpenManager: AppOpenManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTransformer(AppIntroPageTransformerType.Parallax(
            titleParallaxFactor = 1.0,
            imageParallaxFactor = -1.0,
            descriptionParallaxFactor = 2.0
        ))

        isVibrate = true
        vibrateDuration = 50L

        appOpenManager = AppOpenManager(applicationContext as ForexValue)
        appOpenManager!!.fetchAd()

        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.app_intro_one))
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.app_intro_two))
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.app_intro_three))

    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        val openIntent = Intent(this, HomeActivity::class.java)
        startActivity(openIntent)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        appOpenManager!!.showAdIfAvailable()
        val openIntent = Intent(this, HomeActivity::class.java)
        startActivity(openIntent)
        finish()
    }
}