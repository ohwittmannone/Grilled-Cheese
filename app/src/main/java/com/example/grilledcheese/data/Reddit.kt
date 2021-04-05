package com.example.grilledcheese.data

import com.google.gson.annotations.SerializedName

data class Reddit(val data: Data)
data class Data(val children: List<Children>)

data class Children(@SerializedName("data") val grilledCheese: GrilledCheese)

data class GrilledCheese(
    val url: String,
    val url_overridden_by_dest: String
)