package com.learning.aplikasigithubuser.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class Item(
    @ColumnInfo(name = "avatar_url")
    val avatar_url: String,
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "login")
    val login: String,
):Parcelable