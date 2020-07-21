package com.example.andemployees

import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardEditActivity : AppCompatActivity() {
    val api = RetrofitAPI.create()

    var mBoardId: String? = null
    lateinit var mUserId: String
    lateinit var mCategories: ArrayList<Result.TableBoardCategories>

    lateinit var inputCategory: TextView
    lateinit var inputTitle: EditText
    lateinit var inputContents: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_edit)

        val mIntent = intent
        mBoardId = mIntent.getStringExtra("boardId")
        mUserId = getString(R.string.user_id_dummy);

        // 카테고리 목록 받아오기
        getCategory()
    }

    private fun setView() {
        inputCategory = findViewById(R.id.tv_board_edit_category)
        inputTitle = findViewById(R.id.et_board_edit_title)
        inputContents = findViewById(R.id.et_board_edit_contents)
        val containerCategory = findViewById<LinearLayout>(R.id.ll_board_edit_category_container)

        var categoryNames = ArrayList<String>()

        containerCategory.setOnClickListener{
            val categoryDialogBuilder = AlertDialog.Builder(this)
            categoryDialogBuilder.setTitle(getString(R.string.hint_category))

            categoryNames = ArrayList<String>()
            for(i in mCategories){
                categoryNames.add(i.name)
            }
            val items = categoryNames.toArray(arrayOf<String>())
            categoryDialogBuilder.setItems(items){ i, which  ->
                inputCategory.text = categoryNames[which]
            }
            categoryDialogBuilder.show()
        }

        // 추가 또는 수정 버튼 동작
        // TODO 이미지 추가 필요
        val btnComplete = findViewById<Button>(R.id.btn_board_edit_complete)
        btnComplete.setOnClickListener {
            if(inputCategory.text == getString(R.string.hint_category) ) {
                Toast.makeText(this@BoardEditActivity, getString(R.string.hint_category), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(inputTitle.text.isEmpty()) {
                Toast.makeText(this@BoardEditActivity, getString(R.string.hint_title), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(inputContents.text.isEmpty()) {
                Toast.makeText(this@BoardEditActivity, getString(R.string.hint_contents), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val category = inputCategory.text.toString()
            var categoryId = ""
            for( item in mCategories){
                if(item.name == category) {
                    categoryId = item.id
                }
            }
            val title = inputTitle.text.toString()
            val contents = inputContents.text.toString()

            // 추가하기
            if(mBoardId.isNullOrEmpty() || mBoardId == "") {
                addBoard(mUserId, categoryId, title, contents, null)
            }
            // 수정하기
            else {
                updateBoard(mBoardId!!, categoryId, title, contents, null)
            }
        }

        if(mBoardId.isNullOrEmpty() || mBoardId == "") {
            btnComplete.text = getString(R.string.add)
        }
        else {
            getBoardDetail(mBoardId!!, mUserId)
            btnComplete.text = getString(R.string.update)
        }
    }

    private fun getCategory() {
        api.getBoardCategories().enqueue(object: Callback<Result.ResultBoardCategories> {
            override fun onResponse(
                call: Call<Result.ResultBoardCategories>,
                response: Response<Result.ResultBoardCategories>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message
                var mData = response.body()?.data

                if(mCode == 200) {
                    if (mData != null) {
                        mCategories = ArrayList<Result.TableBoardCategories>()
                        mCategories.addAll(mData)
                        setView()
                    }
                    else {
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultBoardCategories>, t: Throwable) {
                Toast.makeText(this@BoardEditActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
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
                    if (mData != null) {
                        inputCategory.text = mData.category_name
                        inputTitle.setText(mData.title)
                        inputContents.setText(mData.contents)
                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultBoard>, t: Throwable) {
                Toast.makeText(this@BoardEditActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addBoard(userId: String, categoryId: String, title: String, contents: String, image: String?) {
        api.addBoard(userId, categoryId, title, contents, image).enqueue(object: Callback<Result.ResultBasic> {
            override fun onResponse(
                call: Call<Result.ResultBasic>,
                response: Response<Result.ResultBasic>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message

                if(mCode == 200) {
                    finish()
                }
            }

            override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
                Toast.makeText(this@BoardEditActivity, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateBoard(boardId: String, categoryId: String, title: String, contents: String, image: String?) {
        api.updateBoard(boardId, categoryId, title, contents, image).enqueue(object: Callback<Result.ResultBasic> {
            override fun onResponse(
                call: Call<Result.ResultBasic>,
                response: Response<Result.ResultBasic>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message

                if(mCode == 200) {
                    finish()
                }
            }

            override fun onFailure(call: Call<Result.ResultBasic>, t: Throwable) {
                Toast.makeText(this@BoardEditActivity, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}