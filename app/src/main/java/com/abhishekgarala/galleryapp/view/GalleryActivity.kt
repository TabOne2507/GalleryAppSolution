package com.abhishekgarala.galleryapp.view

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishekgarala.galleryapp.database.AppDatabase
import com.abhishekgarala.galleryapp.databinding.ActivityGalleryBinding
import com.abhishekgarala.galleryapp.viewmodel.GalleryViewModel
import kotlinx.coroutines.*
import kotlin.math.log

class GalleryActivity : AppCompatActivity() {

    lateinit var binding: ActivityGalleryBinding
    lateinit var galleryViewModel: GalleryViewModel


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        galleryViewModel = ViewModelProvider(this@GalleryActivity).get(GalleryViewModel::class.java)

        if (galleryViewModel.checkInternetConnection()) {

            binding.textView.text = "Fetching Data From Internet"
            binding.textView.setTextColor(Color.rgb(255, 0, 0));

            galleryViewModel.getPhotoList(binding.recyclerView)

            binding.recyclerView.visibility = View.VISIBLE
            binding.textView.visibility = View.GONE
            binding.progressBar.visibility = View.GONE

            /* CoroutineScope(Dispatchers.IO).launch {
                 galleryViewModel.insert()
             }*/

            Log.d("TAG", "onCreate1: ${galleryViewModel.read()}")

        } else {

            CoroutineScope(Dispatchers.IO).launch {

                withContext(Dispatchers.Main) {
                    binding.textView.text = "Fetching Data From Datbase"
                    binding.textView.setTextColor(Color.rgb(255, 0, 0));
                }
                val list = galleryViewModel.read()

                withContext(Dispatchers.Main) {
                    binding.textView.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.recyclerView.adapter = PhotoRecyclerView(this@GalleryActivity, list)
                }
            }
        }

    }
}