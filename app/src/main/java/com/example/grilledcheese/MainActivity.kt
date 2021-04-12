package com.example.grilledcheese

import android.content.Context
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.grilledcheese.api.RedditAdapter
import com.example.grilledcheese.data.GrilledCheese
import com.example.grilledcheese.model.GrilledCheeseViewModel
import com.example.grilledcheese.model.RedditItemRepository
import com.example.grilledcheese.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val DIALOG_SELECTION = "DIALOG_SELECTION"

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imagePreview = this.findViewById<ImageView>(R.id.preview_image)
        val hotButton = this.findViewById<Button>(R.id.button_hot)
        val randomButton = this.findViewById<Button>(R.id.button_random)
        val spinner = this.findViewById<ProgressBar>(R.id.loading_spinner)
        val setBackgroundButton = this.findViewById<Button>(R.id.button_set_background)
        val workerButton = this.findViewById<Button>(R.id.button_worker)
        val cancelButton = this.findViewById<Button>(R.id.button_cancel)

        val dialogSelectionPrefs = IntPersistence(
            DIALOG_SELECTION,
            this.getSharedPreferences(DIALOG_SELECTION, Context.MODE_PRIVATE)
        )
        val repository = RedditItemRepository(RedditAdapter.create())
        val viewModel = GrilledCheeseViewModel(repository, prefs = dialogSelectionPrefs)
        val dialog = SelectTypeDialog(this, viewModel)

        setWorkerButtonVisibility(viewModel, workerButton, cancelButton)

        hotButton.setOnClickListener { launch { viewModel.setHotGrilledCheese() } }
        randomButton.setOnClickListener { launch { viewModel.setRandomGrilledCheese() } }
        setBackgroundButton.setOnClickListener {
            if (viewModel.grilledCheese.value.data?.url != "") {
                setWallpaper(this, viewModel.grilledCheese.value.data!!.url)
                showLongToast(this, R.string.image_set)
            } else {
                showLongToast(this, R.string.preview_image_toast)
            }
        }
        cancelButton.setOnClickListener { viewModel.setDialogSelection(CANCEL_SELECTION) }
        workerButton.setOnClickListener { dialog.show() }

        launch { setWallpaperWorker(viewModel, this@MainActivity) }
        setPreviewImage(viewModel, imagePreview, spinner)
    }

    private fun setPreviewImage(
        viewModel: GrilledCheeseViewModel,
        imagePreview: ImageView,
        spinner: ProgressBar
    ) {
        launch {
            viewModel.grilledCheese.collectLatest {
                setImage(it, imagePreview, spinner)
            }
        }
    }

    private fun setImage(
        resource: Resource<GrilledCheese>,
        imagePreview: ImageView,
        spinner: ProgressBar
    ) {
        when (resource.status) {
            Status.SUCCESS -> {
                spinner.visibility = GONE
                imagePreview.visibility = VISIBLE
                setGlideImage(imagePreview, resource.data!!.url)
            }
            Status.LOADING -> {
                spinner.visibility = VISIBLE
                imagePreview.visibility = GONE
            }
            else -> {
                spinner.visibility = GONE
                imagePreview.visibility = VISIBLE
                imagePreview.setImageResource(R.drawable.ic_baseline_error_outline_24)
            }
        }
    }

    private fun setWorkerButtonVisibility(
        viewModel: GrilledCheeseViewModel,
        workerButton: Button,
        cancelButton: Button
    ) {
        launch {
            viewModel.getDialogSelection().collectLatest {
                when (it) {
                    CANCEL_SELECTION -> {
                        workerButton.visibility = VISIBLE
                        cancelButton.visibility = GONE
                    }
                    else -> {
                        workerButton.visibility = GONE
                        cancelButton.visibility = VISIBLE
                    }
                }
            }
        }
    }
}