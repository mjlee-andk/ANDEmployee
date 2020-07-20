package com.example.andemployees

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardDetailActivity : AppCompatActivity() {

    lateinit var adapter: BoardCommentAdapter
    lateinit var list: ArrayList<Result.TableBoardComments>

    val api = RetrofitAPI.create()

    lateinit var mInputComment: EditText
    lateinit var mBtnAddComment: Button

    lateinit var mBoardId: String
    lateinit var mUserId: String

    lateinit var mListView: ListView
    lateinit var title: TextView
    lateinit var writer: TextView
    lateinit var date: TextView
    lateinit var content: TextView
    lateinit var likeClicked: ImageView
    lateinit var likeCount: TextView
    lateinit var commentCount: TextView
    lateinit var share: LinearLayout
    lateinit var likeContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        val mIntent = intent
        mBoardId = mIntent.getStringExtra("boardId").toString()
        mUserId = getString(R.string.user_id_dummy);

        mListView = findViewById(R.id.lv_board_detail_container)

        // TODO 다이얼로그 띄워서 수정하기,삭제하기 실행(본인 글일 경우에만)
        findViewById<ImageView>(R.id.iv_board_detail_more).setOnClickListener {

        }

        val header = layoutInflater.inflate(R.layout.listview_board_header, null, false)

        mListView.addHeaderView(header)
        title = header.findViewById(R.id.tv_board_header_title)
        writer = header.findViewById(R.id.tv_board_header_writer)
        date = header.findViewById(R.id.tv_board_header_date)
        content = header.findViewById(R.id.tv_board_header_content)
        likeClicked = header.findViewById(R.id.iv_board_header_like)
        likeCount = header.findViewById(R.id.tv_board_header_like_count)
        commentCount = header.findViewById(R.id.tv_board_header_comment_count)
        share = header.findViewById(R.id.ll_board_header_share_container)
        likeContainer =  header.findViewById(R.id.ll_board_header_like_container)

        if (mBoardId != null) {
            getBoardDetail(mBoardId, mUserId)
        }

        mInputComment = findViewById(R.id.et_board_detail_comment_input)
        mBtnAddComment = findViewById(R.id.btn_board_detail_add_comment)

        mInputComment.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(mInputComment.text.isNotEmpty()) {
                    mBtnAddComment.visibility = View.VISIBLE
                }
                else {
                    mBtnAddComment.visibility = View.INVISIBLE
                }
            }

            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        mBtnAddComment.setOnClickListener {
            val inputText = mInputComment.text.toString()
            val mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            if(inputText.isEmpty()) {
                Toast.makeText(this@BoardDetailActivity, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mBoardId != null) {
                addComment(mBoardId, mUserId, inputText)
                mInputMethodManager.hideSoftInputFromWindow(mBtnAddComment.windowToken, 0);
            }
        }
    }

    private fun getBoardDetail (boardId: String, userId: String) {
        api.getBoardDetail(boardId, userId).enqueue(object: Callback<Result.ResultBoard> {
            override fun onResponse(
                call: Call<Result.ResultBoard>,
                response: Response<Result.ResultBoard>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message
                var mData = response.body()?.data

                if(mCode == 200) {
                    title.text = mData?.title
                    writer.text = mData?.user_name
                    date.text = mData?.createdat
                    content.text = mData?.contents
                    likeCount.text = mData?.like_count.toString()
                    commentCount.text = mData?.comment_count.toString()

                    likeClicked.setImageResource(R.drawable.icon_like)
                    likeClicked.tag = R.drawable.icon_like

                    if (mData != null) {
                        if(mData.like_clicked) {
                            likeClicked.setImageResource(R.drawable.icon_like_selected)
                            likeClicked.tag = R.drawable.icon_like_selected
                        }
                    }

                    likeContainer.setOnClickListener{
                        // 좋아요 취소
                        if(likeClicked.tag == R.drawable.icon_like_selected) {
                            likeClicked.setImageResource(R.drawable.icon_like)
                            likeClicked.tag = R.drawable.icon_like
                            likeCount.text = (likeCount.text.toString().toInt() - 1).toString()
                        }
                        // 좋아요
                        else {
                            likeClicked.setImageResource(R.drawable.icon_like_selected)
                            likeClicked.tag = R.drawable.icon_like_selected
                            likeCount.text = (likeCount.text.toString().toInt() + 1).toString()
                        }
                        boardLike(mBoardId, mUserId)
                    }

                    list = ArrayList()
                    if (mData != null) {
                        list.addAll(mData.comments)
                    }
                    adapter = BoardCommentAdapter(this@BoardDetailActivity, list)
                    mListView.adapter = adapter
                    adapter.notifyDataSetChanged()

                    // 댓글 클릭시 이벤트
                    mListView.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->

                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultBoard>, t: Throwable) {
                Toast.makeText(this@BoardDetailActivity, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addComment(boardId: String, userId: String, comment: String) {
        api.addComment(boardId, userId, comment).enqueue(object: Callback<Result.ResultBasic> {
            override fun onResponse(
                call: Call<Result.ResultBasic>,
                response: Response<Result.ResultBasic>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message

                if(mCode == 200) {
                    mInputComment.text.clear()
                    getBoardDetail(boardId, userId)
                }
            }

            override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
                Toast.makeText(this@BoardDetailActivity, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun boardLike(boardId: String, userId: String) {
        api.boardLike( boardId, userId).enqueue(object:
            Callback<Result.ResultBasic> {
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
                Toast.makeText(this@BoardDetailActivity, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}