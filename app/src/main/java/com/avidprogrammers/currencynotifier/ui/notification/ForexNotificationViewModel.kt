package com.avidprogrammers.currencynotifier.ui.notification

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ForexNotificationViewModel(private val forexNotificationRepository: ForexNotificationRepository):ViewModel() {
    val notifications=forexNotificationRepository.getNotifications()
    val filteredNotifications= MediatorLiveData<List<Forex>>()
    val inProgress=MutableLiveData<Boolean>()
    init {
        inProgress.value=false
    }

    fun insertForex(forex:Forex){
        inProgress.value=true
        viewModelScope.launch{
            val foNotifications=forexNotificationRepository.getNotification("In Progress")
            val list=foNotifications.toMutableList()
            list.forEach { it2->
                it2.notificationStatus="Cancelled"
            }
            forexNotificationRepository.updateNotifications(list)
           // updateNotifications(list)
            forexNotificationRepository.insertForex(forex)
            inProgress.value=false

        }
    }

    fun deleteForex(forex:Forex){
        viewModelScope.launch{
            forexNotificationRepository.deleteForex(forex)
        }
    }
    fun deleteForex(position:Int){
        notifications.value?.let {
            deleteForex(it[position])
        }

    }
    fun getForexNotifications():List<Forex>{
       return forexNotificationRepository.getForexNotifications()
    }
     fun deleteAllForex(){
        viewModelScope.launch{
            forexNotificationRepository.deleteAllForex()
        }
    }



/*
    fun getNotification(currencyCode:String){
        filteredNotifications.addSource(forexNotRepository.getNotification(currencyCode)){
            filteredNotifications.value=it
        }
    }*/

    fun updateNotification(forex: Forex){
       viewModelScope.launch{
           forexNotificationRepository.updateNotification(forex)
        }
    }

    fun updateNotifications(forexs:List<Forex>){
       viewModelScope.launch{
            forexNotificationRepository.updateNotifications(forexs)
        }
    }
}