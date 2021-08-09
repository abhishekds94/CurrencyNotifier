package com.avidprogrammers.currencynotifier.ui.ads

import android.app.Application
import com.google.android.gms.ads.MobileAds


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(
            this
        ) { AppOpenManager(this@MyApplication) }
    }
}