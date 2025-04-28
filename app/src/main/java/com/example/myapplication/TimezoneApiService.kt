package com.example.myapplication

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface TimezoneApiService {
    @GET("timezone")
    suspend fun getTimezones(): List<String>
}

object RetrofitInstance {
    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    val api: TimezoneApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://worldtimeapi.org/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TimezoneApiService::class.java)
    }
}
