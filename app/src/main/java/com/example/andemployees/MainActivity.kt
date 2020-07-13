package com.example.andemployees

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayout

class MainActivity : FragmentActivity() {
    lateinit var tabs: TabLayout;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.container, EmployeesFragment()).commit()

        tabs = findViewById(R.id.tabs)
        tabs.addTab(tabs.newTab().setText("주소록"))
        tabs.addTab(tabs.newTab().setText("알리미"))
        tabs.addTab(tabs.newTab().setText("익게"))

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position: Int = tab?.position ?: 0
                var selected: Fragment? = null
                when (position) {
                    0 -> {
                        selected = EmployeesFragment()
                    }
                    1 -> {
                        selected = NoticeFragment()
                    }
                    2 -> {
                        selected = AnonymousFragment()
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
//        setSupportActionBar(findViewById(R.id.toolbar))
//
//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}