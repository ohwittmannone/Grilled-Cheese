package com.example.grilledcheese

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.grilledcheese.api.RedditAdapter
import com.example.grilledcheese.model.RedditItemRepository
import com.example.grilledcheese.model.GrilledCheeseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imagePreview = this.findViewById<ImageView>(R.id.preview_image)
        val hotButton = this.findViewById<Button>(R.id.button_hot)
        val randomButton = this.findViewById<Button>(R.id.button_random)

        val repository = RedditItemRepository(RedditAdapter.create())
        val viewModel = GrilledCheeseViewModel(repository)

        hotButton.setOnClickListener {
            setHotPreviewImage(viewModel, imagePreview)
        }
        randomButton.setOnClickListener {
            setRandomPreviewImage(viewModel, imagePreview)
        }
    }

    private fun setHotPreviewImage(viewModel: GrilledCheeseViewModel, imagePreview: ImageView) {
        launch {
            viewModel.getHotGrilledCheese().collectLatest {
                setGlideImage(imagePreview, it.url)
            }
        }
    }

    private fun setRandomPreviewImage(viewModel: GrilledCheeseViewModel, imagePreview: ImageView) {
        launch {
            viewModel.getRandomGrilledCheese().collectLatest {
                setGlideImage(imagePreview, it.url)
            }
        }
    }
}