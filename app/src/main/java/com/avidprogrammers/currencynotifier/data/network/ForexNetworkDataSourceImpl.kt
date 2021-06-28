package com.avidprogrammers.currencynotifier.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.avidprogrammers.currencynotifier.data.forex.ForexApiService
import com.avidprogrammers.currencynotifier.db.entity.ForexResponse
import com.avidprogrammers.currencynotifier.internal.NoConnectivityException

class ForexNetworkDataSourceImpl(
    private val forexApiService: ForexApiService
) : ForexNetworkDataSource {

    private val _downloadedForex = MutableLiveData<ForexResponse>()
    override val downloadedForex: LiveData<ForexResponse>
        get() = _downloadedForex

    override suspend fun fetchCurrentValue(currencyCode: String) {
        try {
            val fetchedCurrentValue = forexApiService
                .getCurrentValueAsync(currencyCode)
                .await()
            _downloadedForex.postValue(fetchedCurrentValue)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet Connection - ", e)
        }

    }
}