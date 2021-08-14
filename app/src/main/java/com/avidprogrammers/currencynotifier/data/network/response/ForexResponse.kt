package com.avidprogrammers.currencynotifier.data.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ForexResponse(
    @SerializedName("currencyCode")
    var currencyCode: String,
    @SerializedName("currencyVal")
    var currencyVal: String
)