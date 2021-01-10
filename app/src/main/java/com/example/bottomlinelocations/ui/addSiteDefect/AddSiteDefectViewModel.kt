package com.example.bottomlinelocations.ui.addSiteDefect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bottomlinelocations.data.SiteDefectRepository
import com.example.bottomlinelocations.data.SiteDefects
import kotlinx.coroutines.launch

class AddSiteDefectViewModel(private val repository: SiteDefectRepository) : ViewModel() {

    // Get insert from repository
    fun insert(siteDefects: SiteDefects) = viewModelScope.launch {
        repository.insert(siteDefects)
    }
}

class AddSiteDefectViewModelFactory(private val repository: SiteDefectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddSiteDefectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddSiteDefectViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}