package com.avidprogrammers.currencynotifier.data.forex.repository

import androidx.lifecycle.LiveData
import com.avidprogrammers.currencynotifier.db.entity.ForexResponse

interface ForexRepository {
    suspend fun getCurrentValue(): LiveData<ForexResponse>
}