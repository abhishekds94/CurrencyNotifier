package com.avidprogrammers.currencynotifier.data.forex.repository

import android.util.Log
import android.widget.Toast
import com.avidprogrammers.currencynotifier.data.network.ForexNetworkDataSource
import com.avidprogrammers.currencynotifier.data.network.response.ForexResponse
import com.avidprogrammers.currencynotifier.db.ForexValueDao
import com.avidprogrammers.currencynotifier.db.entity.ForexResponseDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class ForexRepositoryImpl(
    private val ForexValueDao: ForexValueDao,
    private val ForexNetworkDataSource: ForexNetworkDataSource
) : ForexRepository {

  /*  init {
        ForexNetworkDataSource.downloadedForex.observeForever { newForexValue ->
            persistFetchedForexValue(newForexValue)
        }
    }*/

    private val value: Flow<ForexResponseDB?> = ForexValueDao.getForexValue()

    override suspend fun getCurrentValue(value: String) {
        return withContext(Dispatchers.IO) {
            initForexData(value)
        }
    }

    override fun getCurrentForexValue(): Flow<ForexResponseDB?> {
        return value
    }

    private fun persistFetchedForexValue(forexValue: ForexResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            ForexValueDao.upsert(
                ForexResponseDB(
                    currencyCode = forexValue.currencyCode,
                    currencyVal = forexValue.currencyVal
                )
            )
        }
    }

    private suspend fun initForexData(value: String) {
        if (isFetchForexNeeded(ZonedDateTime.now().minusMinutes(61)))
            fetchForexValue(value)
        Log.d("[Forex123]", "[Forex123] initForexData -" + fetchForexValue(value).toString())
    }

    private suspend fun fetchForexValue(value: String) {
        Log.d("[Forex123]", "[Forex123] value -fetching")
       // ForexNetworkDataSource.fetchCurrentValue(currencyCode = value)
        try {
            val forexValue = ForexNetworkDataSource.fetchCurrentValue(currencyCode = value)
            withContext(Dispatchers.IO){
                ForexValueDao.upsert(
                    ForexResponseDB(
                        currencyCode = forexValue.currencyCode,
                        currencyVal = forexValue.currencyVal
                    )
                )
            }

        }catch (e:Exception){
            Log.d("[Forex123]","Network Error")
        }


    }

    private fun isFetchForexNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val sixtyMinsAgo = ZonedDateTime.now().minusMinutes(60)
        Log.d("[Forex123]", "[sixtyMinsAgo] sixtyMinsAgo -$sixtyMinsAgo")
        return lastFetchTime.isBefore(sixtyMinsAgo)
    }


}