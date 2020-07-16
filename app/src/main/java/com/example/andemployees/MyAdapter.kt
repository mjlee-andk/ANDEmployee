package com.example.andemployees

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.andemployees.models.Result

class MyAdapter(val context: Context, private val mItems: ArrayList<Result.TableDepartments>) : BaseAdapter() {
    override fun getCount(): Int {
        return mItems.size
    }

    override fun getItem(position: Int): Any? {
        return mItems[position]
    }

    override fun getItemId(position: Int): Long{
        return 0;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val holder: MyAdapter.ViewHolder

        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_department, null)
            holder = MyAdapter.ViewHolder()

            holder.departmentsName = view.findViewById(R.id.tv_departments_name)

            view.tag = holder
        } else {
            holder = convertView.tag as MyAdapter.ViewHolder
            view = convertView
        }

        val department = mItems[position]

        holder.departmentsName?.text = department.name

        return view
    }

    private class ViewHolder {
        var departmentsName : TextView? = null
    }
}