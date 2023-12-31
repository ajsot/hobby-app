package com.example.hobbyapp.LoginActivity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.hobbyapp.CreateUserActivity.CreateUserActivity
import com.example.hobbyapp.HomeActivity.HomeActivity
import com.example.hobbyapp.R
import com.example.hobbyapp.Util.UserApplication
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LoginActivity : AppCompatActivity() {
    private lateinit var signupBtn:TextView
    private lateinit var loginBtn:TextView
    private lateinit var email:EditText
    private lateinit var password:EditText
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModel.LoginViewModelFactory((application as UserApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        //initializes variables
        signupBtn = findViewById(R.id.input_signup)
        loginBtn = findViewById(R.id.input_submit)
        email = findViewById(R.id.input_email)
        password = findViewById(R.id.input_password)
        //onclick listener to create new user
        signupBtn.setOnClickListener {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }

        //onclick listener to login in
        loginBtn.setOnClickListener {
            //check if email exist in database
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            loginViewModel.checkUserExistsByEmailAndPassword(emailText, passwordText)
        }

        //checks if user exists in data based on email/password
        loginViewModel.loginResult.observe(this) { user ->
            // User successfully logged in, handle the success
            if (user != null) {
                Log.d("LoginActivity", "User logged in successfully: User ID --> ${user.id}")
                //Goes to HomeActivity if successful
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("USER_ID", user.id) //sends user id to HomeActivity
                startActivity(intent)
            } else {
                // Login failed, handle the failure
                Log.d("LoginActivity", "Login failed: User not found")
            }
        }
    }
}