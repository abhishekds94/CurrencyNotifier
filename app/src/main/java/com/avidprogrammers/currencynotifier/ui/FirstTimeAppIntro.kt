package com.avidprogrammers.currencynotifier.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.ui.ads.AppOpenManager
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroCustomLayoutFragment
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import kotlinx.android.synthetic.main.app_intro_one.*

class FirstTimeAppIntro: AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        setTransformer(AppIntroPageTransformerType.Parallax(
            titleParallaxFactor = 1.0,
            imageParallaxFactor = -1.0,
            descriptionParallaxFactor = 2.0
        ))

        isVibrate = true
        vibrateDuration = 50L

        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.app_intro_one))
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.app_intro_two))
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.app_intro_three))

    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        val openIntent = Intent(this, HomeActivity::class.java)
        startActivity(openIntent)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
//        appOpenManager!!.showAdIfAvailable()
        val openIntent = Intent(this, HomeActivity::class.java)
        startActivity(openIntent)
        finish()
    }
}