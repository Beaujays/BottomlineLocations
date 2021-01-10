package com.example.bottomlinelocations

import androidx.multidex.MultiDexApplication
import com.example.bottomlinelocations.data.SettingsRepository
import com.example.bottomlinelocations.data.SiteDefectRepository
import com.example.bottomlinelocations.database.MyRoomDatabase
import com.google.firebase.firestore.FirebaseFirestore

class MyApplication : MultiDexApplication() {
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val database by lazy {  MyRoomDatabase.getInstance(this) }
    val siteDefectRepository by lazy { SiteDefectRepository(firestore.collection("SiteDefects")) }
    val settingsRepository by lazy { SettingsRepository(database.settingsDao()) }
}