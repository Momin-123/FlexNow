package com.momin.l226955.flexnow

import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val logoImageView = findViewById<ImageView>(R.id.logoImageView)
        val signInTitle = findViewById<TextView>(R.id.signInTitle)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val rememberMeCheckBox = findViewById<CheckBox>(R.id.rememberMeCheckBox)
        val forgotPasswordText = findViewById<TextView>(R.id.forgotPasswordText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpLink = findViewById<TextView>(R.id.signUpLink)
        val eyeIcon = findViewById<ImageView>(R.id.eye)

        // Password visibility toggle
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

        // Login button click
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
            }
        }

        // Sign up link click
        signUpLink.setOnClickListener {
            Toast.makeText(this, "Redirect to sign up page", Toast.LENGTH_SHORT).show()
        }
    }
}
