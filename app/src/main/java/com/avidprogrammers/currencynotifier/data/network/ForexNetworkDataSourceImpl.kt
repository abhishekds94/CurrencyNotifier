package com.avidprogrammers.currencynotifier.data.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.avidprogrammers.currencynotifier.data.forex.ForexApiService
import com.avidprogrammers.currencynotifier.data.forex.Response
import com.avidprogrammers.currencynotifier.data.network.response.ForexResponse

class ForexNetworkDataSourceImpl(
    private val forexApiService: ForexApiService
) : ForexNetworkDataSource {

    private val _downloadedForex = MutableLiveData<ForexResponse>()
    override val downloadedForex: LiveData<ForexResponse>
        get() = _downloadedForex

    override suspend fun fetchCurrentValue(currencyCode: String): ForexResponse {

        return forexApiService
            .getCurrentValueAsync(currencyCode)
            .await()
        // _downloadedForex.postValue(fetchedCurrentValue)


    }

    override suspend fun fetchValue(): List<Response> {
        TODO("Not yet implemented")
    }

}