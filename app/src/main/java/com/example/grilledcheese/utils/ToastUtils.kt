package com.example.grilledcheese.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

    fun showLongToast(context: Context, @StringRes message: Int) =
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()