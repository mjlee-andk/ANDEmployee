package com.example.andemployees

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import com.pixplicity.easyprefs.library.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardEditActivity : AppCompatActivity() {
    private val api = RetrofitAPI.create()
    private lateinit var loadingDialog: LoadingDialog

    var mBoardId: String? = null
    lateinit var mUserId: String
    lateinit var mCategories: ArrayList<Result.TableBoardCategories>

    lateinit var inputCategory: TextView
    lateinit var inputTitle: EditText
    lateinit var inputContents: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_edit)

        loadingDialog = LoadingDialog(this@BoardEditActivity)

        val mIntent = intent
        mBoardId = mIntent.getStringExtra(getString(R.string.BOARD_ID))
        mUserId = Prefs.getString(getString(R.string.PREF_USER_ID), null)

        // 카테고리 목록 받아오기
        getCategory()
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.dismiss()
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
        loadingDialog.show()
        api.getBoardCategories().enqueue(object: Callback<Result.ResultBoardCategories> {
            override fun onResponse(
                call: Call<Result.ResultBoardCategories>,
                response: Response<Result.ResultBoardCategories>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message
                var mData = response.body()?.data

                loadingDialog.dismiss()
                if(mCode == 200) {
                    if (mData != null) {
                        mCategories = ArrayList()
                        mCategories.addAll(mData)
                        setView()
                    }
                    else {
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultBoardCategories>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@BoardEditActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
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
                    if (mData != null) {
                        inputCategory.text = mData.category_name
                        inputTitle.setText(mData.title)
                        inputContents.setText(mData.contents)
                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultBoard>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@BoardEditActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addBoard(userId: String, categoryId: String, title: String, contents: String, image: String?) {
        loadingDialog.show()
        api.addBoard(userId, categoryId, title, contents, image).enqueue(object: Callback<Result.ResultBasic> {
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
                Toast.makeText(this@BoardEditActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateBoard(boardId: String, categoryId: String, title: String, contents: String, image: String?) {
        loadingDialog.show()
        api.updateBoard(boardId, categoryId, title, contents, image).enqueue(object: Callback<Result.ResultBasic> {
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
                Toast.makeText(this@BoardEditActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }
}