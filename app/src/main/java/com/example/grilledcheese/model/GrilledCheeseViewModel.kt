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

    suspend fun getHotGrilledCheeseUrl(): String {
        repository.getHotRedditList().data.children.forEach { child ->
            if (child.grilledCheese.url.imageEnding()) return child.grilledCheese.url
        }
        return "https://i.imgur.com/EK9tToe.jpg"
    }

    suspend fun setHotGrilledCheese() {
        grilledCheese.value = Resource.loading()
        for (child in repository.getHotRedditList().data.children) {
            if (child.grilledCheese.url.imageEnding()) {
                grilledCheese.value = Resource.success(child.grilledCheese)
                break
            }
        }
    }

    suspend fun getRandomGrilledCheeseUrl(): String =
        repository.getRandomReddit().first().data.children.first().randomGrilledCheese.url

    suspend fun setRandomGrilledCheese() {
        grilledCheese.value = Resource.loading()
        val url = getRandomGrilledCheeseUrl()
        if (url.imageEnding()) {
            val item = GrilledCheese(url)
            grilledCheese.value = Resource.success(item)
        } else {
            setRandomGrilledCheese()
        }
    }

    private fun String.imageEnding(): Boolean {
        return this.endsWith(".jpg") || this.endsWith(".png") || this.endsWith(".jpeg")
    }

}