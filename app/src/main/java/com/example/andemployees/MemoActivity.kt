package com.example.andemployees

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)
//        setSupportActionBar(findViewById(R.id.toolbar))

        var mMemoId = ""
        val mIntent = intent
        val mUserId = mIntent.getStringExtra("userId")
        val mEmployeeId = mIntent.getStringExtra("employeeId")

        val mMemoText = findViewById<EditText>(R.id.et_memo)

        val api = RetrofitAPI.create()

        // 기존에 작성한 메모 있는지 검색
        if (mEmployeeId != null && mUserId != null) {
            api.getMemo(mUserId, mEmployeeId).enqueue(object: Callback<Result.ResultMemo> {
                override fun onResponse(
                    call: Call<Result.ResultMemo>,
                    response: Response<Result.ResultMemo>
                ) {
                    var mCode = response.body()?.code
                    var mMessage = response.body()?.message
                    var mData = response.body()?.data

                    if(mCode == 200) {
                        // 메모 있으면 id값 저장 및 텍스트 세팅
                        if(mData?.id != null) {
                            mMemoId = mData?.id
                            mMemoText.setText(mData?.memo)
                        }
                    }
                }

                override fun onFailure(call: Call<Result.ResultMemo>, t: Throwable) {
                    Toast.makeText(this@MemoActivity, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        findViewById<Button>(R.id.btn_savememo).setOnClickListener{
            // memo_id 있으면 /api/memo/update
            if(mMemoId != "") {
                api.updateMemo(mMemoId, mMemoText.text.toString()).enqueue(object: Callback<Result.ResultBasic> {
                    override fun onResponse(
                        call: Call<Result.ResultBasic>,
                        response: Response<Result.ResultBasic>
                    ) {
                        var mCode = response.body()?.code
                        var mMessage = response.body()?.message

                        if(mCode == 200) {
                            Toast.makeText(this@MemoActivity, "메모가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
                        Toast.makeText(this@MemoActivity, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            // memo_id 없으면 /api/memo/add
            else {
                if (mEmployeeId != null && mUserId != null) {
                    api.addMemo(mUserId, mEmployeeId, mMemoText.text.toString()).enqueue(object: Callback<Result.ResultMemo> {
                        override fun onResponse(
                            call: Call<Result.ResultMemo>,
                            response: Response<Result.ResultMemo>
                        ) {
                            var mCode = response.body()?.code
                            var mMessage = response.body()?.message

                            if(mCode == 200) {
                                Toast.makeText(this@MemoActivity, "메모가 추가 되었습니다.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<Result.ResultMemo>, t: Throwable) {
                            Toast.makeText(this@MemoActivity, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }
}