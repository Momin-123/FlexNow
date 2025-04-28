package com.momin.l226955.flexnow

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    private var isCreatePasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val firstNameEditText = findViewById<EditText>(R.id.firstNameEditText)
        val lastNameEditText = findViewById<EditText>(R.id.lastNameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val createPasswordEditText = findViewById<EditText>(R.id.createPasswordEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val ageEditText = findViewById<EditText>(R.id.ageEditText)
        val heightEditText = findViewById<EditText>(R.id.heightEditText)
        val weightEditText = findViewById<EditText>(R.id.weightEditText)
        val signupButton = findViewById<Button>(R.id.signupButton)
        val loginLink = findViewById<TextView>(R.id.loginLink)
        val createEyeIcon = findViewById<ImageView>(R.id.createEyeIcon)
        val confirmEyeIcon = findViewById<ImageView>(R.id.confirmEyeIcon)

        createEyeIcon.setOnClickListener {
            isCreatePasswordVisible = !isCreatePasswordVisible
            if (isCreatePasswordVisible) {
                createPasswordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                createEyeIcon.setImageResource(R.drawable.eye)
            } else {
                createPasswordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                createEyeIcon.setImageResource(R.drawable.eyeclose)
            }
            createPasswordEditText.setSelection(createPasswordEditText.text.length)
        }

        confirmEyeIcon.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            if (isConfirmPasswordVisible) {
                confirmPasswordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                confirmEyeIcon.setImageResource(R.drawable.eye)
            } else {
                confirmPasswordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                confirmEyeIcon.setImageResource(R.drawable.eyeclose)
            }
            confirmPasswordEditText.setSelection(confirmPasswordEditText.text.length)
        }

        signupButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val username = usernameEditText.text.toString()
            val createPassword = createPasswordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val age = ageEditText.text.toString()
            val height = heightEditText.text.toString()
            val weight = weightEditText.text.toString()

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() ||
                createPassword.isEmpty() || confirmPassword.isEmpty() ||
                age.isEmpty() || height.isEmpty() || weight.isEmpty()) {

                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (createPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Signed up successfully", Toast.LENGTH_SHORT).show()
                // Proceed to next screen if needed
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        loginLink.setOnClickListener {
            finish() // Finish signup and return to LoginActivity
        }
    }
}
