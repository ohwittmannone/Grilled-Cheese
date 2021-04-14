package com.example.grilledcheese.model

import androidx.lifecycle.ViewModel
import com.example.grilledcheese.IntPersistence
import com.example.grilledcheese.data.GrilledCheese
import com.example.grilledcheese.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
class GrilledCheeseViewModel(
    private val repository: RedditItemRepository,
    private val prefs: IntPersistence
) : ViewModel() {

    val grilledCheese = MutableStateFlow(Resource.initial<GrilledCheese>())
    private val dialogSelection = MutableStateFlow(prefs.read())

    fun setDialogSelection(selection: Int) {
        dialogSelection.value = selection
        prefs.write(selection)
    }

    fun getDialogSelection(): Flow<Int> = dialogSelection

    suspend fun setHotGrilledCheese() {
        grilledCheese.value = Resource.loading()
        for (child in repository.getHotRedditList().data.children) {
            if (child.grilledCheese.url.imageEnding()) {
                grilledCheese.value = Resource.success(child.grilledCheese)
                break
            }
        }
    }

    suspend fun setRandomGrilledCheese() {
        grilledCheese.value = Resource.loading()
        val randomGrilledCheese =
            repository.getRandomReddit().first().data.children.first().randomGrilledCheese
        if (randomGrilledCheese.url.imageEnding()) {
            val item = GrilledCheese(randomGrilledCheese.url)
            grilledCheese.value = Resource.success(item)
        } else {
            setRandomGrilledCheese()
        }
    }

    private fun String.imageEnding(): Boolean {
        return this.endsWith(".jpg") || this.endsWith(".png") || this.endsWith(".jpeg")
    }

}