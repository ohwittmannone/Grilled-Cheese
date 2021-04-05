package com.example.grilledcheese.model

import com.example.grilledcheese.api.GrilledCheeseService

class GrilledCheeseRepository(private val service: GrilledCheeseService) {

    suspend fun getGrilledCheese() = service.getRandomGrilledCheese()
}