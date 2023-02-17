package com.abhishekgarala.galleryapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "app_table")
data class Hit(

    val id: Int,
    val collections: Int,
    val comments: Int,
    val downloads: Int,
    val imageHeight: Int,
    val imageSize: Int,
    val imageWidth: Int,
    val largeImageURL: String,
    val likes: Int,
    val pageURL: String,
    val previewHeight: Int,
    val previewURL: String,
    val previewWidth: Int,
    val tags: String,
    val type: String,
    val user: String,
    val userImageURL: String,
    val user_id: Int,
    val views: Int,
    val webformatHeight: Int,
    val webformatURL: String,
    val webformatWidth: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id1: Int = 0
}