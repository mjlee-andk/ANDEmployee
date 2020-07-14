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

class EmployeeDetailActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_detail)
        setSupportActionBar(findViewById(R.id.toolbar))

        val mIntent = intent
        //TODO id값들 서버에서 받아온것으로 바꾸기
        val mEmployeeId = mIntent.getStringExtra("employeeId")
//        val mEmployeeId = "62cc7cb0-c1ff-11ea-9982-20cf305809b8"
        val mUserId = "d204d659-c1e2-11ea-9982-20cf305809b8";

        findViewById<Button>(R.id.button_memo).setOnClickListener {
            val intent = Intent(this, MemoActivity::class.java)
            intent.putExtra("employeeId", mEmployeeId)
            intent.putExtra("userId", mUserId)
            startActivity(intent)
        }

        val mEmployeeProfile = findViewById<ImageView>(R.id.iv_employee_profile)
        mEmployeeProfile.background = ShapeDrawable(OvalShape())
        mEmployeeProfile.clipToOutline = true

        val mEmployeeName = findViewById<TextView>(R.id.tv_employee_name)
        val mEmployeeDepartment = findViewById<TextView>(R.id.tv_employee_department)
        val mEmployeePosition = findViewById<TextView>(R.id.tv_employee_position)
        val mEmployeeExtensionNum = findViewById<TextView>(R.id.tv_employee_extension_num)
        val mEmployeePhoneNum = findViewById<TextView>(R.id.tv_employee_phone_num)
        val mEmployeeBirthdate = findViewById<TextView>(R.id.tv_employee_birthdate)
        val mEmployeeJoindate = findViewById<TextView>(R.id.tv_employee_joindate)

        val api = RetrofitAPI.create()
        if (mEmployeeId != null) {
            api.getEmployee(mEmployeeId).enqueue(object: Callback<Result.ResultEmployee> {
                override fun onResponse(
                    call: Call<Result.ResultEmployee>,
                    response: Response<Result.ResultEmployee>
                ) {
                    var mCode = response.body()?.code
                    var mMessage = response.body()?.message
                    var mData = response.body()?.data

//                    Toast.makeText(this@EmployeeDetailActivity, mMessage, Toast.LENGTH_SHORT).show()

                    if(mCode == 200) {
                        mEmployeeName.text = mData?.name
                        mEmployeeDepartment.text = mData?.department_name
                        mEmployeePosition.text = mData?.position_name
                        mEmployeeExtensionNum.text = mData?.extension_number
                        mEmployeePhoneNum.text = mData?.phone
                        mEmployeeBirthdate.text = mData?.birth
                        mEmployeeJoindate.text = mData?.join_date

                        if(mData?.profile_img == null) {

                            Glide.with(this@EmployeeDetailActivity).load(getString(R.string.basic_profile_url)).into(mEmployeeProfile)
                        }
                        else {
                            Glide.with(this@EmployeeDetailActivity).load(mData?.profile_img).into(mEmployeeProfile)
                        }
                    }
                }

                override fun onFailure(call: Call<Result.ResultEmployee>, t: Throwable) {
                    Toast.makeText(this@EmployeeDetailActivity, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}