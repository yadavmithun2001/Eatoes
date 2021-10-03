package com.example.eatoes

import android.app.DownloadManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.androidgamesdk.gametextinput.Listener
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"//Email Pattern for Validation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
                                                                                //declaring editText and Login button
        val edittext_email = findViewById<EditText>(R.id.editText_email)
        val edittext_password = findViewById<EditText>(R.id.editText_password)
        val btn_login = findViewById<Button>(R.id.btn_login)


        btn_login?.setOnClickListener(){
            if(edittext_email.text.isEmpty() && edittext_password.text.isEmpty()){
                Toast.makeText(this,"Field Cannot be Empty",Toast.LENGTH_SHORT).show()
            }else {    // if TextFields are empty
                if(edittext_email.text.matches(emailPattern.toRegex())){   //Email Validation
                    if(edittext_password.length() < 6){   // Showing message for invalid Password
                        Toast.makeText(this,"Password must contain least 6 Characters",Toast.LENGTH_SHORT).show()
                    }else{    //Login successful

                        try {
                         Postdata(edittext_email.text.toString(),edittext_password.text.toString())
                            intent = Intent(this@MainActivity,Home_Page::class.java)
                            intent.putExtra("useremail",edittext_email.text.toString())
                            startActivity(intent)

                        }catch (e:Exception){
                            Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{  // Showing message for Invalid Email
                    Toast.makeText(this,"Invalid Email !",Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    fun Postdata(email:String,password:String) {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://reqres.in/api/login/")
            .build()

        // Create Service
        val service = retrofit.create(ApiService::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("password", password)

        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.createUser(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string()
                        )
                    )
                    Toast.makeText(this@MainActivity,"Account created Succesfully",Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@MainActivity,prettyJson.toString(),Toast.LENGTH_LONG).show()

                } else {

                    Toast.makeText(this@MainActivity,response.code(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}