package com.example.grilledcheese.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grilledcheese.IntPersistence
import com.example.grilledcheese.api.RedditAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.IllegalArgumentException

private const val DIALOG_SELECTION = "DIALOG_SELECTION"

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
class GrilledCheeseViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(model: Class<T>): T {
        if (model.isAssignableFrom(GrilledCheeseViewModel::class.java)) {
            val dialogSelectionPrefs = IntPersistence(
                DIALOG_SELECTION,
                context.getSharedPreferences(DIALOG_SELECTION, Context.MODE_PRIVATE)
            )
            val repository = RedditItemRepository(RedditAdapter.create())
            return GrilledCheeseViewModel(repository, dialogSelectionPrefs) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}