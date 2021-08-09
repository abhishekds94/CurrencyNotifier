package com.avidprogrammers.currencynotifier.ui.ads

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import java.util.*


class AppOpenManager(myApplication: MyApplication) : LifecycleObserver,
    ActivityLifecycleCallbacks {
    private val myApplication: MyApplication = myApplication
    private var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAdLoadCallback? = null
    private var currentActivity: Activity? = null
    private var loadTime: Long = 0
    fun showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable) {
            Log.d(LOG_TAG, "Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {

                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        fetchAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {}
                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }
                }
            appOpenAd!!.setFullScreenContentCallback(fullScreenContentCallback)
            appOpenAd!!.show(currentActivity)
        } else {
            Log.d(LOG_TAG, "Can not show ad.")
            fetchAd()
        }
    }

    /**
     * LifecycleObserver methods
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        showAdIfAvailable()
        Log.d(LOG_TAG, "onStart")
    }

    /**
     * Request an ad
     */
    fun fetchAd() {
        // We will implement this below.
        if (isAdAvailable) {
            return
        }
        loadCallback = object : AppOpenAdLoadCallback() {
            // TODO: Replaced ----------------------------------------------------------
            /**
             * Called when an app open ad has loaded.
             *
             * @param appOpenAd the loaded app open ad.
             */
            /*   @Override
            public void onAppOpenAdLoaded(AppOpenAd ad) {
                AppOpenManager.this.appOpenAd = ad;
                AppOpenManager.this.loadTime = (new Date()).getTime();
            }*/
            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                super.onAdLoaded(appOpenAd)
                this@AppOpenManager.appOpenAd = appOpenAd
                loadTime = Date().time
                Log.d(LOG_TAG, "App-Open Ad Loaded")
            }
            //--------------------------------------------------------------------------
            // TODO: Replaced ---------------------------------------------------------------
            /**
             * Called when an app open ad has failed to load.
             *
             * @param loadAdError the error.
             */
            /*  @Override
            public void onAppOpenAdFailedToLoad(LoadAdError loadAdError) {
            }*/
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                // Handle the error.
            } //--------------------------------------------------------------------------------
        }
        val request = adRequest
        AppOpenAd.load(
            myApplication,
            AD_UNIT_ID,
            request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            loadCallback!!
        )
    }

    /**
     * Creates and returns ad request.
     */
    private val adRequest: AdRequest
        get() = AdRequest.Builder().build()

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    val isAdAvailable: Boolean
        get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo()

    /**
     * Utility method to check if ad was loaded more than n hours ago.
     */
    private fun wasLoadTimeLessThanNHoursAgo(): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * 4.toLong()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    companion object {
        private const val LOG_TAG = "AppOpenManager"
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294"
        private var isShowingAd = false
    }

    /**
     * Constructor
     */
    init {
        this.myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
}