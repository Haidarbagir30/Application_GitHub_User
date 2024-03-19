package com.learning.aplikasigithubuser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.learning.aplikasigithubuser.data.model.Item

@Database(entities = [Item::class], version = 3, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME = "my_database"

        // Define migrasi dari versi 1 ke versi 2
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Tambahan perintah SQL migrasi dari versi 1 ke versi 2
            }
        }

        // Define migrasi dari versi 2 ke versi 3
        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Tambahan perintah SQL migrasi dari versi 2 ke versi 3
            }
        }

        // Define migrasi dari versi 1 ke versi 3
        private val MIGRATION_1_3: Migration = object : Migration(1, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Tambahan perintah SQL migrasi dari versi 1 ke versi 3
            }
        }

        fun build(context: Context): AppDb {
            return Room.databaseBuilder(context, AppDb::class.java, DATABASE_NAME)
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_1_3) // Tambahan migrasi
                .build()
        }
    }
}
