package com.example.eatoes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit

class Home_Page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val user_email = findViewById<TextView>(R.id.user_email)       //declaring Textviews
        val user_token = findViewById<TextView>(R.id.user_token)       //declaring Textviews

        user_email.setText(intent.getStringExtra("useremail"))   //get value from Login Page
        user_token.setText(intent.getStringExtra("usertk"))      //get generated Token

        getMethod(user_token)   //calling function

    }
    fun getMethod(textView: TextView) {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://reqres.in/api/login/")
            .build()

        // Create Service
        val service = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.gettoken()

            withContext(Dispatchers.Main) {
                    textView.setText(response.message().toString()+"aOIkjekKSDS")

            }
        }
    }


}