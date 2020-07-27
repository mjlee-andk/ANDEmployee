package com.example.andemployees

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmployeeDetailActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_CALL_PHONE = 1000
    private val api = RetrofitAPI.create()
    private lateinit var loadingDialog: LoadingDialog

    lateinit var mEmployeeName: TextView
    lateinit var mEmployeeDepartment: TextView
    lateinit var mEmployeePosition: TextView
    lateinit var mEmployeeExtensionNum: TextView
    lateinit var mEmployeePhoneNum: TextView
    lateinit var mEmployeeBirthdate: TextView
    lateinit var mEmployeeJoindate: TextView
    lateinit var mEmployeeProfile: ImageView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_detail)
//        setSupportActionBar(findViewById(R.id.toolbar))

        loadingDialog = LoadingDialog(this@EmployeeDetailActivity)

        val mIntent = intent
        //TODO user id값 로그인한 계정으로 바꿀것
        val mEmployeeId = mIntent.getStringExtra("employeeId").toString()
        val mUserId = "d204d659-c1e2-11ea-9982-20cf305809b8";

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val intent = Intent(this, MemoActivity::class.java)
            intent.putExtra("employeeId", mEmployeeId)
            intent.putExtra("userId", mUserId)
            startActivity(intent)
        }

        mEmployeeProfile = findViewById(R.id.iv_employee_profile)
        mEmployeeProfile.background = ShapeDrawable(OvalShape())
        mEmployeeProfile.clipToOutline = true

        mEmployeeName = findViewById(R.id.tv_employee_name)
        mEmployeeDepartment = findViewById(R.id.tv_employee_department)
        mEmployeePosition = findViewById(R.id.tv_employee_position)
        mEmployeeExtensionNum = findViewById(R.id.tv_employee_extension_num)
        mEmployeePhoneNum = findViewById(R.id.tv_employee_phone_num)
        mEmployeeBirthdate = findViewById(R.id.tv_employee_birthdate)
        mEmployeeJoindate = findViewById(R.id.tv_employee_joindate)

        if (mEmployeeId != null) {
            getEmployee(mEmployeeId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.dismiss()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            MY_PERMISSIONS_REQUEST_CALL_PHONE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.permission_call_ok), Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, getString(R.string.permission_call_fail), Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun getEmployee(employeeId: String) {
        loadingDialog.show()
        api.getEmployee(employeeId).enqueue(object: Callback<Result.ResultEmployee> {
            override fun onResponse(
                call: Call<Result.ResultEmployee>,
                response: Response<Result.ResultEmployee>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message
                var mData = response.body()?.data

                loadingDialog.dismiss()

                if(mCode == 200) {
                    mEmployeeName.text = mData?.name
                    mEmployeeDepartment.text = mData?.department_name
                    mEmployeePosition.text = mData?.position_name
                    mEmployeeExtensionNum.text = mData?.extension_number
                    mEmployeePhoneNum.text = mData?.phone
                    mEmployeeBirthdate.text = mData?.birth
                    mEmployeeJoindate.text = mData?.join_date

                    mEmployeePhoneNum.setOnClickListener {
                        try {
                            val permissionCheck = ContextCompat.checkSelfPermission(this@EmployeeDetailActivity, CALL_PHONE)
                            if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this@EmployeeDetailActivity, arrayOf(CALL_PHONE), MY_PERMISSIONS_REQUEST_CALL_PHONE)
                            }
                            else {
                                var phone = mEmployeePhoneNum.text.toString()
                                if(phone.isNullOrEmpty()){
                                    return@setOnClickListener
                                }
                                phone = phone.replace("-", "")

                                val intent = Intent(Intent.ACTION_CALL)
                                intent.data = Uri.parse("tel:$phone")
                                startActivity(intent)
                                return@setOnClickListener
                            }
                        }
                        catch (e: Exception) {
                            e.printStackTrace()
                            return@setOnClickListener
                        }
                    }

                    mEmployeeExtensionNum.setOnClickListener {
                        try {
                            val permissionCheck = ContextCompat.checkSelfPermission(this@EmployeeDetailActivity, CALL_PHONE)
                            if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this@EmployeeDetailActivity, arrayOf(CALL_PHONE), MY_PERMISSIONS_REQUEST_CALL_PHONE)
                            }
                            else {
                                var extensionNum = mEmployeeExtensionNum.text.toString()
                                if(extensionNum.isNullOrEmpty()){
                                    return@setOnClickListener
                                }

                                extensionNum = "07046725$extensionNum"

                                val intent = Intent(Intent.ACTION_CALL)
                                intent.data = Uri.parse("tel:$extensionNum")
                                startActivity(intent)
                                return@setOnClickListener
                            }
                        }
                        catch (e: Exception) {
                            e.printStackTrace()
                            return@setOnClickListener
                        }
                    }

                    if(mData?.profile_img == null) {
                        Glide.with(this@EmployeeDetailActivity).load(getString(R.string.basic_profile_url)).into(mEmployeeProfile)
                    }
                    else {
                        Glide.with(this@EmployeeDetailActivity).load(mData?.profile_img).into(mEmployeeProfile)
                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultEmployee>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@EmployeeDetailActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }
}