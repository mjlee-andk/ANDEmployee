package com.example.andemployees

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.andemployees.api.RetrofitAPI
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

        val account = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
//        val loading = findViewById<ProgressBar>(R.id.loading)

        login.setOnClickListener {
            var mAccount = account.text.toString()
            var mPassword = password.text.toString()

            if(mAccount.isEmpty() || mPassword.isEmpty()) {
                Toast.makeText(this@LoginActivity, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
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
        api.login(account, password).enqueue(object: Callback<com.example.andemployees.models.Result.ResultLogin> {
            override fun onResponse(
                call: Call<com.example.andemployees.models.Result.ResultLogin>,
                response: Response<com.example.andemployees.models.Result.ResultLogin>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message
                var mData = response.body()?.data

                loadingDialog.dismiss()

//                Toast.makeText(this@LoginActivity, mMessage, Toast.LENGTH_SHORT).show()

                if(mCode == 200) {

                    if(mData?.is_valid == 0) {
                        Intent(this@LoginActivity, ChangePasswordActivity::class.java).putExtra("userId", mData?.id)
                        startActivity(Intent(this@LoginActivity, ChangePasswordActivity::class.java))
                        finish()
                    }

                    else {
                        Intent(this@LoginActivity, MainActivity::class.java).putExtra("userId", mData?.id)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<com.example.andemployees.models.Result.ResultLogin>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@LoginActivity, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
