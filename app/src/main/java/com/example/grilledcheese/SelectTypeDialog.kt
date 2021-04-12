package com.example.grilledcheese

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.work.WorkManager
import com.example.grilledcheese.model.GrilledCheeseViewModel
import com.example.grilledcheese.utils.showLongToast
import com.example.grilledcheese.worker.startWorkManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

const val CANCEL_SELECTION = -1
const val RANDOM_SELECTION = 0
const val HOT_SELECTION = 1

@ExperimentalCoroutinesApi
class SelectTypeDialog(
    private val context: Context,
    private val model: GrilledCheeseViewModel
) {

    fun show() {
        val items = context.resources.getStringArray(R.array.dialog_items)
        AlertDialog.Builder(context)
            .setTitle(R.string.dialog_title)
            .setSingleChoiceItems(
                items,
                CANCEL_SELECTION
            ) { dialog, which ->
                when (which) {
                    RANDOM_SELECTION -> model.setDialogSelection(RANDOM_SELECTION)
                    HOT_SELECTION -> model.setDialogSelection(HOT_SELECTION)
                }
                dialog.dismiss()
                showLongToast(context, R.string.image_set)
            }
            .setNegativeButton(R.string.dialog_cancel) { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }
}

@ExperimentalCoroutinesApi
suspend fun setWallpaperWorker(viewModel: GrilledCheeseViewModel, context: Context) {
    viewModel.getDialogSelection().collectLatest { selection ->
        when (selection) {
            RANDOM_SELECTION -> {
                viewModel.setRandomGrilledCheese()
                startWorkManager(viewModel.grilledCheese, context)
            }
            HOT_SELECTION -> {
                viewModel.setHotGrilledCheese()
                startWorkManager(viewModel.grilledCheese, context)
            }
            CANCEL_SELECTION -> WorkManager.getInstance(context).cancelAllWork()
        }
    }
}
