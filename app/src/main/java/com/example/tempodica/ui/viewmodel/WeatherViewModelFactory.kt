package com.example.tempodica.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tempodica.data.local.AppDatabase
import com.example.tempodica.repository.WeatherRepository

class WeatherViewModelFactory(
    private val context: Context,
    private val useFake: Boolean = false,
    private val useFakeFallback: Boolean = true
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            val dao = AppDatabase.getInstance(context).weatherDao()
            val repo = com.example.tempodica.repository.WeatherRepository(dao, useFake = useFake, useFakeFallback = useFakeFallback)
            return WeatherViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
