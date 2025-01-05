package com.example.nalum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        // Play Animation Programmatically
        lottieAnimationView.setAnimation(R.raw.sun_login2) // Replace with your animation file
        lottieAnimationView.playAnimation()
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Get UI elements
        val emailField: EditText = findViewById(R.id.login_email)
        val passwordField: EditText = findViewById(R.id.login_password)
        val loginButton: Button = findViewById(R.id.login)
        val registerButton: Button = findViewById(R.id.button)

        // Login Button Click Listener
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            // Validate input fields
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase Login
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Login successful
                        Log.d("Login", "signInWithEmail:success")
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                        // Navigate to Home or Dashboard Activity
                        val intent = Intent(this, Home::class.java) // Replace 'Home' with your target activity
                        startActivity(intent)
                        finish() // Finish login activity so user can't go back to it by pressing back button

                    } else {
                        // Login failed
                        Log.w("Login", "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Redirect to Register Activity
        registerButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}
