package com.example.andemployees

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.andemployees.fragments.AnonymousFragment
import com.example.andemployees.fragments.EmployeesFragment
import com.example.andemployees.fragments.NoticeFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.iid.FirebaseInstanceId

class MainActivity : FragmentActivity() {
    lateinit var tabs: TabLayout;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.container,
            EmployeesFragment()
        ).commit()

        tabs = findViewById(R.id.tabs)
        tabs.addTab(tabs.newTab().setText("부서 목록"))
        tabs.addTab(tabs.newTab().setText("공지사항"))
        tabs.addTab(tabs.newTab().setText("자유게시판"))

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position: Int = tab?.position ?: 0
                var selected: Fragment? = null
                when (position) {
                    0 -> {
                        selected =
                            EmployeesFragment()
                    }
                    1 -> {
                        selected =
                            NoticeFragment()
                    }
                    2 -> {
                        selected =
                            AnonymousFragment()
                    }
                }
                if (selected != null) {
                    supportFragmentManager.beginTransaction().replace(R.id.container, selected).commit()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })
    }

    override fun onResume() {
        super.onResume()
    }
}