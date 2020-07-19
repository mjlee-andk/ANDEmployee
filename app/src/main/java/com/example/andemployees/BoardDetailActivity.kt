package com.example.andemployees

import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

//        val mIntent = intent
//        val mEmployeeId = mIntent.getStringExtra("employeeId")
//        val mUserId = "d204d659-c1e2-11ea-9982-20cf305809b8";



    }
}