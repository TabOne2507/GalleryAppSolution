package com.abhishekgarala.galleryapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.abhishekgarala.galleryapp.model.Hit

@Dao
interface AppDao {

    @Insert
    suspend fun insert(data:Hit)

    @Query("Select * from app_table")
    fun read():List<Hit>
}