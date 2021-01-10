package com.example.bottomlinelocations.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "language")
    val language: String
)