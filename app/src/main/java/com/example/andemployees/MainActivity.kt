package com.example.andemployees

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.andemployees.fragments.FreeBoardFragment
import com.example.andemployees.fragments.DepartmentsFragment
import com.example.andemployees.fragments.BoardFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : FragmentActivity() {
    lateinit var tabs: TabLayout;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.container,
            DepartmentsFragment()
        ).commit()

        tabs = findViewById(R.id.tabs)
        tabs.addTab(tabs.newTab().setText(getString(R.string.tab_title_departments)))
        tabs.addTab(tabs.newTab().setText(getString(R.string.tab_title_notice)))
        tabs.addTab(tabs.newTab().setText(getString(R.string.tab_title_freeboard)))

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position: Int = tab?.position ?: 0
                var selected: Fragment? = null
                when (position) {
                    0 -> {
                        selected =
                            DepartmentsFragment()
                    }
                    1 -> {
                        selected =
                            BoardFragment()
                    }
                    2 -> {
                        selected =
                            FreeBoardFragment()
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