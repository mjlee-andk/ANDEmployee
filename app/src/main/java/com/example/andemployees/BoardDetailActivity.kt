package com.example.andemployees

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.andemployees.adapter.BoardCommentAdapter
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import com.pixplicity.easyprefs.library.Prefs
import com.squareup.otto.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardDetailActivity : AppCompatActivity() {

    lateinit var adapter: BoardCommentAdapter
    lateinit var list: ArrayList<Result.TableBoardComments>

    private val api = RetrofitAPI.create()
    private lateinit var loadingDialog: LoadingDialog

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
    lateinit var mBoardDetailMore: ImageView
    lateinit var mBoardDetailBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BusProvider.getInstance().register(this)
        setContentView(R.layout.activity_board_detail)

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        loadingDialog = LoadingDialog(this@BoardDetailActivity)

        val mIntent = intent
        mBoardId = mIntent.getStringExtra(getString(R.string.BOARD_ID)).toString()
        mUserId = Prefs.getString(getString(R.string.PREF_USER_ID), null)

        mListView = findViewById(R.id.lv_board_detail_container)

        mBoardDetailBack = findViewById(R.id.btn_board_detail_back)
        mBoardDetailBack.setOnClickListener {
            finish()
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
                Toast.makeText(this@BoardDetailActivity, getString(R.string.hint_comment), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mBoardId != null) {
                addComment(mBoardId, mUserId, inputText)
                mInputMethodManager.hideSoftInputFromWindow(mBtnAddComment.windowToken, 0);
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(mBoardId != null) {
            getBoardDetail(mBoardId, mUserId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BusProvider.getInstance().unregister(this)
        loadingDialog.dismiss()
    }

    private fun getBoardDetail (boardId: String, userId: String) {
        loadingDialog.show()
        api.getBoardDetail(boardId, userId).enqueue(object: Callback<Result.ResultBoard> {
            override fun onResponse(
                call: Call<Result.ResultBoard>,
                response: Response<Result.ResultBoard>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message
                var mData = response.body()?.data

                loadingDialog.dismiss()

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

                    mBoardDetailMore = findViewById(R.id.iv_board_detail_more)
                    if (mData != null) {
                        mBoardDetailMore.visibility = INVISIBLE

                        if(mUserId == mData.user_id){
                            mBoardDetailMore.visibility = VISIBLE
                            mBoardDetailMore.setOnClickListener {
                                val dialogBuilder = AlertDialog.Builder(this@BoardDetailActivity)
                                val items = arrayOf("수정하기", "삭제하기")
                                dialogBuilder.setItems(items) { _, which ->
                                    when(which) {
                                        // 수정하기
                                        0 -> {
                                            val intent = Intent(this@BoardDetailActivity, BoardEditActivity::class.java)
                                            intent.putExtra(getString(R.string.BOARD_ID), mBoardId)
                                            startActivity(intent)
                                        }
                                        // 삭제하기
                                        1 -> {
                                            val deleteDialogBuilder = AlertDialog.Builder(this@BoardDetailActivity)
                                            deleteDialogBuilder.setMessage(getString(R.string.alert_delete))
                                            deleteDialogBuilder.setPositiveButton("삭제") { _, _ ->
                                                deleteBoard(mBoardId)
                                            }

                                            deleteDialogBuilder.setNegativeButton("유지") { _, _ ->

                                            }
                                            deleteDialogBuilder.show()
                                        }
                                    }
                                }

                                dialogBuilder.show()
                            }
                        }
                    }

                    list = ArrayList()
                    if (mData != null) {
                        list.addAll(mData.comments)
                    }
                    adapter =
                        BoardCommentAdapter(
                            this@BoardDetailActivity,
                            list
                        )
                    mListView.adapter = adapter
                    adapter.notifyDataSetChanged()

                    // 댓글 클릭시 이벤트
                    mListView.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->

                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultBoard>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@BoardDetailActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addComment(boardId: String, userId: String, comment: String) {
        loadingDialog.show()
        api.addComment(boardId, userId, comment).enqueue(object: Callback<Result.ResultBasic> {
            override fun onResponse(
                call: Call<Result.ResultBasic>,
                response: Response<Result.ResultBasic>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message

                loadingDialog.dismiss()

                if(mCode == 200) {
                    mInputComment.text.clear()
                    getBoardDetail(boardId, userId)
                }
            }

            override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@BoardDetailActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun boardLike(boardId: String, userId: String) {
        loadingDialog.show()
        api.boardLike(boardId, userId).enqueue(object:
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
                loadingDialog.dismiss()
                Toast.makeText(this@BoardDetailActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteBoard(boardId: String) {
        loadingDialog.show()
        api.deleteBoard(boardId).enqueue(object:
            Callback<Result.ResultBasic> {
            override fun onResponse(
                call: Call<Result.ResultBasic>,
                response: Response<Result.ResultBasic>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message

                loadingDialog.dismiss()

                if(mCode == 200) {
                    finish()
                }
            }

            override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@BoardDetailActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 댓글 등록 후 갱신을 위한 이벤트 버스
    @Subscribe
    fun onEvent(event: BusEvent){
        getBoardDetail(mBoardId, mUserId)
    }
}