package com.avidprogrammers.currencynotifier.data.forex

import androidx.annotation.Keep
import com.avidprogrammers.currencynotifier.BuildConfig
import com.avidprogrammers.currencynotifier.data.network.ConnectivityInterceptor
import com.avidprogrammers.currencynotifier.data.network.response.ForexResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = BuildConfig.API_URL

interface ForexApiService {

    @Keep
    @GET("api/currency")
    fun getCurrentValueAsync(
        @Query("val") currencyCode: String
    ): Deferred<ForexResponse>

    @Keep
    @GET("api/currency")
    fun getCurrentValueSync(@Query("val")currencyCode: String):Call<ForexResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): ForexApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ForexApiService::class.java)
        }
    }
}
