package com.example.grilledcheese.data

import com.google.gson.annotations.SerializedName

class RandomReddit : ArrayList<RandomItem>()

data class RandomItem(val data: RandomData)

data class RandomData(val children: List<RandomChildren>)

data class RandomChildren(@SerializedName("data") val randomGrilledCheese: RandomGrilledCheese)

data class RandomGrilledCheese(val url: String)