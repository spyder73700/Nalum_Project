package com.example.nalum

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        // Play Animation Programmatically
        lottieAnimationView.setAnimation(R.raw.sun) // Replace with your animation file
        lottieAnimationView.playAnimation()

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

        val emailField: EditText = findViewById(R.id.email)
        val passwordField: EditText = findViewById(R.id.password_reg)
        val nameField: EditText = findViewById(R.id.username)
        val rollnumberField: EditText = findViewById(R.id.rollnumber)
        val phonenumberField: EditText = findViewById(R.id.phonenumber)
        val companynameField: EditText = findViewById(R.id.companyname)
        val designationField: EditText = findViewById(R.id.designation)
        val registerButton = findViewById<Button>(R.id.Registerbutton)
        Log.d("fire", "done")
        registerButton.setOnClickListener {
            Log.d("fire", "buttondone")
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val name = nameField.text.toString()
            val rollnumber = rollnumberField.text.toString()
            val phonenumber = phonenumberField.text.toString()
            val companyname = companynameField.text.toString()
            val designation = designationField.text.toString()
            val hash = hashMapOf(
                "email" to email,
                "name" to name,
                "rollnumber" to rollnumber,
                 "company" to companyname,
                "designation" to designation,
                "phonenumber" to phonenumber
            )
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                       // Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(
                            baseContext,
                            "ACCOUNT CREATED.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val intent = Intent(this, Home::class.java) // Replace 'Home' with your target activity
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.Email Exist \n try again"
                            ,
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }
            firestore.collection("users").document(email).set(hash)
                .addOnSuccessListener {
               //     Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    emailField.text.clear()

                    nameField.text.clear()
                    rollnumberField.text.clear()
                    phonenumberField.text.clear()

                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)


                }
                .addOnFailureListener {
                 //   Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
