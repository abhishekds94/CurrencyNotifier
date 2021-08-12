package com.avidprogrammers.currencynotifier.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.ui.notification.NotificationWorker
import kotlinx.android.synthetic.main.activity_home.*
import java.time.Duration
import java.util.concurrent.TimeUnit
import androidx.work.OneTimeWorkRequestBuilder as OneTimeWorkRequestBuilder

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        //setup bottom nav bar
        bottom_nav.setupWithNavController(navController)

        bottom_nav.itemIconTintList = null

        val periodicWorkRequest= PeriodicWorkRequestBuilder<NotificationWorker>(60, TimeUnit.MINUTES).build()

//        For Onetime notification testing
//        val oneTimeWorkRequest= OneTimeWorkRequest.Builder(NotificationWorker::class.java).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "forexnotification",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
//        For Onetime notification testing
//        val workManager = WorkManager.getInstance(this)
//        workManager.enqueue(oneTimeWorkRequest)

    }
}