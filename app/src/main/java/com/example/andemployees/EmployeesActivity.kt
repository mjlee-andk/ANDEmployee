package com.example.andemployees

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class EmployeesActivity : AppCompatActivity() {

    var itemList = arrayListOf<MyItem>(
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "이게 나야"),
        MyItem("testimg", "이민재", "this is me"),
        MyItem("testimg", "리민자이", "에욱")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employees)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        /*위젯과 멤버변수 참조 획득*/
        val mListView = findViewById<ListView>(R.id.listView)
        /*어댑터 등록*/
        val mMyAdapter = MyAdapter(this, itemList)
        mListView.adapter = mMyAdapter

        mListView.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->
            val selectItem = parent.getItemAtPosition(position)
        }
    }
}