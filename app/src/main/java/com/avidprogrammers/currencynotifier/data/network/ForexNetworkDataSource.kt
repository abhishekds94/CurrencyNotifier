package com.avidprogrammers.currencynotifier.data.network

import androidx.lifecycle.LiveData
import com.avidprogrammers.currencynotifier.data.forex.Response
import com.avidprogrammers.currencynotifier.data.network.response.ForexResponse

interface ForexNetworkDataSource {
    val downloadedForex: LiveData<ForexResponse>

    suspend fun fetchCurrentValue(
        currencyCode: String
    ):ForexResponse

    suspend fun fetchValue():List<Response>
}