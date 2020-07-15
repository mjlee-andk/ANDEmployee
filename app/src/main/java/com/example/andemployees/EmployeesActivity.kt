package com.example.andemployees

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmployeesActivity : AppCompatActivity() {

    lateinit var adapter: SearchAdapter

    lateinit var list: ArrayList<Result.TableEmployees>
    lateinit var arraylist: ArrayList<Result.TableEmployees>

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
                    val mEditTextSearch = findViewById<EditText>(R.id.et_employees_search)
                    val mListView = findViewById<ListView>(R.id.listView)

                    list = ArrayList<Result.TableEmployees>()
                    if (mData != null) {
                        list.addAll(mData)
                    }
                    arraylist = ArrayList<Result.TableEmployees>()
                    arraylist.addAll(list)

                    adapter = SearchAdapter(this@EmployeesActivity, list);
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

                    /*어댑터 등록*/
//                    val mMyAdapter = mData?.let { MyAdapter(this@EmployeesActivity, it) }
//                    mListView.adapter = mMyAdapter

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

        findViewById<Button>(R.id.btn_employees_searchbar_back).setOnClickListener {
            finish()
        }
    }

    // 검색을 수행하는 메소드
    fun search(charText: String) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear()

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length == 0) {
            list.addAll(arraylist)
        } else {
            // 리스트의 모든 데이터를 검색한다.
            for (i in 0 until arraylist.size) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if ( arraylist[i].name.contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i))
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged()
    }
}