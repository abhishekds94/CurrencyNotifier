package com.avidprogrammers.currencynotifier.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.data.forex.ForexApiService
import com.avidprogrammers.currencynotifier.data.forex.ForexSharedPrefUtil
import com.avidprogrammers.currencynotifier.data.network.ConnectivityInterceptorImpl
import com.avidprogrammers.currencynotifier.data.network.response.ForexResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import android.os.Looper
import retrofit2.Response
import java.lang.Exception


class NotificationWorker(val appContext: Context,workerParameters: WorkerParameters):
    Worker(appContext,workerParameters) {

    private val forexShared=ForexSharedPrefUtil(appContext)
    private val forexApiService:ForexApiService = ForexApiService(ConnectivityInterceptorImpl(appContext))
    private val responses:MutableList<ForexResponse>? =forexShared.getForex("forex")

    private  fun checkTheValue(id:Int,forexResponse: ForexResponse){
      val fetchedCurrentValue = forexApiService
          .getCurrentValueSync(forexResponse.currencyCode)

      val handler = Handler(Looper.getMainLooper())

      handler.postDelayed({
          Toast.makeText(appContext, forexResponse.currencyVal, Toast.LENGTH_SHORT).show()
      }, 1000)

      try {
          val response=fetchedCurrentValue.execute()
          val forexValue=response.body()
          val handler = Handler(Looper.getMainLooper())
          handler.postDelayed({
              Toast.makeText(appContext, forexValue?.currencyVal, Toast.LENGTH_SHORT).show()
          }, 1000)

          val v = String.format("%.2f",forexValue?.currencyVal?.toFloat())

          if(forexResponse.currencyVal.toFloat() >= v.toFloat()){
              showNotification(id,forexValue?.currencyCode+forexValue?.currencyVal)
              responses?.remove(forexResponse)
          }
      } catch (e:Exception){
          e.printStackTrace()
      }
    }

    private fun showNotification(id:Int,message:String){
        val channelId = "COM.AVIDPROGRAMMERS.CHANNEL"
        val builder = NotificationCompat.Builder(appContext, channelId)
            .setSmallIcon(R.drawable.ic_forex)
            .setContentTitle("My notification")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val channel = NotificationChannel(channelId, "Currency Channel", NotificationManager.IMPORTANCE_DEFAULT )

        val notificationManager: NotificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(id, builder.build())
    }

    override fun doWork(): Result {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            Toast.makeText(appContext, "Working", Toast.LENGTH_SHORT).show()
        }, 1000)

        responses?.let {
            it.forEachIndexed { index, forexResponse ->
                checkTheValue(index,forexResponse)
            }
            forexShared.setForexValues("forex", responses)
        }
        return Result.success()
    }
}