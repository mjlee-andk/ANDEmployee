package com.example.andemployees

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
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

class EmployeesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employees)
        setSupportActionBar(findViewById(R.id.toolbar))

        val mIntent = intent
        var mDepartmentId = mIntent.getStringExtra("departmentId")
        if(mDepartmentId == null) {
            mDepartmentId = ""
        }

        val api = RetrofitAPI.create()
        api.getEmployees("", "", mDepartmentId).enqueue(object: Callback<Result.ResultEmployees> {
            override fun onResponse(
                call: Call<Result.ResultEmployees>,
                response: Response<Result.ResultEmployees>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message
                var mData = response.body()?.data

                if(mCode == 200) {

                    /*위젯과 멤버변수 참조 획득*/
                    val mListView = findViewById<ListView>(R.id.listView)
                    /*어댑터 등록*/
                    val mMyAdapter = mData?.let { MyAdapter(this@EmployeesActivity, it) }
                    mListView.adapter = mMyAdapter

                    mListView.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->
                        val selectedEmployee = parent.getItemAtPosition(position) as Result.TableEmployees
                        val intent = Intent(this@EmployeesActivity, EmployeeDetailActivity::class.java)
                        intent.putExtra("employeeId", selectedEmployee.id)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultEmployees>, t: Throwable) {
                Toast.makeText(this@EmployeesActivity, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}