package com.example.andemployees

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result

class BoardCommentAdapter(val context: Context, private val comments: ArrayList<Result.TableBoardComments>) : BaseAdapter() {

    override fun getCount(): Int {
        return comments.size;
    }

    override fun getItem(position: Int): Any {
        return comments[position];
    }

    override fun getItemId(position: Int): Long{
        return 0;
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val view : View
        val holder: ViewHolder

        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_board_comments, null)
            holder = ViewHolder()

            holder.boardCommentsDivision = view.findViewById(R.id.tv_board_comments_division)
            holder.boardCommentsDepartment = view.findViewById(R.id.tv_board_comments_department)
            holder.boardCommentsAccount = view.findViewById(R.id.tv_board_comments_account)
            holder.boardCommentsComment = view.findViewById(R.id.tv_board_comments_comment)
            holder.boardCommentsDate = view.findViewById(R.id.tv_board_comments_date)
            holder.boardCommentsLikeCount = view.findViewById(R.id.tv_board_comments_like_count)
            holder.boardCommentsLike = view.findViewById(R.id.iv_board_comments_like)
            holder.boardCommentsLikeContainer = view.findViewById(R.id.ll_board_comments_like_container)

            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val comment = comments[position]

        holder.boardCommentsDivision?.text = comment.division_name
        holder.boardCommentsDepartment?.text = comment.department_name
        holder.boardCommentsAccount?.text = comment.user_name
        holder.boardCommentsComment?.text = comment.comment
        holder.boardCommentsDate?.text = comment.createdat
//        holder.boardCommentsLikeCount?.text =  "좋아요 " + comment.like_count.toString()

        holder.boardCommentsLike.setImageResource(R.drawable.icon_like)
        holder.boardCommentsLike.tag = R.drawable.icon_like

//        if(comment.like_clicked) {
//            holder.boardCommentsLike.setImageResource(R.drawable.icon_like_selected)
//            holder.boardCommentsLike.tag = R.drawable.icon_like_selected
//        }

        holder.boardCommentsLikeContainer?.setOnClickListener {
            val api = RetrofitAPI.create()
            // TODO 댓글 좋아요 api 추가해야함
//            api.board( board.id, context.getString(R.string.user_id_dummy) ).enqueue(object:
//                Callback<Result.ResultBasic> {
//                override fun onResponse(
//                    call: Call<Result.ResultBasic>,
//                    response: Response<Result.ResultBasic>
//                ) {
//                    var mCode = response.body()?.code
//                    var mMessage = response.body()?.message
//
//                    if(mCode == 200) {
//
//                    }
//                }
//
//                override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
//                    Toast.makeText(context, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
//                }
//            })
        }

        return view
    }

    private class ViewHolder {
        var boardCommentsDivision : TextView? = null
        var boardCommentsDepartment : TextView? = null
        var boardCommentsAccount : TextView? = null
        var boardCommentsComment : TextView? = null
        var boardCommentsDate : TextView? = null
        var boardCommentsLikeCount : TextView? = null
        lateinit var boardCommentsLike : ImageView

        var boardCommentsLikeContainer : LinearLayout? = null
    }
}