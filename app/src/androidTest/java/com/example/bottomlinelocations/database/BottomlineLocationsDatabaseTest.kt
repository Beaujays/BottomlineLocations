package com.example.bottomlinelocations.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.bottomlinelocations.data.Settings
import com.example.bottomlinelocations.data.SettingsDAO
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BottomlineLocationsDatabaseTest {

    private lateinit var settingsDAO: SettingsDAO
    private lateinit var db: MyRoomDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, MyRoomDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        settingsDAO = db.settingsDao()
        //val settings = Settings(1, "en")
        //settingsDAO.insert(settings)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertSetting() {
        val settings = Settings(2, "nl")
        settingsDAO.insert(settings)
        assertEquals(settingsDAO.getAll().size, 2)
    }

    @Test
    @Throws(Exception::class)
    suspend fun getLastId() {
        settingsDAO.getLastId()
        assertEquals(settingsDAO.getAll().toString(), "2, nl")
    }
}