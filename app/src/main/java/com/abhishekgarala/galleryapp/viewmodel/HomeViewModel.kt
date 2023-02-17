package com.abhishekgarala.galleryapp.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var context = application.applicationContext

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

    fun googleSignIn(): GoogleSignInClient {

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, googleSignInOptions)
    }

    fun googleSignOut(image:ImageView, email:TextView, name:TextView) {

        googleSignIn().signOut().addOnSuccessListener {

            image.visibility = View.GONE
            email.visibility = View.GONE
            name.visibility = View.GONE

            Toast.makeText(context, "SignOut", Toast.LENGTH_SHORT).show()

        }

    }

    @SuppressLint("SetTextI18n")
    fun lastSignedInAccount(image:ImageView, email:TextView, name:TextView) {
        val lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(context)
        if (lastSignedInAccount != null) {
            Toast.makeText(context, lastSignedInAccount.email.toString(), Toast.LENGTH_SHORT).show()

            image.visibility = View.VISIBLE
            Glide.with(context)
                .load(lastSignedInAccount.photoUrl)
                .into(image)

            email.visibility = View.VISIBLE
            email.text = "Email : " + lastSignedInAccount.email

            name.visibility = View.VISIBLE
            name.text = "Name : " + lastSignedInAccount.displayName

        }
    }

}