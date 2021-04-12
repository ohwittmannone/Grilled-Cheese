package com.example.grilledcheese.worker

import android.content.Context
import androidx.work.*
import com.example.grilledcheese.utils.Status
import com.example.grilledcheese.utils.setWallpaper
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

const val RES_URL = "RES_URL"

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

fun startWorkManager(status: Status, url: String?, context: Context) {
    if (status == Status.SUCCESS) {
        val workerConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val repeatingRequest =
            PeriodicWorkRequestBuilder<WallpaperWorker>(1, TimeUnit.DAYS)
                .setConstraints(workerConstraints)
                .setInputData(
                    Data.Builder().putString(RES_URL, url).build()
                )
                .build()
        WorkManager.getInstance(context).enqueue(repeatingRequest)
    }
}