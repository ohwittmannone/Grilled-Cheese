package com.example.grilledcheese.model

import com.example.grilledcheese.api.RedditService

class RedditItemRepository(private val service: RedditService) {

    suspend fun getHotRedditList() = service.getHotReddit()

    suspend fun getRandomReddit() = service.getRandomReddit()
}