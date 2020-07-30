package com.example.andemployees

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageDetailActivity : Activity() {
    private lateinit var mImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        val mIntent = intent
        val mProfileImage = mIntent.getStringExtra(getString(R.string.PROFILE_URL)).toString()

        mImageView = findViewById(R.id.iv_image_detail)
        Glide.with(this).load(mProfileImage).into(mImageView)

        findViewById<Button>(R.id.btn_image_detail_back).setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}


