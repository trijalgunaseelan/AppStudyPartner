package com.trijalgunaseelan.studypartner

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.trijalgunaseelan.studypartner.databinding.ActivityPinBinding

class PinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPinBinding
    private lateinit var sharedPrefs: SharedPreferences
    private val correctPin = "123456"
    private var enteredPin = ""
    private var failedAttempts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences("SecurePrefs", MODE_PRIVATE)

        setupNumberPad()
    }

    private fun setupNumberPad() {
        val buttons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9
        )

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (enteredPin.length < 6) {
                    enteredPin += index.toString()
                    updatePinDisplay()
                }
                if (enteredPin.length == 6) {
                    verifyPin()
                }
            }
        }

        binding.btnBack.setOnClickListener {
            if (enteredPin.isNotEmpty()) {
                enteredPin = enteredPin.dropLast(1)
                updatePinDisplay()
            }
        }
    }

    private fun updatePinDisplay() {
        val dots = listOf(
            binding.dot1, binding.dot2, binding.dot3,
            binding.dot4, binding.dot5, binding.dot6
        )

        dots.forEachIndexed { index, dot ->
            dot.setImageResource(if (index < enteredPin.length) R.drawable.ic_filled_dot else R.drawable.ic_empty_dot)
        }
    }

    private fun verifyPin() {
        if (enteredPin == correctPin) {
            Toast.makeText(this, "PIN Correct!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            failedAttempts++
            if (failedAttempts >= 3) {
                lockoutUser()
            } else {
                showIncorrectPinAnimation()
                enteredPin = ""
                updatePinDisplay()
            }
        }
    }

    private fun showIncorrectPinAnimation() {
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        binding.pinContainer.startAnimation(shake)
        Toast.makeText(this, "Incorrect PIN!", Toast.LENGTH_SHORT).show()
    }

    private fun lockoutUser() {
        Toast.makeText(this, "Too many attempts! Try again in 24 hours.", Toast.LENGTH_LONG).show()
        sharedPrefs.edit().putLong("lockoutTime", System.currentTimeMillis()).apply()
        binding.btn0.isEnabled = false
        binding.btn1.isEnabled = false
        binding.btn2.isEnabled = false
        binding.btn3.isEnabled = false
        binding.btn4.isEnabled = false
        binding.btn5.isEnabled = false
        binding.btn6.isEnabled = false
        binding.btn7.isEnabled = false
        binding.btn8.isEnabled = false
        binding.btn9.isEnabled = false
        binding.btnBack.isEnabled = false
    }
}