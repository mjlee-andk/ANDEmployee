package com.example.andemployees.adapter

import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.andemployees.LoadingDialog
import com.example.andemployees.R
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import com.pixplicity.easyprefs.library.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardAdapter(val context: Context, private val boards: ArrayList<Result.TableBoards>) : BaseAdapter() {

    val api = RetrofitAPI.create()

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

            holder.boardsLike = view.findViewById(R.id.iv_board_like)

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

        // 게시글 좋아요 클릭시 이미지 변경
        holder.boardsLike?.setImageResource(R.drawable.icon_like)
        holder.boardsLike?.tag = R.drawable.icon_like

        if(board.like_clicked) {
            holder.boardsLike?.setImageResource(R.drawable.icon_like_selected)
            holder.boardsLike?.tag = R.drawable.icon_like_selected
        }

        // 게시글 좋아요 클릭시 이미지 변경
        holder.boardsLike.setOnClickListener{
            Prefs.Builder()
                .setContext(context)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(context.packageName)
                .setUseDefaultSharedPreference(true)
                .build()

            val mUserId = Prefs.getString(context.getString(R.string.PREF_USER_ID), null)

            // 좋아요 취소
            if(holder.boardsLike.tag == R.drawable.icon_like_selected) {
                holder.boardsLike.setImageResource(R.drawable.icon_like)
                holder.boardsLike.tag = R.drawable.icon_like

                holder.boardsLikeCount?.text = (holder.boardsLikeCount?.text.toString().toInt() - 1).toString()
            }
            // 좋아요
            else {
                holder.boardsLike.setImageResource(R.drawable.icon_like_selected)
                holder.boardsLike.tag = R.drawable.icon_like_selected

                holder.boardsLikeCount?.text = (holder.boardsLikeCount?.text.toString().toInt() + 1).toString()
            }

            boardLike(board.id, mUserId)
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
        lateinit var boardsLike : ImageView
    }

    private fun boardLike (boardId: String, userId: String) {
        var loadingDialog = LoadingDialog(context)
        loadingDialog.show()
        api.boardLike( boardId, userId ).enqueue(object:
            Callback<Result.ResultBasic> {
            override fun onResponse(
                call: Call<Result.ResultBasic>,
                response: Response<Result.ResultBasic>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message

                loadingDialog.dismiss()

                if(mCode == 200) {

                }
            }

            override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
                Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                loadingDialog.dismiss()
            }
        })
    }
}