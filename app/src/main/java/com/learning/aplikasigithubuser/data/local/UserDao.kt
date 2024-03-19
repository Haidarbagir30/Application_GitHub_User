package com.learning.aplikasigithubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.learning.aplikasigithubuser.data.model.Item

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (user:Item)

    @Query("SELECT * FROM User")
    fun loadAll():LiveData<MutableList<Item>> // setiap perubahan dan penambahan data akan mendapatkan infonya

    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
    fun findByid(id: Int): Item

    @Delete
    fun delete(user: Item)


}