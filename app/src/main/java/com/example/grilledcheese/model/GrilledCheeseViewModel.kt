package com.example.grilledcheese.model

import androidx.lifecycle.ViewModel
import com.example.grilledcheese.utils.DefaultDispatcher
import com.example.grilledcheese.utils.DispatcherProvider
import com.example.grilledcheese.data.GrilledCheese
import com.example.grilledcheese.data.RandomGrilledCheese
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class GrilledCheeseViewModel(
    private val repository: RedditItemRepository,
    private val dispatcher: DispatcherProvider = DefaultDispatcher()
) : ViewModel() {

    @ExperimentalCoroutinesApi
    suspend fun getHotGrilledCheese(): Flow<GrilledCheese> {
        return flow {
            withContext(dispatcher.main()) {
                emit(checkHotGrilledCheese())
            }
        }
    }

    suspend fun getRandomGrilledCheese(): Flow<RandomGrilledCheese> {
        return flow {
            withContext(dispatcher.main()) {
                emit(checkRandomGrilledCheese())
            }
        }
    }

    private fun String.imageEnding(): Boolean {
        return this.endsWith(".jpg") || this.endsWith(".png") || this.endsWith(".jpeg")
    }

    private suspend fun checkHotGrilledCheese(): GrilledCheese {
        for (child in repository.getHotRedditList().data.children) {
            if (child.grilledCheese.url.imageEnding()) return child.grilledCheese
        }
        return GrilledCheese("https://i.imgur.com/EK9tToe.jpg", "https://i.imgur.com/EK9tToe.jpg")
    }

    private suspend fun checkRandomGrilledCheese(): RandomGrilledCheese {
        val randomGrilledCheese =
            repository.getRandomReddit().first().data.children.first().randomGrilledCheese
        return if (randomGrilledCheese.url.imageEnding()) {
            randomGrilledCheese
        } else {
            checkRandomGrilledCheese()
        }
    }
}