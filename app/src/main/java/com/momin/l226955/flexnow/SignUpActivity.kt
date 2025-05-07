package com.momin.l226955.flexnow

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.momin.l226955.flexnow.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private var isCreatePasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.createEyeIcon.setOnClickListener {
            isCreatePasswordVisible = !isCreatePasswordVisible
            binding.createPasswordEditText.inputType =
                if (isCreatePasswordVisible) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            binding.createEyeIcon.setImageResource(
                if (isCreatePasswordVisible) R.drawable.eye else R.drawable.eyeclose
            )
            binding.createPasswordEditText.setSelection(binding.createPasswordEditText.text.length)
        }

        binding.confirmEyeIcon.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            binding.confirmPasswordEditText.inputType =
                if (isConfirmPasswordVisible) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            binding.confirmEyeIcon.setImageResource(
                if (isConfirmPasswordVisible) R.drawable.eye else R.drawable.eyeclose
            )
            binding.confirmPasswordEditText.setSelection(binding.confirmPasswordEditText.text.length)
        }

        binding.signupButton.setOnClickListener {
            val firstName = binding.firstNameEditText.text.toString().trim()
            val lastName = binding.lastNameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim().lowercase()
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.createPasswordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
            val age = binding.ageEditText.text.toString().trim()
            val height = binding.heightEditText.text.toString().trim()
            val weight = binding.weightEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || username.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            val user = mapOf(
                                "firstName" to firstName,
                                "lastName" to lastName,
                                "username" to username,
                                "email" to email,
                                "age" to age,
                                "height" to height,
                                "weight" to weight
                            )
                            FirebaseDatabase.getInstance().getReference("users")
                                .child(userId)
                                .setValue(user)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Error saving user data: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "Failed to get user ID", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Signup failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.loginLink.setOnClickListener {
            finish()
        }
    }
}
