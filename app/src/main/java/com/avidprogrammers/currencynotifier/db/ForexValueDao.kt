package com.avidprogrammers.currencynotifier.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.avidprogrammers.currencynotifier.db.entity.FOREX_VALUE_ID
import com.avidprogrammers.currencynotifier.db.entity.ForexResponseDB
import com.avidprogrammers.currencynotifier.ui.notification.Forex
import kotlinx.coroutines.flow.Flow

@Dao
interface ForexValueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(ForexResponse: ForexResponseDB)

    @Query("select * from forex_value where id = $FOREX_VALUE_ID")
    fun getForexValue(): Flow<ForexResponseDB?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forex: Forex)

    @Delete
    suspend fun delete(forex: Forex)

    @Query("Delete from forex_notification")
    suspend fun deleteAll()

    @Query("Select * from forex_notification ORDER BY notificationCreatedAt DESC")
    fun getNotifications(): LiveData<List<Forex>>

    @Update
    suspend fun updateNotification(forex: Forex)

    @Query("Select * from forex_notification ORDER BY createdAt ASC")
    fun getForexNotifications(): List<Forex>

    @Update
    suspend fun updateNotifications(forexs:List<Forex>):Int

    @Query("Select * from forex_notification WHERE notificationStatus=:notificationStatus")
    fun getNotification(notificationStatus:String):List<Forex>

    @Query("Select * from forex_notification WHERE notificationStatus=:notificationStatus")
    fun getInProgressNotification(notificationStatus:String):Forex?

}