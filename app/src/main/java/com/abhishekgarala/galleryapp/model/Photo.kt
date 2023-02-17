package com.abhishekgarala.galleryapp.model

data class Photo(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int
)