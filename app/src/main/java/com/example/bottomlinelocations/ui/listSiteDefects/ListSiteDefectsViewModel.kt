package com.example.bottomlinelocations.ui.listSiteDefects

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bottomlinelocations.data.SiteDefectRepository
import com.example.bottomlinelocations.data.SiteDefects

class ListSiteDefectsViewModel(repository: SiteDefectRepository) : ViewModel() {
    // Set value with live data
    val allSiteDefects: LiveData<List<SiteDefects>> = repository.getAll()
}

class ListSiteDefectsViewModelFactory(
    private val repository: SiteDefectRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        check(modelClass.isAssignableFrom(ListSiteDefectsViewModel::class.java))

        @Suppress("UNCHECKED_CAST")
        return ListSiteDefectsViewModel(repository) as T
    }
}