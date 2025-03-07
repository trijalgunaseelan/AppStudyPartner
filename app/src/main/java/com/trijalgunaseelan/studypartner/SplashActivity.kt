package com.trijalgunaseelan.studypartner

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val logoImageView = findViewById<ImageView>(R.id.logoImageView)

        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 1000
            fillAfter = true
        }

        val scaleUp = ScaleAnimation(
            0.8f, 1f, 0.8f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 1000
            fillAfter = true
        }

        logoImageView.startAnimation(fadeIn)
        logoImageView.startAnimation(scaleUp)

        logoImageView.postDelayed({
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }, 4000)
    }
}