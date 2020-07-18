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

        /*위젯과 멤버변수 참조 획득*/
        val mListView = view.findViewById(R.id.lv_boards) as ListView

        list = ArrayList()

        var tempData = Result.TableBoards()
        tempData.user_name = "부설연구소 이민재"
        tempData.category_name = "공지사항"
        tempData.title = "ADK 000 결혼식 축의금 각출 안내"
        tempData.contents = "ADK 000 의 결혼식 축의금 각출 안내드립니다. 각출을 원하시는 분은 관리부 000 에게 00월 00일까지 메일을 보내주시기 바랍니다. 기타 궁금한 사항은 관리부 00에게 문의 주세요. 감사합니다."
        tempData.date = "10분전"
        tempData.click_count = 23
        tempData.like_count = 15
        tempData.comment_count = 5
        tempData.like_clicked = true
        tempData.tag_clicked = false

        for (i in 1..10) {
            list.add(tempData)
        }
        adapter = context?.let { BoardAdapter(it, list) }!!
        mListView.adapter = adapter


        mListView.setOnItemClickListener{ adapterView, view, i, l ->

        }
        return view
//        val api = RetrofitAPI.create()
//        api.getBoards("").enqueue(object: Callback<Result.ResultBoards> {
//            override fun onResponse(
//                call: Call<Result.ResultBoards>,
//                response: Response<Result.ResultBoards>
//            ) {
//                var mCode = response.body()?.code
//                var mMessage = response.body()?.message
//                var mData = response.body()?.data
//
//                if(mCode == 200) {
//
//                    /*위젯과 멤버변수 참조 획득*/
//                    val mListView = view.findViewById(R.id.lv_boards) as ListView
//
//                    list = ArrayList()
//                    if (mData != null) {
//                        list.addAll(mData)
//                    }
//                    adapter = context?.let { BoardAdapter(it, list) }!!
//
//
//                    mListView.setOnClickListener{
//                       // 게시글 상세 페이지로 이동
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<Result.ResultBoards>, t: Throwable) {
//                Toast.makeText(activity, "서버 통신에 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
//            }
//        })
//
//        return view
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