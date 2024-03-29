package com.example.grilledcheese.api

import com.example.grilledcheese.data.RandomReddit
import com.example.grilledcheese.data.Reddit
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

//https://ssl.reddit.com/r/grilledcheese/hot.json?limit=10
//https://ssl.reddit.com/r/grilledcheese/random.json
interface RedditService {
    @GET("/r/grilledcheese/hot.json?limit=10")
    suspend fun getHotReddit(): Reddit

    @GET("/r/grilledcheese/random.json?")
    suspend fun getRandomReddit(): RandomReddit
}

object RedditAdapter {

    fun create(): RedditService {
        val client = OkHttpClient.Builder()
            .addInterceptor(StethoInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://ssl.reddit.com")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
            .build()
            .create(RedditService::class.java)
    }
}