package com.example.bottomlinelocations.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SettingsDAO {

    @Query("SELECT * FROM Settings ORDER BY id DESC LIMIT 1")
    fun getLastId(): Settings

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(settings: Settings)

    @Query("SELECT * FROM Settings ORDER BY id")
    suspend fun getAll(): List<Settings>
}