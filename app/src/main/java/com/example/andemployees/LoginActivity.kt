package com.example.andemployees

import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.andemployees.api.RetrofitAPI
import com.google.firebase.iid.FirebaseInstanceId
import com.pixplicity.easyprefs.library.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : Activity() {
    private val api = RetrofitAPI.create()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadingDialog = LoadingDialog(this@LoginActivity)

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        val account = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
//        val loading = findViewById<ProgressBar>(R.id.loading)

        login.setOnClickListener {
            var mAccount = account.text.toString()
            var mPassword = password.text.toString()

            if(mAccount.isEmpty() || mPassword.isEmpty()) {
                Toast.makeText(this@LoginActivity, getString(R.string.hint_id_password), Toast.LENGTH_SHORT).show()
                return@setOnClickListener;
            }

            login(mAccount, mPassword)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.dismiss()
    }

    private fun login(account:String, password:String) {
        loadingDialog.show()

        // 푸시알람 보내기 위한 디바이스토큰 받아오는 부분
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                if(!it.isSuccessful) {
                    Log.w("FCM Log", "getInstanceId failed", it.exception)
                    return@addOnCompleteListener
                }
                val token = it.result?.token
                Log.d("FCM Log", "FCM 토큰 $token")

                api.login(account, password, token).enqueue(object: Callback<com.example.andemployees.models.Result.ResultLogin> {
                    override fun onResponse(
                        call: Call<com.example.andemployees.models.Result.ResultLogin>,
                        response: Response<com.example.andemployees.models.Result.ResultLogin>
                    ) {
                        var mCode = response.body()?.code
                        var mMessage = response.body()?.message
                        var mData = response.body()?.data

                        loadingDialog.dismiss()

                        // 로그인 성공
                        if(mCode == 200) {
                            Prefs.putString(getString(R.string.PREF_USER_ID), mData?.id)

                            // 첫 로그인 시 비밀번호 변경 화면으로 이동
                            if(mData?.is_valid == 0) {
                                Intent(this@LoginActivity, ChangePasswordActivity::class.java)
                                startActivity(Intent(this@LoginActivity, ChangePasswordActivity::class.java))
                                finish()
                            }

                            // 메인 화면으로 이동
                            else {
                                Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            }
                        }
                        else {
                            Toast.makeText(this@LoginActivity, getString(R.string.check_password), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<com.example.andemployees.models.Result.ResultLogin>, t: Throwable) {
                        loadingDialog.dismiss()
                        Toast.makeText(this@LoginActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                    }
                })
            }
    }
}
