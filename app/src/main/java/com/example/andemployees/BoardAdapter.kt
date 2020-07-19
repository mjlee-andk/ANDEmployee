package com.example.andemployees

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            view = LayoutInflater.from(context).inflate(R.layout.listview_boards, null)
            holder = ViewHolder()

            holder.boardsCategory = view.findViewById(R.id.tv_board_category)
            holder.boardsTitle = view.findViewById(R.id.tv_board_title)
            holder.boardsContent = view.findViewById(R.id.tv_board_content)
            holder.boardsWriter = view.findViewById(R.id.tv_board_writer)
            holder.boardsClickCount = view.findViewById(R.id.tv_board_click_count)
            holder.boardsLikeCount = view.findViewById(R.id.tv_board_like_count)
            holder.boardsCommentCount = view.findViewById(R.id.tv_board_comment_count)
            holder.boardsDate = view.findViewById(R.id.tv_board_date)

            holder.boardsLikeClicked = view.findViewById(R.id.iv_board_like)

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
        holder.boardsLikeCount?.text = board.like_count.toString()
        holder.boardsCommentCount?.text = board.comment_count.toString()
        holder.boardsDate?.text = board.createdat

        holder.boardsLikeClicked?.setImageResource(R.drawable.icon_like)
        holder.boardsLikeClicked?.tag = R.drawable.icon_like
        if(board.like_clicked) {
            holder.boardsLikeClicked?.setImageResource(R.drawable.icon_like_selected)
            holder.boardsLikeClicked?.tag = R.drawable.icon_like_selected
        }

        holder.boardsLikeClicked.setOnClickListener{
            val api = RetrofitAPI.create()
            // TODO 수정해야함
            api.boardLike( board.id, context.getString(R.string.user_id_dummy) ).enqueue(object:
                Callback<Result.ResultBasic> {
                override fun onResponse(
                    call: Call<Result.ResultBasic>,
                    response: Response<Result.ResultBasic>
                ) {
                    var mCode = response.body()?.code
                    var mMessage = response.body()?.message

                    if(mCode == 200) {
                        // 좋아요 취소
                        if(holder.boardsLikeClicked?.tag == R.drawable.icon_like_selected) {
                            holder.boardsLikeClicked?.setImageResource(R.drawable.icon_like)
                            holder.boardsLikeClicked?.tag = R.drawable.icon_like
                            holder.boardsLikeCount?.text = (holder.boardsLikeCount?.text.toString().toInt() - 1).toString()
                        }
                        // 좋아요
                        else {
                            holder.boardsLikeClicked?.setImageResource(R.drawable.icon_like_selected)
                            holder.boardsLikeClicked?.tag = R.drawable.icon_like_selected
                            holder.boardsLikeCount?.text = (holder.boardsLikeCount?.text.toString().toInt() + 1).toString()
                        }
                    }
                }

                override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
                    Toast.makeText(context, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return view
    }

    private class ViewHolder {
        var boardsCategory : TextView? = null
        var boardsTitle : TextView? = null
        var boardsContent : TextView? = null
        var boardsWriter : TextView? = null
        var boardsClickCount : TextView? = null
        var boardsLikeCount : TextView? = null
        var boardsCommentCount : TextView? = null
        var boardsDate : TextView? = null
        lateinit var boardsLikeClicked : ImageView
    }
}