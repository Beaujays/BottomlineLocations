package com.example.bottomlinelocations.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SiteDefectRepository(private val siteDefectsCollection: CollectionReference) {
    private val defaultSiteDefect =
        SiteDefects("", "loading", "One moment please", "", "", "", "", "")

    private val all: MutableLiveData<List<SiteDefects>> =
        MutableLiveData(listOf(defaultSiteDefect))

    // Post document on Fire store
    fun insert(siteDefects: SiteDefects) {
        siteDefectsCollection.document().set(
            siteDefects.toData()
        ).addOnSuccessListener { getAll() }
    }

    // Get all documents from Fire store
    fun getAll(): LiveData<List<SiteDefects>> {
        siteDefectsCollection.get()
            .addOnSuccessListener { documents ->
                documents
                    .map(QueryDocumentSnapshot::toSiteDefect)
                    .let(all::postValue)
            }
        return all
    }

    // Get by ID from Fire store
    fun getById(id: String): LiveData<SiteDefects> {
        val result = MutableLiveData(defaultSiteDefect)
        siteDefectsCollection.document(id).get()
            .addOnSuccessListener { doc ->
                doc.toSiteDefect()
                    .let(result::postValue)
            }
        return result
    }

    // Delete by ID from Fire store
    fun deleteSingle(id: String) {
        GlobalScope.launch {
            siteDefectsCollection.document(id).delete()
                .addOnSuccessListener {
                    getAll()
                }
        }
    }
}


