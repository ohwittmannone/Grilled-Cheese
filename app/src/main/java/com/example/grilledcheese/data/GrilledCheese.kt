package com.example.grilledcheese.data

import com.google.gson.annotations.SerializedName

data class GrilledCheese(
    @SerializedName("url_overridden_by_dest") val imgUrl: String
)