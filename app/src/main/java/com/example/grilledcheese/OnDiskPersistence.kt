package com.example.grilledcheese

interface OnDiskPersistence<T> {

    fun write(item: T)

    fun read(): T
}