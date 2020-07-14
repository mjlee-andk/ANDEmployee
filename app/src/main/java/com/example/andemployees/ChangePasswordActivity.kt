package com.example.andemployees

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.andemployees.R
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_change_password)

        val password = findViewById<EditText>(R.id.password)
        val password_confirm = findViewById<EditText>(R.id.password_confirm)
        val change_password = findViewById<Button>(R.id.change_password)
        val loading = findViewById<ProgressBar>(R.id.loading)

        val mIntent = intent
        val mUserId = mIntent.getStringExtra("userId")

        change_password.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            var mPassword = password.text.toString()
            var mPasswordConfirm = password_confirm.text.toString()

            if(mPassword.isEmpty() || mPasswordConfirm.isEmpty()) {
                Toast.makeText(this@ChangePasswordActivity, "비밀번호와 비밀번호 확인을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener;
            }

            if(mPassword != mPasswordConfirm) {
                Toast.makeText(this@ChangePasswordActivity, "비밀번호가 서로 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener;
            }

            val api = RetrofitAPI.create()
            if (mUserId != null) {
                api.changePassword(mUserId, mPassword).enqueue(object: Callback<Result.ResultChangePassword> {
                    override fun onResponse(
                        call: Call<Result.ResultChangePassword>,
                        response: Response<Result.ResultChangePassword>
                    ) {
                        var mCode = response.body()?.code
                        var mMessage = response.body()?.message

                        Toast.makeText(this@ChangePasswordActivity, mMessage, Toast.LENGTH_SHORT).show()

                        if(mCode == 200) {
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<Result.ResultChangePassword>, t: Throwable) {
                        Toast.makeText(this@ChangePasswordActivity, "비밀번호 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}