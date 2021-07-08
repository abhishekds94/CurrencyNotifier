package com.avidprogrammers.currencynotifier.data.forex.repository

import androidx.lifecycle.LiveData
import com.avidprogrammers.currencynotifier.db.entity.ForexResponseDB

interface ForexRepository {
    suspend fun getCurrentValue(): LiveData<ForexResponseDB>
}