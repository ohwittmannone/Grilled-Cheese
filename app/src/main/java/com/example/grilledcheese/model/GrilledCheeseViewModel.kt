package com.example.grilledcheese.model

import androidx.lifecycle.ViewModel
import com.example.grilledcheese.data.GrilledCheese
import com.example.grilledcheese.data.RandomGrilledCheese
import com.example.grilledcheese.utils.DefaultDispatcher
import com.example.grilledcheese.utils.DispatcherProvider
import com.example.grilledcheese.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
class GrilledCheeseViewModel(
    private val repository: RedditItemRepository,
    private val dispatcher: DispatcherProvider = DefaultDispatcher()
) : ViewModel() {

    val imageUrl = MutableStateFlow("")

    @ExperimentalCoroutinesApi
    suspend fun getHotGrilledCheese(): Flow<Resource<GrilledCheese>> {
        return flow {
            withContext(dispatcher.main()) {
                emit(Resource.loading())
                try {
                    val grilledCheese = checkHotGrilledCheese()
                    imageUrl.value = grilledCheese.url
                    emit(Resource.success(grilledCheese))
                } catch (e: Exception) {
                    emit(Resource.error(message = e.message))
                }
            }
        }
    }

    suspend fun getRandomGrilledCheese(): Flow<Resource<RandomGrilledCheese>> {
        return flow {
            withContext(dispatcher.main()) {
                emit(Resource.loading())
                try {
                    val grilledCheese = checkRandomGrilledCheese()
                    imageUrl.value = grilledCheese.url
                    emit(Resource.success(grilledCheese))
                } catch (e: Exception) {
                    emit(Resource.error(message = e.message))
                }
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