package com.example.grilledcheese.utils

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target


fun setGlideImage(view: ImageView, resUrl: String) {
    Glide.with(view)
        .load(resUrl)
        .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
        .into(view)
}

@SuppressLint("CheckResult")
fun setWallpaper(context: Context, resUrl: String): Boolean {
    var isSuccessful = false
    val wallpaperManager = WallpaperManager.getInstance(context)
    Glide.with(context)
        .asBitmap()
        .load(resUrl)
        .listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                wallpaperManager.setBitmap(resource)
                isSuccessful = true
                return true
            }

        })
        .preload()
    return isSuccessful
}
