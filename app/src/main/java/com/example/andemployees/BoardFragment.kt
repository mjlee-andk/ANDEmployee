package com.example.andemployees

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ListView
import android.widget.Toast
import com.example.andemployees.api.RetrofitAPI
import com.example.andemployees.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NoticeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoticeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var adapter: BoardAdapter
    lateinit var list: ArrayList<Result.TableBoards>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_notice, container, false)

        val api = RetrofitAPI.create()
        // TODO 수정해야함
        api.getBoards( getString(R.string.category_notice), getString(R.string.user_id_dummy) ).enqueue(object: Callback<Result.ResultBoards> {
            override fun onResponse(
                call: Call<Result.ResultBoards>,
                response: Response<Result.ResultBoards>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message
                var mData = response.body()?.data

                if(mCode == 200) {

                    /*위젯과 멤버변수 참조 획득*/
                    val mListView = view.findViewById(R.id.lv_boards) as ListView

                    list = ArrayList()
                    if (mData != null) {
                        list.addAll(mData)
                    }
                    adapter = context?.let { BoardAdapter(it, list) }!!
                    mListView.adapter = adapter

                    mListView.setOnItemClickListener { adapterView, view, i, l ->
                       // 게시글 상세 페이지로 이동
                        val intent = Intent(activity, BoardDetailActivity::class.java)
//                        intent.putExtra("departmentId", selectedDepartment.id)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultBoards>, t: Throwable) {
                Toast.makeText(activity, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        return view
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
            NoticeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}