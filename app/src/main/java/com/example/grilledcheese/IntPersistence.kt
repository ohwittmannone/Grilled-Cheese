package com.example.grilledcheese

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class IntPersistence(
    private val key: String,
    private val pref: SharedPreferences
) : ReadWriteProperty<Any?, Int>, OnDiskPersistence<Int> {
    override fun read() = pref.getInt(key, CANCEL_SELECTION)

    override fun write(item: Int) = pref.edit { putInt(key, item) }

    override fun getValue(thisRef: Any?, property: KProperty<*>) = read()

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) = write(value)
}