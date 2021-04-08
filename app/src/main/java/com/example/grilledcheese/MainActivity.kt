package com.example.grilledcheese

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.grilledcheese.api.RedditAdapter
import com.example.grilledcheese.model.GrilledCheeseViewModel
import com.example.grilledcheese.model.RedditItemRepository
import com.example.grilledcheese.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        val repository = RedditItemRepository(RedditAdapter.create())
        val viewModel = GrilledCheeseViewModel(repository)

        hotButton.setOnClickListener {
            setHotPreviewImage(viewModel, imagePreview, spinner)
        }
        randomButton.setOnClickListener {
            setRandomPreviewImage(viewModel, imagePreview, spinner)
        }

        setBackgroundButton.setOnClickListener {
            if (viewModel.imageUrl.value != "") {
                setWallpaper(this, viewModel.imageUrl.value)
                showLongToast(this, R.string.image_set)
            } else {
                showLongToast(this, R.string.preview_image_toast)
            }
        }
    }

    private fun setHotPreviewImage(
        viewModel: GrilledCheeseViewModel,
        imagePreview: ImageView,
        spinner: ProgressBar
    ) {
        launch {
            viewModel.getHotGrilledCheese().collectLatest {
                setImage(it.status, imagePreview, spinner, it.data?.url)
            }
        }
    }

    private fun setRandomPreviewImage(
        viewModel: GrilledCheeseViewModel,
        imagePreview: ImageView,
        spinner: ProgressBar
    ) {
        launch {
            viewModel.getRandomGrilledCheese().collectLatest {
                setImage(it.status, imagePreview, spinner, it.data?.url)
            }
        }
    }

    private fun setImage(
        status: Status,
        imagePreview: ImageView,
        spinner: ProgressBar,
        url: String? = ""
    ) {
        when (status) {
            Status.SUCCESS -> {
                spinner.visibility = GONE
                imagePreview.visibility = VISIBLE
                setGlideImage(imagePreview, url!!)
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
}