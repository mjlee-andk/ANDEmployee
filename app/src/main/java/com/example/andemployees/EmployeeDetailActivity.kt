package com.example.andemployees

import android.Manifest.permission.CALL_PHONE
import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
import com.pixplicity.easyprefs.library.Prefs
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
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        loadingDialog = LoadingDialog(this@EmployeeDetailActivity)


        val mIntent = intent
        val mEmployeeId = mIntent.getStringExtra(getString(R.string.EMPLOYEE_ID)).toString()
        val mUserId = Prefs.getString(getString(R.string.PREF_USER_ID), null)

        // 메모 추가/수정 버튼
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            getMemo(mUserId, mEmployeeId)
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

                                extensionNum = getString(R.string.extension_num_header) + extensionNum

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
                        mEmployeeProfile.setOnClickListener{
                            val intent = Intent(this@EmployeeDetailActivity, ImageDetailActivity::class.java)
                            intent.putExtra(getString(R.string.PROFILE_URL), mData?.profile_img)
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultEmployee>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@EmployeeDetailActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 메모가 있는지 확인하기 위해 호출함
    private fun getMemo(userId: String, employeeId: String) {
        loadingDialog.show()
        api.getMemo(userId, employeeId).enqueue(object: Callback<Result.ResultMemo> {
            override fun onResponse(
                call: Call<Result.ResultMemo>,
                response: Response<Result.ResultMemo>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message
                var mData = response.body()?.data

                loadingDialog.dismiss()

                if(mCode == 200) {
                    // 메모 없으면 바로 화면 띄우기
                    if(mData?.id.isNullOrEmpty()) {
                        val intent = Intent(this@EmployeeDetailActivity, MemoActivity::class.java)
                        intent.putExtra(getString(R.string.EMPLOYEE_ID), employeeId)
                        startActivity(intent)

                    }
                    // 메모 있으면 비밀번호 입력 다이얼로그
                    else {
                        confirmMemoPassword(userId, employeeId)
                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultMemo>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@EmployeeDetailActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 메모 있을 경우 비밀번호 입력 받기 위해 호출함
    private fun confirmMemoPassword(userId: String, employeeId: String) {
        val inputMemoPasswordDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        val imm: InputMethodManager? = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val customLayout = LayoutInflater.from(this).inflate(R.layout.dialog_memo_password_input, null)
        var memoPasswordEditText = customLayout.findViewById<EditText>(R.id.et_memo_password_edit)

        inputMemoPasswordDialogBuilder.setView(customLayout)
        inputMemoPasswordDialogBuilder.setMessage(getString(R.string.hint_password))
        inputMemoPasswordDialogBuilder.setPositiveButton(getString(R.string.ok)) { _, _ ->
            // 키보드 숨기기
            imm?.toggleSoftInput(
                InputMethodManager.HIDE_IMPLICIT_ONLY, 0
            )
            loadingDialog.show()

            val password = memoPasswordEditText.text.toString()
            if(password.isNullOrEmpty()) {
                Toast.makeText(this, getString(R.string.hint_password), Toast.LENGTH_SHORT).show()
                loadingDialog.dismiss()
                return@setPositiveButton
            }
            api.confirmMemoPasssword(userId, password).enqueue(object: Callback<Result.ResultBasic> {
                override fun onResponse(
                    call: Call<Result.ResultBasic>,
                    response: Response<Result.ResultBasic>
                ) {
                    var mCode = response.body()?.code
                    var mMessage = response.body()?.message

                    loadingDialog.dismiss()

                    if(mCode == 200) {
                        val intent = Intent(this@EmployeeDetailActivity, MemoActivity::class.java)
                        intent.putExtra(getString(R.string.EMPLOYEE_ID), employeeId)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this@EmployeeDetailActivity, getString(R.string.check_password), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
                    loadingDialog.dismiss()
                    Toast.makeText(this@EmployeeDetailActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                }
            })
        }

        inputMemoPasswordDialogBuilder.setNegativeButton(getString(R.string.close)) { _, _ ->
            // 키보드 숨기기
            imm?.toggleSoftInput(
                InputMethodManager.HIDE_IMPLICIT_ONLY, 0
            )
        }
        inputMemoPasswordDialogBuilder.show()

        // 키보드 띄우기
        imm?.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }
}