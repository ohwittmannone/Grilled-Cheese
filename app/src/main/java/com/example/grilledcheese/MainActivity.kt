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
        val button = this.findViewById<Button>(R.id.button_background)

        val repository = RedditItemRepository(RedditAdapter.create())
        val viewModel = GrilledCheeseViewModel(repository)

        button.setOnClickListener {
            setPreviewImage(viewModel, imagePreview)
        }
    }

    private fun setPreviewImage(viewModel: GrilledCheeseViewModel, imagePreview: ImageView) {
        launch {
            viewModel.grilledCheese().collectLatest {
                setGlideImage(imagePreview, it.url)
            }
        }
    }
}