package com.example.grilledcheese.model

import androidx.lifecycle.ViewModel
import com.example.grilledcheese.data.GrilledCheese
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

class GrilledCheeseViewModel(private val repository: RedditItemRepository) : ViewModel() {

    @ExperimentalCoroutinesApi
    suspend fun grilledCheese(): Flow<GrilledCheese> {
        return flow {
            withContext(Dispatchers.Main) { //todo abstract Dispatcher
                val arrayList = mutableListOf<GrilledCheese>()
                repository.getRedditList().data.children.map { child ->
                    val url = child.grilledCheese.url
                    if (url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".jpeg")) {
                        arrayList.add(child.grilledCheese)
                    }
                }
                emit(arrayList.first())
            }
        }
    }
}