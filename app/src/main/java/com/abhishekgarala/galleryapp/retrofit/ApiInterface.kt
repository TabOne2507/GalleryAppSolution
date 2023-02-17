package com.abhishekgarala.galleryapp.retrofit

import com.abhishekgarala.galleryapp.model.Photo
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("api/?key=33557813-fed6bf15db22152ce9534e4ea&q=flower&image_type=photo&pretty=true")
    suspend fun getPhotoList(): Response<Photo>
}