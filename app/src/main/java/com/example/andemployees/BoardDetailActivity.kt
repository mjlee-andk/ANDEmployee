package com.example.andemployees

import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.annotation.RequiresApi
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
    lateinit var arraylist: ArrayList<Result.TableBoardComments>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        val mIntent = intent
        val mBoardId = mIntent.getStringExtra("boardId")
        val mUserId = getString(R.string.user_id_dummy);

        val api = RetrofitAPI.create()
        if (mBoardId != null) {
            api.getBoardDetail(mBoardId, mUserId).enqueue(object: Callback<Result.ResultBoard> {
                override fun onResponse(
                    call: Call<Result.ResultBoard>,
                    response: Response<Result.ResultBoard>
                ) {
                    var mCode = response.body()?.code
                    var mMessage = response.body()?.message
                    var mData = response.body()?.data

                    if(mCode == 200) {
                        val mListView = findViewById<ListView>(R.id.lv_board_detail_container)
                        val header = layoutInflater.inflate(R.layout.listview_board_header, null, false)
                        val title = header.findViewById<TextView>(R.id.tv_board_header_title)
                        val writer = header.findViewById<TextView>(R.id.tv_board_header_writer)
                        val date = header.findViewById<TextView>(R.id.tv_board_header_date)
                        val content = header.findViewById<TextView>(R.id.tv_board_header_content)
                        val likeClicked = header.findViewById<ImageView>(R.id.iv_board_header_like)
                        val likeCount = header.findViewById<TextView>(R.id.tv_board_header_like_count)
                        val commentCount = header.findViewById<TextView>(R.id.tv_board_header_comment_count)
                        val share = header.findViewById<TextView>(R.id.tv_board_header_share)

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

                        header.findViewById<LinearLayout>(R.id.ll_board_header_like_container).setOnClickListener{
                            if (mData != null) {
                                api.boardLike( mData.id, getString(R.string.user_id_dummy) ).enqueue(object:
                                    Callback<Result.ResultBasic> {
                                    override fun onResponse(
                                        call: Call<Result.ResultBasic>,
                                        response: Response<Result.ResultBasic>
                                    ) {
                                        var mCode = response.body()?.code
                                        var mMessage = response.body()?.message

                                        if(mCode == 200) {
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
                                        }
                                    }

                                    override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
                                        Toast.makeText(this@BoardDetailActivity, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                        }

                        mListView.addHeaderView(header)
                        list = ArrayList<Result.TableBoardComments>()
                        if (mData != null) {
                            list.addAll(mData.comments)
                        }
                        adapter = BoardCommentAdapter(this@BoardDetailActivity, list);
                        mListView.adapter = adapter



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

    }
}