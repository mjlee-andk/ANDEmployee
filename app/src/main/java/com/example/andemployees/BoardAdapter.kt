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

class BoardAdapter(val context: Context, private val boards: ArrayList<Result.TableBoards>) : BaseAdapter() {

    override fun getCount(): Int {
        return boards.size;
    }

    override fun getItem(position: Int): Any {
        return boards[position];
    }

    override fun getItemId(position: Int): Long{
        return 0;
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val view : View
        val holder: ViewHolder

        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_board, null)
            holder = ViewHolder()

            holder.boardsCategory = view.findViewById(R.id.tv_board_category)
            holder.boardsTitle = view.findViewById(R.id.tv_board_title)
            holder.boardsContent = view.findViewById(R.id.tv_board_content)
            holder.boardsWriter = view.findViewById(R.id.tv_board_writer)
            holder.boardsClickCount = view.findViewById(R.id.tv_board_click_count)
            holder.boardslikeCount = view.findViewById(R.id.tv_board_like_count)
            holder.boardsCommentCount = view.findViewById(R.id.tv_board_comment_count)
            holder.boardsDate = view.findViewById(R.id.tv_board_date)

            holder.boardsLikeClicked = view.findViewById(R.id.iv_board_like)
            holder.boardsTagClicked = view.findViewById(R.id.iv_board_tag)

            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val board = boards[position]

        holder.boardsCategory?.text = board.category_name
        holder.boardsTitle?.text = board.title
        holder.boardsContent?.text = board.contents
        holder.boardsWriter?.text = board.user_name
        holder.boardsClickCount?.text = board.click_count.toString()
        holder.boardslikeCount?.text = board.like_count.toString()
        holder.boardsCommentCount?.text = board.comment_count.toString()
        holder.boardsDate?.text = board.date

        holder.boardsLikeClicked?.setImageResource(R.drawable.icon_like)
        holder.boardsTagClicked?.setImageResource(R.drawable.icon_tag)

        return view
    }

    private class ViewHolder {
        var boardsCategory : TextView? = null
        var boardsTitle : TextView? = null
        var boardsContent : TextView? = null
        var boardsWriter : TextView? = null
        var boardsClickCount : TextView? = null
        var boardslikeCount : TextView? = null
        var boardsCommentCount : TextView? = null
        var boardsDate : TextView? = null
        lateinit var boardsLikeClicked : ImageView
        lateinit var boardsTagClicked : ImageView
    }
}