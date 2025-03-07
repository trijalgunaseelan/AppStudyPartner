package com.trijalgunaseelan.studypartner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPin: EditText
    private lateinit var etConfirmPin: EditText
    private lateinit var btnSignUp: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvSignIn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        if (sharedPref.contains("user_pin")) {
            startActivity(Intent(this, PinActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_sign_up)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPin = findViewById(R.id.etPin)
        etConfirmPin = findViewById(R.id.etConfirmPin)
        btnSignUp = findViewById(R.id.btnSignUp)
        progressBar = findViewById(R.id.progressBar)
        tvSignIn = findViewById(R.id.tvSignIn)

        val pinFilters = arrayOf(InputFilter.LengthFilter(6))
        etPin.filters = pinFilters
        etPin.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        etConfirmPin.filters = pinFilters
        etConfirmPin.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        btnSignUp.setOnClickListener {
            registerUser()
        }

        tvSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val pin = etPin.text.toString().trim()
        val confirmPin = etConfirmPin.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || pin.isEmpty() || confirmPin.isEmpty()) {
            showToast("All fields are required!")
            return
        }

        if (pin.length != 6) {
            showToast("PIN must be exactly 6 digits!")
            return
        }

        if (pin != confirmPin) {
            showToast("PIN do not match!")
            return
        }

        progressBar.visibility = View.VISIBLE
        btnSignUp.isEnabled = false

        progressBar.postDelayed({
            savePin(pin)
            showToast("Account Created Successfully!")
            startActivity(Intent(this, PinActivity::class.java))
            finish()
        }, 2000)
    }

    private fun savePin(pin: String) {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putString("user_pin", pin).apply()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}