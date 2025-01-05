package com.example.nalum

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Userprofile : AppCompatActivity() {

    // Declare variables
    private lateinit var passwordField: EditText
    private lateinit var rollnumberField: EditText
    private lateinit var phonenumberField: EditText
    private lateinit var companynameField: EditText
    private lateinit var designationField: EditText
    private lateinit var registerButton: Button
    private lateinit var username: TextView
    private lateinit var number: TextView
    private lateinit var db: FirebaseFirestore
    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userprofile)

        // Initialize Firestore and UI elements
        db = FirebaseFirestore.getInstance()
        passwordField = findViewById(R.id.passwordupdate)
        rollnumberField = findViewById(R.id.rollnumberupdate)
        phonenumberField = findViewById(R.id.phoneupdate)
        companynameField = findViewById(R.id.companynameupdate)
        designationField = findViewById(R.id.designationupdate)
        registerButton = findViewById(R.id.updatebutton)
        username = findViewById(R.id.user_name)
        number = findViewById(R.id.rollno)

        // Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Fetch current user
        val user = FirebaseAuth.getInstance().currentUser
        userEmail = user?.email

        if (userEmail != null) {
            fetchUserData(userEmail!!)
        }

        // Update button click listener
        registerButton.setOnClickListener {
            if (validateFields()) {
                updateUserData()
            }
        }
    }

    // Fetch user data from Firestore
    private fun fetchUserData(email: String) {
        val userRef = db.collection("users").document(email)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    rollnumberField.setText(document.getString("rollnumber"))
                    phonenumberField.setText(document.getString("phonenumber"))
                    companynameField.setText(document.getString("company"))
                    designationField.setText(document.getString("designation"))
                    username.setText(document.getString("name"))
                   number.setText(document.getString("rollno"))
                } else {
                    Toast.makeText(this, "No Data Found!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to fetch data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    // Update user data in Firestore
    private fun updateUserData() {
        val userRef = db.collection("users").document(userEmail!!)

        val updates = hashMapOf(
            "rollnumber" to rollnumberField.text.toString(),
            "phonenumber" to phonenumberField.text.toString(),
            "company" to companynameField.text.toString(),
            "designation" to designationField.text.toString()
        )

        // Update Firestore data
        userRef.update(updates as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(this, "Update Successful!", Toast.LENGTH_SHORT).show()
                Log.d("Firestore", "Document successfully updated!")
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Update Failed: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("Firestore", "Error updating document", e)
            }

        // Update password in Firebase Authentication
        val newPassword = passwordField.text.toString()
        val user = FirebaseAuth.getInstance().currentUser

        if (newPassword.isNotEmpty()) {
            user?.updatePassword(newPassword)
                ?.addOnSuccessListener {
                    Toast.makeText(this, "Password Updated Successfully!", Toast.LENGTH_SHORT).show()
                }
                ?.addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to update password: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    // Validate input fields
    private fun validateFields(): Boolean {
        if (rollnumberField.text.isEmpty() || phonenumberField.text.isEmpty() ||
            companynameField.text.isEmpty() || designationField.text.isEmpty()
        ) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
