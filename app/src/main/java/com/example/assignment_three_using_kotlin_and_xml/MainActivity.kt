package com.example.assignment_three_using_kotlin_and_xml

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var editTextPhone: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextPassword = findViewById(R.id.editTextPassword)
        editTextPhone = findViewById(R.id.editTextPhone)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            val phoneNumber = editTextPhone.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            fetchDataFromServer(phoneNumber, password)
        }
    }

    private fun fetchDataFromServer(whichNumber: String, password: String) {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val url = "http://192.168.29.135/membershipApp/getDetails.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val serverPassword = jsonResponse.getString("password")
                    val serverMobileNo = jsonResponse.getString("mobileNo")

                    if (serverPassword.trim() == password && serverMobileNo.trim() == whichNumber) {
                        val intent = Intent(this@MainActivity, DashboardActiviy::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Data fetching unsuccessful",
                            Toast.LENGTH_LONG
                        ).show()
                        editTextPassword.text.clear()
                        editTextPhone.text.clear()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        applicationContext,
                        "Error parsing JSON response.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, "Error: " + error.message, Toast.LENGTH_SHORT)
                    .show()
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["mobileNo"] = whichNumber
                return params
            }
        }

        queue.add(stringRequest)
    }

}


