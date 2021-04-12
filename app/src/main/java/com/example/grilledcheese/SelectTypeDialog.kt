package com.example.grilledcheese

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.work.WorkManager
import com.example.grilledcheese.model.GrilledCheeseViewModel
import com.example.grilledcheese.worker.startWorkManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

const val CANCEL_SELECTION = -1 //todo have this be persistent
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
                    RANDOM_SELECTION -> {
                        model.dialogSelection.value = 0
                        dialog.dismiss()
                    }
                    HOT_SELECTION -> {
                        model.dialogSelection.value = 1
                        dialog.dismiss()
                    }
                }
            }
            .setNegativeButton(R.string.dialog_cancel) { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }
}

@ExperimentalCoroutinesApi
suspend fun handleDialogSelection(viewModel: GrilledCheeseViewModel, context: Context) {
    viewModel.dialogSelection.collectLatest { selection ->
        when (selection) {
            RANDOM_SELECTION -> {
                viewModel.getRandomGrilledCheese().collectLatest {
                    startWorkManager(it.status, it.data?.url, context)
                }
            }
            HOT_SELECTION -> {
                viewModel.getHotGrilledCheese().collectLatest {
                    startWorkManager(it.status, it.data?.url, context)
                }
            }
            CANCEL_SELECTION -> WorkManager.getInstance(context).cancelAllWork()
        }
    }
}