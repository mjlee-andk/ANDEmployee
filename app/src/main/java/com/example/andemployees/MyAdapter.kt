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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.listview_custom, null)

        val mEmployeesProfile = view.findViewById<ImageView>(R.id.iv_employees_profile)
        val mEmployeesName = view.findViewById<TextView>(R.id.tv_employees_name)
        val mEmployeesDepartment = view.findViewById<TextView>(R.id.tv_employees_department)
        val mEmployeesPosition = view.findViewById<TextView>(R.id.tv_employees_position)

        mEmployeesProfile.background = ShapeDrawable(OvalShape())
        mEmployeesProfile.clipToOutline = true

        val item = mItems[position]

        if(item.profile_img == null) {

            Glide.with(view).load(context.getString(R.string.basic_profile_url)).into(mEmployeesProfile)
        }
        else {
            Glide.with(view).load(item.profile_img).into(mEmployeesProfile)
        }

        // TODO 수정할 것
        mEmployeesName.text = item.name
        mEmployeesDepartment.text = "계량기 사업 1부"
        mEmployeesPosition.text = "사원"
//        mEmployeesName.text = item.name
//        mEmployeesDepartment.text = item.department_id
//        mEmployeesPosition.text = item.position_id

        return view
    }
}