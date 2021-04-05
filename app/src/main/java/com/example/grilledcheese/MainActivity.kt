package com.example.grilledcheese

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grilledcheese.api.GrilledCheeseService
import com.example.grilledcheese.api.RedditGrilledCheeseAdapter
import com.example.grilledcheese.model.GrilledCheeseRepository
import com.example.grilledcheese.model.GrilledCheeseViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: GrilledCheeseViewModel
    private lateinit var repository: GrilledCheeseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = GrilledCheeseRepository(RedditGrilledCheeseAdapter.create())
        viewModel = GrilledCheeseViewModel(repository)
    }
}