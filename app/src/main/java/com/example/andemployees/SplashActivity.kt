package com.example.andemployees

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat.startActivity

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        val intent = Intent(application, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}
