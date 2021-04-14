package com.example.grilledcheese.worker

import android.content.Context
import androidx.work.*
import com.example.grilledcheese.model.GrilledCheeseViewModel
import com.example.grilledcheese.utils.Status
import com.example.grilledcheese.utils.setWallpaper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

const val RES_URL = "RES_URL"
private const val WALLPAPER_WORKER_TAG = "WALLPAPER_DAILY"

class WallpaperWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            if (setWallpaper(
                    this.applicationContext,
                    resUrl = inputData.getString(RES_URL) ?: "https://i.imgur.com/EK9tToe.jpg"
                )
            ) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}

@ExperimentalCoroutinesApi
suspend fun startWorkManager(
    model: GrilledCheeseViewModel,
    context: Context,
    type: SelectionType
): String {
    val url = if (type == SelectionType.RANDOM) {
        model.getRandomGrilledCheeseUrl()
    } else {
        model.getHotGrilledCheeseUrl()
    }
    val workerConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val repeatingRequest =
        PeriodicWorkRequestBuilder<WallpaperWorker>(15, TimeUnit.MINUTES)
            .setConstraints(workerConstraints)
            .setInputData(Data.Builder().putString(RES_URL, url).build())
            .addTag(WALLPAPER_WORKER_TAG)
            .build()
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        WALLPAPER_WORKER_TAG,
        ExistingPeriodicWorkPolicy.REPLACE,
        repeatingRequest
    )

    return url
}

enum class SelectionType { RANDOM, HOT }