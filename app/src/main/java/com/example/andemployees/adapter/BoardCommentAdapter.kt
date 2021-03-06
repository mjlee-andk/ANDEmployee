package com.example.andemployees.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.andemployees.BusEvent
import com.example.andemployees.BusProvider
import com.example.andemployees.R
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import com.pixplicity.easyprefs.library.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BoardCommentAdapter(val context: Context, private val comments: ArrayList<Result.TableBoardComments>) : BaseAdapter() {

    val api = RetrofitAPI.create()
    lateinit var view : View

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
//        val view : View
        val holder: ViewHolder

        Prefs.Builder()
            .setContext(context)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(context.packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        val mUserId = Prefs.getString(context.getString(R.string.PREF_USER_ID), null)

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
            holder.boardCommentsMore = view.findViewById(R.id.btn_board_comments_more)

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
        holder.boardCommentsLikeCount?.text = comment.like_count.toString()

        holder.boardCommentsLike.setImageResource(R.drawable.icon_like)
        holder.boardCommentsLike.tag = R.drawable.icon_like

        if(comment.like_clicked) {
            holder.boardCommentsLike.setImageResource(R.drawable.icon_like_selected)
            holder.boardCommentsLike.tag = R.drawable.icon_like_selected
        }

        holder.boardCommentsLikeContainer?.setOnClickListener {




            // 좋아요 취소
            if(holder.boardCommentsLike.tag == R.drawable.icon_like_selected) {
                holder.boardCommentsLike.setImageResource(R.drawable.icon_like)
                holder.boardCommentsLike.tag = R.drawable.icon_like

                holder.boardCommentsLikeCount?.text = (holder.boardCommentsLikeCount?.text.toString().toInt() - 1).toString()
            }
            // 좋아요
            else {
                holder.boardCommentsLike.setImageResource(R.drawable.icon_like_selected)
                holder.boardCommentsLike.tag = R.drawable.icon_like_selected

                holder.boardCommentsLikeCount?.text = (holder.boardCommentsLikeCount?.text.toString().toInt() + 1).toString()
            }

            commentLike(comment.id, mUserId)
        }

        holder.boardCommentsMore.visibility = View.INVISIBLE
        if(comment.user_id == mUserId){
            holder.boardCommentsMore.visibility = View.VISIBLE

            holder.boardCommentsMore.setOnClickListener {
                val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context)
                val items = arrayOf("수정하기", "삭제하기")
                dialogBuilder.setItems(items) { _, which ->
                    when(which) {
                        // 수정하기
                        0 -> {
                            val editCommentDialogBuilder = AlertDialog.Builder(context)
                            val imm: InputMethodManager? = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                            val customLayout = LayoutInflater.from(context).inflate(R.layout.dialog_comment_edit, null)
                            var commentEditText = customLayout.findViewById<EditText>(R.id.et_board_comment_edit)

                            commentEditText.setText(comment.comment)
                            commentEditText.setSelection(commentEditText.length())
                            editCommentDialogBuilder.setView(customLayout)
                            editCommentDialogBuilder.setTitle(context.getString(R.string.hint_comment))
                            editCommentDialogBuilder.setPositiveButton(context.getString(R.string.update)) { _, _ ->
                                var inputComment = commentEditText.text
                                if(inputComment.isEmpty()) {
                                    Toast.makeText(context, context.getString(R.string.hint_contents), Toast.LENGTH_SHORT).show()
                                    return@setPositiveButton
                                }

                                updateComment(comment.id, inputComment.toString())
                                // 키보드 숨기기
                                imm?.toggleSoftInput(
                                    InputMethodManager.HIDE_IMPLICIT_ONLY, 0
                                )
                            }
                            editCommentDialogBuilder.setNegativeButton(context.getString(R.string.cancel)) { _, _ ->

                            }
                            editCommentDialogBuilder.show()

                            // 키보드 띄우기
                            imm?.toggleSoftInput(
                                InputMethodManager.SHOW_FORCED,
                                InputMethodManager.HIDE_IMPLICIT_ONLY
                            )
                        }
                        // 삭제하기
                        1 -> {
                            val deleteDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context)
                            deleteDialogBuilder.setMessage(context.getString(R.string.alert_delete))
                            deleteDialogBuilder.setPositiveButton(context.getString(R.string.delete)) { _, _ ->
                                deleteComment(comment.id)
                            }

                            deleteDialogBuilder.setNegativeButton(context.getString(R.string.keep)) { _, _ ->

                            }
                            deleteDialogBuilder.show()
                        }
                    }
                }

                dialogBuilder.show()
            }
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
        lateinit var boardCommentsMore : Button

        var boardCommentsLikeContainer : LinearLayout? = null
    }

    private fun commentLike(commentId: String, userId: String) {
        api.commentLike(commentId, userId).enqueue(object: Callback<Result.ResultBasic> {
            override fun onResponse(
                call: Call<Result.ResultBasic>,
                response: Response<Result.ResultBasic>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message

                if(mCode == 200) {

                }
            }

            override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
                Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateComment(commentId: String, comment: String){
        api.updateComment(commentId, comment).enqueue(object: Callback<Result.ResultBasic> {
            override fun onResponse(
                call: Call<Result.ResultBasic>,
                response: Response<Result.ResultBasic>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message

                if(mCode == 200) {
//                    view.findViewById<TextView>(R.id.tv_board_comments_comment).text = comment
                    BusProvider.getInstance()
                        .post(BusEvent())
                }
            }

            override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
                Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteComment(commentId: String) {
        api.deleteComment(commentId).enqueue(object:
            Callback<Result.ResultBasic> {
            override fun onResponse(
                call: Call<Result.ResultBasic>,
                response: Response<Result.ResultBasic>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message

                if(mCode == 200) {
                    BusProvider.getInstance()
                        .post(BusEvent())
                }
            }

            override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
                Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }
}