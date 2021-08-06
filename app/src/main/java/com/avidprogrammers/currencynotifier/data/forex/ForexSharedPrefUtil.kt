package com.avidprogrammers.currencynotifier.data.forex

import android.content.Context
import com.avidprogrammers.currencynotifier.data.network.response.ForexResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ForexSharedPrefUtil(val context: Context) {
    private val sharedPreferences=context.getSharedPreferences("forex",Context.MODE_PRIVATE)
    private val editor=sharedPreferences.edit()
    fun setForexValues(key:String, data:MutableList<ForexResponse>){
        val gson= Gson()
        val json=gson.toJson(data)
        editor.putString(key,json)
        editor.commit()
    }

    fun setForex(key:String,value:ForexResponse){
        var data=getForex(key)
        if(data==null){
            data= mutableListOf()
        }
        if(data.find {it.currencyCode==value.currencyCode  }!=null){
            data.find {it.currencyCode==value.currencyCode  }?.currencyVal=value.currencyVal
        }else{
            data.add(value)
        }
        setForexValues(key,data)
    }

    fun getForex(key:String):MutableList<ForexResponse>?{
        var data:MutableList<ForexResponse>?=null
        var json=sharedPreferences.getString(key,null)
        json?.let {
            val gson=Gson()
            val type=object:TypeToken<MutableList<ForexResponse>>() {}.type
            data=gson.fromJson(json,type)
        }

        return data
    }
}