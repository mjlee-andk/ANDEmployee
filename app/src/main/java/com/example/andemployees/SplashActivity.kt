package com.example.andemployees

import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import com.pixplicity.easyprefs.library.Prefs

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        val mUserId = Prefs.getString(getString(R.string.PREF_USER_ID), null)
        if(mUserId.isNullOrEmpty()) {
            val intent = Intent(application, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            val intent = Intent(application, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {

    }
}
