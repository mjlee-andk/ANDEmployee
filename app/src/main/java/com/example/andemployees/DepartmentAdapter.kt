package com.example.andemployees

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.andemployees.models.Result

class DepartmentAdapter(val context: Context, private val mDevisions: ArrayList<Result.TableDevisions>) : BaseExpandableListAdapter(){

    override fun getGroup(p0: Int): Any? {
        return mDevisions.size
    }

    override fun getGroupCount(): Int {
        return mDevisions.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val holder: ViewHolder

        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_division, null)
            holder = ViewHolder()

            holder.divisionName = view.findViewById(R.id.tv_division_name)

            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val division = mDevisions[groupPosition]

        holder.divisionName?.text = division.name

        return view
    }

    override fun getChild(groupPosition: Int, childposition: Int): Result.TableDepartments {
        return mDevisions[groupPosition].departments[childposition]
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return mDevisions[groupPosition].departments.size
    }

    override fun getChildId(groupPosition: Int, childposition: Int): Long {
        return childposition.toLong()
    }

    override fun getChildView(groupPosition: Int, childposition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val holder: ViewHolder

        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_department, null)
            holder = ViewHolder()

            holder.departmentName = view.findViewById(R.id.tv_department_name)

            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val department = mDevisions[groupPosition].departments[childposition]

        holder.departmentName?.text = department.name

        return view
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(groupPosition: Int, childposition: Int): Boolean {
        return true
    }

    private class ViewHolder {
        var divisionName : TextView? = null
        var departmentName : TextView? = null
    }
}