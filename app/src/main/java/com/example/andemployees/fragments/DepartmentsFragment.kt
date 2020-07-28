package com.example.andemployees.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast
import com.example.andemployees.EmployeesActivity
import com.example.andemployees.LoadingDialog
import com.example.andemployees.R
import com.example.andemployees.adapter.DepartmentAdapter
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
 * Use the [EmployeesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmployeesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val api = RetrofitAPI.create()
    private lateinit var loadingDialog: LoadingDialog

    lateinit var adapter: DepartmentAdapter
    lateinit var list: ArrayList<Result.TableDevisions>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        loadingDialog = context?.let { LoadingDialog(it) }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_departments, container, false)
        getDepartments(view)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.dismiss()
    }

    private fun getDepartments(view: View){
        loadingDialog.show()
        api.getDepartments().enqueue(object: Callback<Result.ResultDevisions> {
            override fun onResponse(
                call: Call<Result.ResultDevisions>,
                response: Response<Result.ResultDevisions>
            ) {
                var mCode = response.body()?.code
                var mMessage = response.body()?.message
                var mData = response.body()?.data

                loadingDialog.dismiss()

                if(mCode == 200) {

                    /*위젯과 멤버변수 참조 획득*/
                    val mListView = view.findViewById(R.id.lv_departments) as ExpandableListView

                    list = ArrayList()
                    if (mData != null) {
                        list.addAll(mData)
                    }
                    adapter = context?.let {
                        DepartmentAdapter(
                            it,
                            list
                        )
                    }!!
                    mListView.setAdapter(adapter)
                    mListView.expandGroup(0)
                    mListView.expandGroup(1)
                    mListView.setOnGroupExpandListener {

                    }

                    mListView.setOnGroupCollapseListener {

                    }

                    mListView.setOnChildClickListener { _, _, groupPosition, childPostion, id ->
                        val selectedDepartment = list[groupPosition].departments[childPostion]
                        val intent = Intent(activity, EmployeesActivity::class.java)
                        intent.putExtra(getString(R.string.DEPARTMENT_ID), selectedDepartment.id)
                        startActivity(intent)

                        return@setOnChildClickListener false
                    }
                }
            }

            override fun onFailure(call: Call<Result.ResultDevisions>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(activity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            }
        })
    }
}