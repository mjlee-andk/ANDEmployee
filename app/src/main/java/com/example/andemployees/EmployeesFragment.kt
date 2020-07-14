package com.example.andemployees

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_employees.view.*

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
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_employees, container, false)
        val view: View = inflater.inflate(R.layout.fragment_employees, container, false)


        // 관리부
        view.btn_department_management.setOnClickListener{ view ->
            val intent = Intent(activity, EmployeesActivity::class.java)
            intent.putExtra("departmentId", "29b745f5-c5bd-11ea-9982-20cf305809b8")
            activity?.startActivity(intent)
        }
        // 경영지원실
        view.btn_department_business_support.setOnClickListener{ view ->
            val intent = Intent(activity, EmployeesActivity::class.java)
            intent.putExtra("departmentId", "29c1c969-c5bd-11ea-9982-20cf305809b8")
            activity?.startActivity(intent)
        }
        // PI 사업부
        view.btn_department_business_pi.setOnClickListener{ view ->
            val intent = Intent(activity, EmployeesActivity::class.java)
            intent.putExtra("departmentId", "29d1f87c-c5bd-11ea-9982-20cf305809b8")
            activity?.startActivity(intent)
        }
        // 계량기 사업 1부
        view.btn_department_business_weight_1.setOnClickListener{ view ->
            val intent = Intent(activity, EmployeesActivity::class.java)
            intent.putExtra("departmentId", "29de5206-c5bd-11ea-9982-20cf305809b8")
            activity?.startActivity(intent)
        }
        // 계량기 사업 2부
        view.btn_department_business_weight_2.setOnClickListener{ view ->
            val intent = Intent(activity, EmployeesActivity::class.java)
            intent.putExtra("departmentId", "29ea2057-c5bd-11ea-9982-20cf305809b8")
            activity?.startActivity(intent)
        }
        // 계측기 사업부
        view.btn_department_business_instrument.setOnClickListener{ view ->
            val intent = Intent(activity, EmployeesActivity::class.java)
            intent.putExtra("departmentId", "29ed7672-c5bd-11ea-9982-20cf305809b8")
            activity?.startActivity(intent)
        }
        // 부설연구소
        view.btn_department_rnd.setOnClickListener{ view ->
            val intent = Intent(activity, EmployeesActivity::class.java)
            intent.putExtra("departmentId", "7d3b5d72-c1fe-11ea-9982-20cf305809b8")
            activity?.startActivity(intent)
        }
        // CS 사업부
        view.btn_department_cs.setOnClickListener{ view ->
            val intent = Intent(activity, EmployeesActivity::class.java)
            intent.putExtra("departmentId", "29f0d839-c5bd-11ea-9982-20cf305809b8")
            activity?.startActivity(intent)
        }
        // 교정센터
        view.btn_department_calibration.setOnClickListener{ view ->
            val intent = Intent(activity, EmployeesActivity::class.java)
            intent.putExtra("departmentId", "2a00b1fb-c5bd-11ea-9982-20cf305809b8")
            activity?.startActivity(intent)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmployeesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}