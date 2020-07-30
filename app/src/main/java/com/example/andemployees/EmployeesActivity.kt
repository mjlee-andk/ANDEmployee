package com.example.andemployees

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.andemployees.adapter.SearchAdapter
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmployeesActivity : AppCompatActivity() {

    private val api = RetrofitAPI.create()
    private lateinit var loadingDialog: LoadingDialog

    lateinit var adapter: SearchAdapter
    lateinit var list: ArrayList<Result.TableEmployees>
    lateinit var arraylist: ArrayList<Result.TableEmployees>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employees)
//        setSupportActionBar(findViewById(R.id.toolbar))

        loadingDialog = LoadingDialog(this@EmployeesActivity)

        val mIntent = intent
        var mDepartmentId = mIntent.getStringExtra(getString(R.string.DEPARTMENT_ID))
        if(mDepartmentId == null) {
            mDepartmentId = ""
        }

        getEmployees("", "", mDepartmentId)

        findViewById<Button>(R.id.btn_employees_back).setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.dismiss()
    }

    private fun getEmployees(search:String, divisionId:String, departmentId: String) {
        loadingDialog.show()
        api.getEmployees(search, divisionId, departmentId).enqueue(object: Callback<Result.ResultEmployees> {
            override fun onResponse(
                call: Call<Result.ResultEmployees>,
                response: Response<Result.ResultEmployees>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message
                var mData = response.body()?.data

                loadingDialog.dismiss()
                if(mCode == 200) {

                    /*위젯과 멤버변수 참조 획득*/
                    val mEditTextSearch = findViewById<EditText>(R.id.et_employees_search)
                    val mListView = findViewById<ListView>(R.id.lv_employees)

                    list = ArrayList()
                    if (mData != null) {
                        list.addAll(mData)
                    }
                    arraylist = ArrayList()
                    arraylist.addAll(list)

                    adapter = SearchAdapter(
                        this@EmployeesActivity,
                        list
                    );
                    mListView.adapter = adapter

                    mEditTextSearch.addTextChangedListener(object: TextWatcher{
                        override fun afterTextChanged(p0: Editable?) {
                            val text = mEditTextSearch.text.toString()
                            search(text)
                        }

                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {

                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                        }

                    })

                    mListView.onItemClickListener = AdapterView.OnItemClickListener{ parent, _, position, _ ->
                        val selectedEmployee = parent.getItemAtPosition(position) as Result.TableEmployees
                        val intent = Intent(this@EmployeesActivity, EmployeeDetailActivity::class.java)
                        intent.putExtra(getString(R.string.EMPLOYEE_ID), selectedEmployee.id)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultEmployees>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@EmployeesActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 검색을 수행하는 메소드
    private fun search(charText: String) {
        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear()

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.isEmpty()) {
            list.addAll(arraylist)
        } else {
            // 리스트의 모든 데이터를 검색한다.
            for (i in 0 until arraylist.size) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if ( arraylist[i].name.contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist[i])
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged()
    }
}