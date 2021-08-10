package com.avidprogrammers.currencynotifier.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.impl.WorkDatabaseMigrations.MIGRATION_3_4
import com.avidprogrammers.currencynotifier.db.entity.ForexResponseDB
import com.avidprogrammers.currencynotifier.ui.notification.Forex

@Database(
    entities = [ForexResponseDB::class, Forex::class],
    version = 3,
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
            Room.databaseBuilder(context.applicationContext, ForexDatabase::class.java, "forex.db").addMigrations(
                MIGRATION_1_2, MIGRATION_2_3)
                .build()

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `forex_notification` (`id` INTEGER primary key autoincrement, `currencyCode` TEXT, `currencyVal` TEXT, `targetVal` TEXT, `notificationStatus` TEXT, `createdAt` TEXT )" )
            }
        }

        val MIGRATION_2_3=object:Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE forex_notification "
                        + " ADD COLUMN notificationCreatedAt INTEGER DEFAULT 0");
            }
        }

    }
}