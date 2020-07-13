package com.example.andemployees

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class MyAdapter(val context: Context, val mItems: ArrayList<MyItem>) : BaseAdapter() {
    override fun getCount(): Int {
        return mItems.size;
    }

    override fun getItem(position: Int): MyItem {
        return mItems[position]
    }

    override fun getItemId(position: Int): Long{
        return 0;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.listview_custom, null)

        val iv_img = view.findViewById<ImageView>(R.id.iv_img)
        val tv_name = view.findViewById<TextView>(R.id.tv_name)
        val tv_contents = view.findViewById<TextView>(R.id.tv_contents)

        val item = mItems[position]
        val resourceId = context.resources.getIdentifier(item.icon, "drawable", context.packageName)

        iv_img.setImageResource(resourceId)
        tv_name.text = item.name
        tv_contents.text = item.contents

        return view
    }
}