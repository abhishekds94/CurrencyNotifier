package com.avidprogrammers.currencynotifier.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.avidprogrammers.currencynotifier.db.entity.FOREX_VALUE_ID
import com.avidprogrammers.currencynotifier.db.entity.ForexResponseDB

@Dao
interface ForexValueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(ForexResponse: ForexResponseDB)

    @Query("select * from forex_value where id = $FOREX_VALUE_ID")
    fun getForexValue(): LiveData<ForexResponseDB>
}