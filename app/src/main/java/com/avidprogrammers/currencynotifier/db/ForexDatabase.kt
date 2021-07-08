package com.avidprogrammers.currencynotifier.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.avidprogrammers.currencynotifier.db.entity.ForexResponseDB

@Database(
    entities = [ForexResponseDB::class],
    version = 1,
    exportSchema = false
)
abstract class ForexDatabase : RoomDatabase() {
    abstract fun ForexValueDao(): ForexValueDao

    companion object {
        @Volatile
        private var instance: ForexDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, ForexDatabase::class.java, "forex.db")
                .build()
    }
}