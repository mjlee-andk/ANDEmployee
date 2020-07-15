package com.example.andemployees

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.andemployees.models.Result

class SearchAdapter(val context: Context, private val employees: ArrayList<Result.TableEmployees>) : BaseAdapter() {

    override fun getCount(): Int {
        return employees.size;
    }

    override fun getItem(position: Int): Any {
        return employees[position];
    }

    override fun getItemId(position: Int): Long{
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val view : View
        val holder: ViewHolder

        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_custom, null)
            holder = ViewHolder()

            holder.employeesProfile = view.findViewById(R.id.iv_employees_profile)
            holder.employeesProfile.background = ShapeDrawable(OvalShape())
            holder.employeesProfile.clipToOutline = true
            holder.employeesName = view.findViewById(R.id.tv_employees_name)
            holder.employeesDepartment = view.findViewById(R.id.tv_employees_department)
            holder.employeesPosition = view.findViewById(R.id.tv_employees_position)

            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val employee = employees[position]

        if(employee.profile_img == null) {
            Glide.with(view).load(context.getString(R.string.basic_profile_url)).into(holder.employeesProfile)
        }
        else {
            Glide.with(view).load(employee.profile_img).into(holder.employeesProfile)
        }

        holder.employeesName?.text = employee.name
        holder.employeesDepartment?.text = employee.department_name
        holder.employeesPosition?.text = employee.position_name

        return view
    }

    private class ViewHolder {
        lateinit var employeesProfile : ImageView
        var employeesName : TextView? = null
        var employeesDepartment : TextView? = null
        var employeesPosition : TextView? = null
    }
}