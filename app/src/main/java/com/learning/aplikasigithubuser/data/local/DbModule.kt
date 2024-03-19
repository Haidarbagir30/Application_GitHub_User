package com.learning.aplikasigithubuser.data.local

import android.content.Context
import androidx.room.Room

class DbModule(private val context: Context) {
    private val db = Room.databaseBuilder(context, AppDb::class.java, "usergithub.db")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration() // Tambahkan ini untuk mengatasi masalah migrasi
        .build()
    val userDao = db.userDao()
}
