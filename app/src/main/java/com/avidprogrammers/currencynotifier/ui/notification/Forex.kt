package com.avidprogrammers.currencynotifier.ui.notification

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forex_notification")
data class Forex(
    @PrimaryKey(autoGenerate = true)
    var id:Int?,
    var currencyCode: String?,
    var currencyVal: String?,
    var targetVal:String?,
    var notificationStatus:String?,
    var createdAt:String?,
    var notificationCreatedAt:Long?


)
