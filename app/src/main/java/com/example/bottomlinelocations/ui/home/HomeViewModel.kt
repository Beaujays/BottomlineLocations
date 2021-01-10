package com.example.bottomlinelocations.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bottomlinelocations.data.Settings
import com.example.bottomlinelocations.data.SettingsRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: SettingsRepository) : ViewModel() {

    // Set value with mutable live data
    val lastLanguage: MutableLiveData<Settings> = repository.getLastId() as MutableLiveData<Settings>

    // Get insert from repository
    fun insert(settings: Settings) = viewModelScope.launch {
        repository.insert(settings)
    }
}

class HomeViewModelFactory(private val repository: SettingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}