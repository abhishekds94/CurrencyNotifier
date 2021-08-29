package com.avidprogrammers.currencynotifier.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.data.forex.ForexApiService
import com.avidprogrammers.currencynotifier.data.forex.ForexSharedPrefUtil
import com.avidprogrammers.currencynotifier.data.network.ConnectivityInterceptorImpl
import com.avidprogrammers.currencynotifier.data.network.response.ForexResponse
import com.avidprogrammers.currencynotifier.ui.MainActivity
import com.avidprogrammers.currencynotifier.ui.forex.firebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class NotificationWorker(val appContext: Context,workerParameters: WorkerParameters):
    CoroutineWorker(appContext,workerParameters),KodeinAware {

    override val kodein by closestKodein{appContext}
    private val forexShared=ForexSharedPrefUtil(appContext)
    private val forexApiService:ForexApiService = ForexApiService(ConnectivityInterceptorImpl(appContext))
    private val responses:MutableList<ForexResponse>? =forexShared.getForex("forex")
    private val repository:ForexNotificationRepository by instance()

    private suspend fun checkTheValue(id:Int,forexResponse: Forex){
      withContext(Dispatchers.IO){
      val fetchedCurrentValue = forexApiService
          .getCurrentValueSync(forexResponse.currencyCode!!)

      try {
          val response=fetchedCurrentValue.execute()
          val forexValue=response.body()
          val handler = Handler(Looper.getMainLooper())


          val v = String.format("%.2f",forexValue?.currencyVal?.toFloat())

          if(forexResponse.targetVal!!.toFloat() >= v.toFloat()){
              showNotification(id,"Forex pair " + forexValue?.currencyCode + " value is now " +v)
              forexResponse.notificationStatus="Completed"
              repository.updateNotification(forexResponse)
             // responses?.remove(forexResponse)
          }
      } catch (e:Exception){
          e.printStackTrace()
      }}
    }

    private fun showNotification(id:Int, message:String){
        val channelId = "COM.AVIDPROGRAMMERS.CHANNEL"
        val builder = NotificationCompat.Builder(appContext, channelId)
            .setSmallIcon(R.drawable.bell)
            .setContentTitle("Selected Forex pair has an update!")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val resultIntent = Intent(appContext, MainActivity::class.java)
// Create the TaskStackBuilder
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(appContext).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        firebaseAnalytics = Firebase.analytics

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT, "notification shown")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

        builder.setContentIntent(resultPendingIntent)
        val channel = NotificationChannel(channelId, "Currency Channel", NotificationManager.IMPORTANCE_DEFAULT )

        val notificationManager: NotificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(id, builder.build())
    }

    override suspend fun doWork(): Result {
        val handler = Handler(Looper.getMainLooper())
/*        handler.postDelayed({
            Toast.makeText(appContext, "Working", Toast.LENGTH_SHORT).show()
        }, 1000)*/
        return withContext(Dispatchers.IO){

            val notification=repository.getInProgressNotification("In Progress")
            notification?.let {
                checkTheValue(0, it)
            }
         /* responses?.let {
                it.forEachIndexed { index, forexResponse ->
                    checkTheValue(index,forexResponse)
                }
                forexShared.setForexValues("forex", responses)
            }*/
             Result.success()
        }

    }
}