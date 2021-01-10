package com.example.bottomlinelocations.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bottomlinelocations.data.Settings
import com.example.bottomlinelocations.data.SettingsDAO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Settings::class], version = 7, exportSchema = false)
abstract class MyRoomDatabase : RoomDatabase() {

    abstract fun settingsDao(): SettingsDAO

    companion object {
        @Volatile
        private var INSTANCE: MyRoomDatabase? = null

        fun getInstance(context: Context): MyRoomDatabase {

            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        val instance = Room.databaseBuilder(
                            context.applicationContext,
                            MyRoomDatabase::class.java,
                            "myRoomDatabase"

                        )
                            .addCallback(MyRoomDatabasePopulator())
                            .fallbackToDestructiveMigration()
                            .build()
                        INSTANCE = instance
                    }
                }
            }
            return INSTANCE!!
        }
    }

    private class MyRoomDatabasePopulator : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let {
                GlobalScope.launch {
                    val settingsDAO = it.settingsDao()
                    settingsDAO.insert(
                        Settings(
                            0,
                            "en"
                        )
                    )
                }
            }
        }
    }
}
