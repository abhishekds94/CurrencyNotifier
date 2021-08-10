package com.avidprogrammers.currencynotifier.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.avidprogrammers.currencynotifier.ui.notification.Forex

@Dao
interface ForexNotiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(forex:Forex)

    @Delete
    fun delete(forex: Forex)
    @Query("Delete from forex_notification")
    fun deleteAll()

    @Query("Select * from forex_notification ORDER BY createdAt ASC")
    fun getNotifications(): LiveData<List<Forex>>

    @Query("Select * from forex_notification ORDER BY createdAt ASC")
    fun getForexNotifications(): List<Forex>

    @Update
    fun updateNotification(forex: Forex)

    @Update
    fun updateNotifications(forexs:List<Forex>):Int

    @Query("Select * from forex_notification WHERE currencyCode=:currencyCode")
    fun getNotification(currencyCode:String)
}