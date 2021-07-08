package com.avidprogrammers.currencynotifier.data.forex.repository

import com.avidprogrammers.currencynotifier.db.entity.ForexResponseDB
import kotlinx.coroutines.flow.Flow

interface ForexRepository {
    suspend fun getCurrentValue()
    fun getCurrentForexValue(): Flow<ForexResponseDB>
}