package com.abhishekgarala.galleryapp.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.abhishekgarala.galleryapp.database.AppDao
import com.abhishekgarala.galleryapp.database.AppDatabase
import com.abhishekgarala.galleryapp.model.Hit
import com.abhishekgarala.galleryapp.retrofit.ApiInterface
import com.abhishekgarala.galleryapp.retrofit.RetrofitClient
import com.abhishekgarala.galleryapp.view.GalleryActivity
import com.abhishekgarala.galleryapp.view.PhotoRecyclerView
import kotlinx.coroutines.*

@SuppressLint("StaticFieldLeak")
class GalleryViewModel(application: Application) : AndroidViewModel(
    application
) {

    private val dao = AppDatabase.getDatabase(application.applicationContext).appDao()

    val context = application.applicationContext

    var list: List<Hit>? = null

    var flag = false

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkInternetConnection(): Boolean {
        var isAvailable: Boolean = false

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        isAvailable = capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))

        return isAvailable
    }

    fun getPhotoList(rv: RecyclerView) {

        if (flag){

            viewModelScope.launch(Dispatchers.IO) {

                val list = read()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "database", Toast.LENGTH_SHORT).show()
                    rv.adapter = PhotoRecyclerView(context, list)
                }

            }

        }
        else{

            val retrofit = RetrofitClient.getInstance()
            val apiInterface = retrofit.create(ApiInterface::class.java)

            //flag = true

            viewModelScope.launch(Dispatchers.IO) {
                try {

                    val response = apiInterface.getPhotoList()
                    if (response.isSuccessful()) {

                        Log.e("TAG", "onCreate: ${response.body()?.hits?.size}")
                        withContext(Dispatchers.Main) {

                            rv.adapter = PhotoRecyclerView(
                                context,
                                response.body()!!.hits
                            )

                            list = response.body()!!.hits

                        }
                        list?.forEach {
                            dao.insert(it)
                        }

                    } else {
                        Toast.makeText(
                            context,
                            response.errorBody().toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (Ex: Exception) {
                    Ex.localizedMessage?.let { Log.e("Error", it) }
                }


            }
        }


    }

/*    fun getPhotoList(rv: RecyclerView) {

        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)

        viewModelScope.launch(Dispatchers.IO) {

            val response = apiInterface.getPhotoList()
            if (response.isSuccessful()) {

                Log.e("TAG", "onCreate: ${response.body()?.hits?.size}")

                rv.adapter = PhotoRecyclerView(
                    context,
                    response.body()!!.hits
                )

                list = response.body()!!.hits
                Log.d("TAG", "getPhotoList: $list")

                list?.forEach {
                    dao.insert(it)
                }

            }

        }


    }*/

    fun read(): List<Hit> {
        return dao.read()
    }

}