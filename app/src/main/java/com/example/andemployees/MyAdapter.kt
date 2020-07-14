package com.example.andemployees

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.andemployees.models.Result

class MyAdapter(val context: Context, private val mItems: List<Result.TableEmployees>) : BaseAdapter() {
    override fun getCount(): Int {
        return mItems.size;
    }

    override fun getItem(position: Int): Result.TableEmployees {
        return mItems[position]
    }

    override fun getItemId(position: Int): Long{
        return 0;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.listview_custom, null)

        val mEmployeesProfile = view.findViewById<ImageView>(R.id.iv_employees_profile)
        val mEmployeesName = view.findViewById<TextView>(R.id.tv_employees_name)
        val mEmployeesDepartment = view.findViewById<TextView>(R.id.tv_employees_department)
        val mEmployeesPosition = view.findViewById<TextView>(R.id.tv_employees_position)

        val item = mItems[position]

        if(item.profile_img == null) {
            Glide.with(view).load("http://goo.gl/gEgYUd").into(mEmployeesProfile)
        }
        else {
            Glide.with(view).load(item.profile_img).into(mEmployeesProfile)
        }

        mEmployeesName.text = item.name
        mEmployeesDepartment.text = item.department_id
        mEmployeesPosition.text = item.position_id

        return view
    }
}