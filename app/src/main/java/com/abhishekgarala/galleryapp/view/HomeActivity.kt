package com.abhishekgarala.galleryapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.abhishekgarala.galleryapp.databinding.ActivityHomeBinding
import com.abhishekgarala.galleryapp.utils.SharedPrefApp
import com.abhishekgarala.galleryapp.viewmodel.HomeViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException


class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    lateinit var homeViewModel: HomeViewModel

    var isLogIn = false

    val sharedPref = SharedPrefApp.getInstance();

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        if (homeViewModel.checkInternetConnection()) {

            //isLogIn(sharedPref.getISLogged_IN(this))

            binding.textView.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.signInButton.visibility = View.VISIBLE
            binding.btnSignOut.visibility = View.VISIBLE

            homeViewModel.lastSignedInAccount(binding.imgUser,binding.tvUserEmail,binding.tvUserName)

            binding.signInButton.setOnClickListener {

                if (isLogIn){

                    startActivity(Intent(this,GalleryActivity::class.java))
                }
                else{

                    val signInIntent: Intent = homeViewModel.googleSignIn().getSignInIntent()
                    startActivityForResult(signInIntent, 1000)
                    isLogIn = true
                }

            }

            binding.btnSignOut.setOnClickListener {
                homeViewModel.googleSignOut(binding.imgUser,binding.tvUserEmail,binding.tvUserName)
                isLogIn = false
            }

            /*if (sharedPref.getISLogged_IN(this)) {
                val NextScreen = Intent(
                    applicationContext,
                    GalleryActivity::class.java
                )
                startActivity(NextScreen)
                finish()
            } else {
                *//*intent = Intent(this, HomeActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()*//*

                binding.textView.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.signInButton.visibility = View.VISIBLE
                binding.btnSignOut.visibility = View.VISIBLE

                homeViewModel.lastSignedInAccount()

                binding.signInButton.setOnClickListener {
                    val signInIntent: Intent = homeViewModel.googleSignIn().getSignInIntent()
                    startActivityForResult(signInIntent, 1000)
                }

                binding.btnSignOut.setOnClickListener {
                    homeViewModel.googleSignOut()
                }

            }*/


        } else {

            binding.textView.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.signInButton.visibility = View.VISIBLE
            binding.btnSignOut.visibility = View.VISIBLE
            binding.textView.setTextColor(Color.rgb(255, 0, 0));
            binding.textView.text = "Check Internet Connection"

        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()

        if (homeViewModel.checkInternetConnection()) {

            homeViewModel.lastSignedInAccount(binding.imgUser,binding.tvUserEmail,binding.tvUserName)
            binding.textView.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.signInButton.visibility = View.VISIBLE
            binding.btnSignOut.visibility = View.VISIBLE

        } else {

            binding.textView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
            binding.signInButton.visibility = View.GONE
            binding.btnSignOut.visibility = View.GONE
            binding.textView.setTextColor(Color.rgb(255, 0, 0));
            binding.textView.text = "Check Internet Connection"

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                //sharedPref.saveISLogged_IN(this, false);
                startActivity(Intent(this, GalleryActivity::class.java))

            } catch (e: ApiException) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun isLogIn(t: Boolean) {
        if (t) {
            val NextScreen = Intent(
                applicationContext,
                GalleryActivity::class.java
            )
            startActivity(NextScreen)
            finish()
        } else {

            binding.textView.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.signInButton.visibility = View.VISIBLE
            binding.btnSignOut.visibility = View.VISIBLE

            homeViewModel.lastSignedInAccount(binding.imgUser,binding.tvUserEmail,binding.tvUserName)

            binding.signInButton.setOnClickListener {
                val signInIntent: Intent = homeViewModel.googleSignIn().getSignInIntent()
                startActivityForResult(signInIntent, 1000)
            }

            binding.btnSignOut.setOnClickListener {
                homeViewModel.googleSignOut(binding.imgUser,binding.tvUserEmail,binding.tvUserName)
            }

        }
    }

}
