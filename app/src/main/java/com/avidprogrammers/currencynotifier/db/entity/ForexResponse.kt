package com.avidprogrammers.currencynotifier.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val FOREX_VALUE_ID = 0

@Entity(tableName = "forex_value")

data class ForexResponse(
    var currencyCode: String,
    var currencyVal: String
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = FOREX_VALUE_ID
}