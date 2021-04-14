package com.example.grilledcheese

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.grilledcheese.model.GrilledCheeseViewModel
import com.example.grilledcheese.utils.showLongToast
import kotlinx.coroutines.ExperimentalCoroutinesApi

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
