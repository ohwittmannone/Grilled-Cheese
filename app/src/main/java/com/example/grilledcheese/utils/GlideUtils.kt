package com.example.grilledcheese.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun setGlideImage(view: ImageView, resUrl: String) {
    Glide.with(view)
        .load(resUrl)
        .into(view)
}