package com.trijalgunaseelan.studypartner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PinActivity : AppCompatActivity() {

    private lateinit var pinFields: Array<EditText>
    private val sharedPref by lazy { getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        pinFields = arrayOf(
            findViewById(R.id.pin1),
            findViewById(R.id.pin2),
            findViewById(R.id.pin3),
            findViewById(R.id.pin4),
            findViewById(R.id.pin5),
            findViewById(R.id.pin6)
        )

        setupPinInputs()
    }

    private fun setupPinInputs() {
        for (i in pinFields.indices) {
            pinFields[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        if (i < pinFields.size - 1) {
                            pinFields[i + 1].requestFocus()  // Move to the next input
                        } else {
                            validatePin()  // Auto-submit on last digit
                        }
                    } else if (s?.isEmpty() == true && i > 0) {
                        pinFields[i - 1].requestFocus()  // Move back on delete
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun validatePin() {
        val enteredPin = pinFields.joinToString("") { it.text.toString() }
        val savedPin = sharedPref.getString("user_pin", null)

        if (savedPin != null && enteredPin == savedPin) {
            Toast.makeText(this, "Enjoy Learning!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Server is Busy!", Toast.LENGTH_SHORT).show()
            clearPinFields()
        }
    }

    private fun clearPinFields() {
        for (editText in pinFields) {
            editText.text.clear()
        }
        pinFields[0].requestFocus()
    }
}