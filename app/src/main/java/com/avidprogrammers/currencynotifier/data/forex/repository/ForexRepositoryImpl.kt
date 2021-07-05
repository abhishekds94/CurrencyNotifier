package com.avidprogrammers.currencynotifier.data.forex.repository

import androidx.lifecycle.LiveData
import com.avidprogrammers.currencynotifier.data.network.ForexNetworkDataSource
import com.avidprogrammers.currencynotifier.db.ForexValueDao
import com.avidprogrammers.currencynotifier.db.entity.ForexResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class ForexRepositoryImpl(
    private val ForexValueDao: ForexValueDao,
    private val ForexNetworkDataSource: ForexNetworkDataSource
) : ForexRepository {

    init {
        ForexNetworkDataSource.downloadedForex.observeForever { newForexValue ->
            persistFetchedForexValue(newForexValue)
        }
    }

    override suspend fun getCurrentValue(): LiveData<ForexResponse> {
        return withContext(Dispatchers.IO) {
            initForexData()
            return@withContext ForexValueDao.getForexValue()
        }
    }

    private fun persistFetchedForexValue(forexValue: ForexResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            ForexValueDao.upsert(ForexResponse(currencyCode = String(), currencyVal = String()))
        }
    }

    private suspend fun initForexData() {
        if (isFetchForexNeeded(ZonedDateTime.now().minusMinutes(61)))
            fetchForexValue()
    }

    private suspend fun fetchForexValue() {
        ForexNetworkDataSource.fetchCurrentValue(currencyCode = "USDINR")
    }

    private fun isFetchForexNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val sixtyMinsAgo = ZonedDateTime.now().minusMinutes(60)
        return lastFetchTime.isBefore(sixtyMinsAgo)
    }


}