package com.avidprogrammers.currencynotifier.ui.notification

import androidx.lifecycle.LiveData
import com.avidprogrammers.currencynotifier.db.ForexValueDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForexNotificationRepository (private val ForexValueDao: ForexValueDao) {

    suspend fun insertForex(forex:Forex){
        withContext(Dispatchers.IO){
            ForexValueDao.insert(forex)
        }
    }

    suspend fun deleteForex(forex:Forex){
        withContext(Dispatchers.IO){
            ForexValueDao.delete(forex)
        }
    }
    suspend fun deleteAllForex(){
        withContext(Dispatchers.IO){
            ForexValueDao.deleteAll()
        }
    }

    fun getNotifications():LiveData<List<Forex>>{
        return ForexValueDao.getNotifications()
    }

    suspend fun getNotification(notificationStatus:String): List<Forex> {
        return withContext(Dispatchers.IO){ ForexValueDao.getNotification(notificationStatus)}
    }
    suspend fun getInProgressNotification(notificationStatus:String): Forex? {
        return withContext(Dispatchers.IO){ ForexValueDao.getInProgressNotification(notificationStatus)}
    }

    suspend fun updateNotification(forex: Forex){
        withContext(Dispatchers.IO){
            ForexValueDao.updateNotification(forex)
        }
    }

    suspend fun updateNotifications(forexs:List<Forex>){
        withContext(Dispatchers.Main){
            ForexValueDao.updateNotifications(forexs)
        }
    }
    fun getForexNotifications():List<Forex>{
        return ForexValueDao.getForexNotifications()
    }

}