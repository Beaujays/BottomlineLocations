package com.example.bottomlinelocations.ui.detailSiteDefect

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bottomlinelocations.data.SiteDefectRepository
import com.example.bottomlinelocations.data.SiteDefects


class DetailSiteDefectsViewModel(
    private val repository: SiteDefectRepository,
    val id: String
) : ViewModel() {
    // Set value with mutable live data
    val siteDefect: MutableLiveData<SiteDefects> =
        repository.getById(id) as MutableLiveData<SiteDefects>

    // Get delete single from repository
    fun deleteSingle(id: String) {
        repository.deleteSingle(id)
    }
}

class DetailSiteDefectsViewModelFactory(
    private val repository: SiteDefectRepository,
    private val siteId: String

) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        check(modelClass.isAssignableFrom(DetailSiteDefectsViewModel::class.java))

        @Suppress("UNCHECKED_CAST")
        return DetailSiteDefectsViewModel(repository, siteId) as T
    }
}