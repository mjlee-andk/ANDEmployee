package com.example.andemployees

import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import com.pixplicity.easyprefs.library.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {
    private val api = RetrofitAPI.create()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        loadingDialog = LoadingDialog(this@ChangePasswordActivity)

        val password = findViewById<EditText>(R.id.password)
        val passwordConfirm = findViewById<EditText>(R.id.password_confirm)
        val changePassword = findViewById<Button>(R.id.change_password)
//        val loading = findViewById<ProgressBar>(R.id.loading)

        val mUserId = Prefs.getString(getString(R.string.PREF_USER_ID), null)

        changePassword.setOnClickListener {
            var mPassword = password.text.toString()
            var mPasswordConfirm = passwordConfirm.text.toString()

            if(mPassword.isEmpty() || mPasswordConfirm.isEmpty()) {
                Toast.makeText(this@ChangePasswordActivity, getString(R.string.hint_password_password_confirm), Toast.LENGTH_SHORT).show()
                return@setOnClickListener;
            }

            if(mPassword != mPasswordConfirm) {
                Toast.makeText(this@ChangePasswordActivity, getString(R.string.inconsistency_password), Toast.LENGTH_SHORT).show()
                return@setOnClickListener;
            }

            if (mUserId != null) {
                changePassword(mUserId, mPassword)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.dismiss()
    }

    private fun changePassword(userId: String, password:String) {
        loadingDialog.show()
        api.changePassword(userId, password).enqueue(object: Callback<Result.ResultChangePassword> {
            override fun onResponse(
                call: Call<Result.ResultChangePassword>,
                response: Response<Result.ResultChangePassword>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message

                Toast.makeText(this@ChangePasswordActivity, getString(R.string.change_password_ok), Toast.LENGTH_SHORT).show()
                loadingDialog.dismiss()

                if(mCode == 200) {
                    startActivity(Intent(this@ChangePasswordActivity, MainActivity::class.java))
                    finish()
                }
            }

            override fun onFailure(call: Call<Result.ResultChangePassword>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@ChangePasswordActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
