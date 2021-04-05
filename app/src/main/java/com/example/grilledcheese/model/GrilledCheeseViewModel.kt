package com.example.grilledcheese.model

import androidx.lifecycle.ViewModel
import com.example.grilledcheese.data.GrilledCheese
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class GrilledCheeseViewModel(private val repository: GrilledCheeseRepository) : ViewModel() {

    suspend fun grilledCheese(): Flow<GrilledCheese> {
        return flow {
            withContext(Dispatchers.Default) { //todo abstract Dispatcher
                repository.getGrilledCheese()
            }

        }
    }
}