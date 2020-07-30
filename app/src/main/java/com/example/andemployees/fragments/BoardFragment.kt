package com.example.andemployees.fragments

import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.example.andemployees.BoardDetailActivity
import com.example.andemployees.LoadingDialog
import com.example.andemployees.R
import com.example.andemployees.adapter.BoardAdapter
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pixplicity.easyprefs.library.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BoardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BoardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var loadingDialog: LoadingDialog

    lateinit var adapter: BoardAdapter
    lateinit var list: ArrayList<Result.TableBoards>

    lateinit var mCategoryId: String
    lateinit var mUserId: String

    private val api = RetrofitAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        Prefs.Builder()
            .setContext(context)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(context?.packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        mCategoryId = ""
        mUserId = Prefs.getString(getString(R.string.PREF_USER_ID), null)

        loadingDialog = context?.let { LoadingDialog(it) }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_board, container, false)

        view.findViewById<FloatingActionButton>(R.id.fab_board_add).visibility = View.GONE

        getBoards(mCategoryId, mUserId)

        return view
    }

    override fun onResume() {
        super.onResume()
        getBoards(mCategoryId, mUserId)
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.dismiss()
    }

    private fun getBoards(categoryId: String, userId: String) {

        loadingDialog.show()
        api.getBoards( categoryId, userId ).enqueue(object: Callback<Result.ResultBoards> {
            override fun onResponse(
                call: Call<Result.ResultBoards>,
                response: Response<Result.ResultBoards>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message
                var mData = response.body()?.data

                loadingDialog.dismiss()

                if(mCode == 200) {

                    /*위젯과 멤버변수 참조 획득*/
                    val mListView = view?.findViewById(R.id.lv_boards) as ListView

                    list = ArrayList()
                    if (mData != null) {
                        list.addAll(mData)
                    }
                    adapter = context?.let {
                        BoardAdapter(
                            it,
                            list
                        )
                    }!!
                    mListView.adapter = adapter

                    mListView.setOnItemClickListener { _, _, i, _ ->
                        val selectedBoard = list[i]
                        val intent = Intent(activity, BoardDetailActivity::class.java)
                        intent.putExtra(getString(R.string.BOARD_ID), selectedBoard.id)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultBoards>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(activity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NoticeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BoardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}