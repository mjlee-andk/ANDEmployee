package com.example.andemployees

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemoActivity : AppCompatActivity() {

    private val api = RetrofitAPI.create()
    private lateinit var loadingDialog: LoadingDialog

    private lateinit var mMemoId: String
    private lateinit var mMemoText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)
//        setSupportActionBar(findViewById(R.id.toolbar))

        loadingDialog = LoadingDialog(this@MemoActivity)

        val mIntent = intent
        val mUserId = mIntent.getStringExtra("userId")
        val mEmployeeId = mIntent.getStringExtra("employeeId")

        mMemoText = findViewById(R.id.et_memo)

        // 기존에 작성한 메모 있는지 검색
        if (mEmployeeId != null && mUserId != null) {
            getMemo(mUserId, mEmployeeId)
        }

        findViewById<Button>(R.id.btn_savememo).setOnClickListener{
            // memo_id 있으면 /api/memo/update
            if(mMemoId != "") {
                updateMemo()
            }
            // memo_id 없으면 /api/memo/add
            else {
                if (mEmployeeId != null && mUserId != null) {
                    addMemo(mUserId, mEmployeeId)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.dismiss()
    }

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
                    // 메모 있으면 id값 저장 및 텍스트 세팅
                    if(mData?.id != null) {
                        mMemoId = mData?.id
                        mMemoText.setText(mData?.memo)
                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultMemo>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@MemoActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addMemo(userId: String, employeeId:String) {
        loadingDialog.show()
        api.addMemo(userId, employeeId, mMemoText.text.toString()).enqueue(object: Callback<Result.ResultMemo> {
            override fun onResponse(
                call: Call<Result.ResultMemo>,
                response: Response<Result.ResultMemo>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message

                loadingDialog.dismiss()

                if(mCode == 200) {
                    Toast.makeText(this@MemoActivity, getString(R.string.memo_add), Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Result.ResultMemo>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@MemoActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateMemo() {
        loadingDialog.show()
        api.updateMemo(mMemoId, mMemoText.text.toString()).enqueue(object: Callback<Result.ResultBasic> {
            override fun onResponse(
                call: Call<Result.ResultBasic>,
                response: Response<Result.ResultBasic>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message

                loadingDialog.dismiss()

                if(mCode == 200) {
                    Toast.makeText(this@MemoActivity, getString(R.string.memo_update), Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@MemoActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }
}