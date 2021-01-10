package com.example.bottomlinelocations.data

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsRepository(private val settingsDAO: SettingsDAO) {
    private val defaultSetting = Settings(0,"en")

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(settings: Settings){
        settingsDAO.insert(settings)
    }

    fun getLastId(): LiveData<Settings> {
        val result = MutableLiveData(defaultSetting)
        GlobalScope.launch {
            result.postValue(settingsDAO.getLastId())
            Log.i("Repository", "Language for repository: $result")
        }
        return result
    }
}