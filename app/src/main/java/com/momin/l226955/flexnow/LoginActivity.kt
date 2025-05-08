package com.momin.l226955.flexnow

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.momin.l226955.flexnow.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val eyeIcon = findViewById<ImageView>(R.id.eye)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)

        // Toggle password visibility
        eyeIcon.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                eyeIcon.setImageResource(R.drawable.eye) // eye open image
            } else {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                eyeIcon.setImageResource(R.drawable.eyeclose) // eye closed image
            }
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        // Handle login
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim().lowercase()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            // Check if user is in 'trainers' or 'users' in Realtime Database
                            FirebaseDatabase.getInstance().getReference("trainers").child(userId).get()
                                .addOnSuccessListener { snapshot ->
                                    if (snapshot.exists()) {
                                        // User is a trainer, navigate to TrainerPortal
                                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                                        val intent = Intent(this, TrainerPortal::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        // User is a regular user, navigate to MainActivity
                                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Error checking user role", Toast.LENGTH_SHORT).show()
                                    Log.e("FIREBASE_LOGIN", "Error: ${it.message}")
                                }
                        }
                    } else {
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        Log.e("FIREBASE_LOGIN", "Error: ${task.exception}")
                    }
                }
        }

        // Go to Sign Up screen
        binding.signUpLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
