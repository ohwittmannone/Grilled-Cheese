package com.example.grilledcheese.api

import com.example.grilledcheese.data.GrilledCheese
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface GrilledCheeseService {
    @GET("r/grilledcheese/random.json?limit=1&raw_json=1")
    suspend fun getRandomGrilledCheese(): GrilledCheese
}

object RedditGrilledCheeseAdapter {

    fun create(): GrilledCheeseService {
        val client = OkHttpClient.Builder()
            .addInterceptor(StethoInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://ssl.reddit.com")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
            .build()
            .create(GrilledCheeseService::class.java)
    }
}