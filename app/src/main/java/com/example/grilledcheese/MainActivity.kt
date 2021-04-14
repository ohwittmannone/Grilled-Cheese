package com.example.grilledcheese

import android.content.Context
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.grilledcheese.data.GrilledCheese
import com.example.grilledcheese.model.GrilledCheeseViewModel
import com.example.grilledcheese.model.GrilledCheeseViewModelFactory
import com.example.grilledcheese.utils.*
import com.example.grilledcheese.worker.startWorkManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

private lateinit var imagePreview: ImageView
private lateinit var hotButton: Button
private lateinit var randomButton: Button
private lateinit var setBackgroundButton: Button
private lateinit var workerButton: Button
private lateinit var cancelButton: Button
private lateinit var spinner: ProgressBar

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imagePreview = this.findViewById(R.id.preview_image)
        hotButton = this.findViewById(R.id.button_hot)
        randomButton = this.findViewById(R.id.button_random)
        spinner = this.findViewById(R.id.loading_spinner)
        setBackgroundButton = this.findViewById(R.id.button_set_background)
        workerButton = this.findViewById(R.id.button_worker)
        cancelButton = this.findViewById(R.id.button_cancel)

        val viewModel = ViewModelProvider(
            this,
            GrilledCheeseViewModelFactory(this)
        ).get(GrilledCheeseViewModel::class.java)
        val dialog = SelectTypeDialog(this, viewModel)

        setWorkerButtonVisibility(viewModel)

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

        setWallpaperWorker(viewModel, this@MainActivity)
        setPreviewImage(viewModel)
    }

    private fun setPreviewImage(viewModel: GrilledCheeseViewModel) {
        launch {
            viewModel.grilledCheese.collectLatest {
                setImage(it)
            }
        }
    }

    private fun setImage(resource: Resource<GrilledCheese>) {
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

    private fun setWorkerButtonVisibility(viewModel: GrilledCheeseViewModel) {
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

    private fun setWallpaperWorker(viewModel: GrilledCheeseViewModel, context: Context) {
        launch {
            withContext(DefaultDispatcher().main()) {
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
        }
    }
}