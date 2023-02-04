package com.example.relay.group.view

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.relay.R
import com.example.relay.databinding.FragmentGroupListBinding
import com.example.relay.group.GetClubListInterface
import com.example.relay.group.GetClubListService
import com.example.relay.group.adapter.GroupListRVAdapter
import com.example.relay.group.models.GroupListResponse
import com.example.relay.group.models.GroupListResult
import com.example.relay.ui.MainActivity

class GroupListFragment: Fragment(), GetClubListInterface {
    private var _binding: FragmentGroupListBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        if (context != null) {
            super.onAttach(context)
        }
        mainActivity = activity as MainActivity?
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 클럽 리스트 받아오기 (전체)
        GetClubListService(this).tryGetClubList("")

        // EditText에서 엔터 키를 누르면 검색한 클럽의 리스트를 가져온다.
        binding.etGroupSearch.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> {
                    val search = binding.etGroupSearch.text.toString()
                    GetClubListService(this).tryGetClubList(search)

                    // 키보드 내리기
                    val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
            true
        })

        // 그룹 생성 버튼
        binding.btnCreate.setOnClickListener {
            mainActivity?.groupFragmentChange(3)
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onGetClubListSuccess(response: GroupListResponse) {
        val res = response.result

        // 리사이클러뷰
        val clubList: ArrayList<GroupListResult> = arrayListOf()
        val listAdapter = GroupListRVAdapter(clubList)

        binding.rvGroupAll.adapter = listAdapter
        binding.rvGroupAll.layoutManager = LinearLayoutManager(activity)

        // 리사이클러뷰 아이템 클릭 이벤트
        listAdapter.setItemClickListener( object : GroupListRVAdapter.ItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val clubIdx = clubList[position].clubIdx
                val recruitStatus = clubList[position].recruitStatus

                // 리스트 -> 메인
                parentFragmentManager.setFragmentResult("go_to_main",
                    bundleOf("clubIdx" to clubIdx, "recruitStatus" to recruitStatus))
                mainActivity?.groupFragmentChange(0) // 그룹 메인으로 이동
            }
        })

        // 모집 상태
        val recruitList = listOf("전체", "모집중", "모집완료")
        val recruitAdapter = activity?.let {
            ArrayAdapter<String>(
                it,
                R.layout.spinner_item,
                recruitList
            )
        }

        binding.spRecruitStatus.adapter = recruitAdapter
        binding.spRecruitStatus.setSelection(0)
        binding.spRecruitStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                clubList.clear()
                if (p2 == 1) {
                    res.forEach {
                        if (it.recruitStatus == "recruiting") {
                            clubList.add(it)
                        }
                    }
                } else if (p2 == 2) {
                    res.forEach {
                        if (it.recruitStatus != "recruiting") {
                            clubList.add(it)
                        }
                    }
                } else {
                    clubList.addAll(res)
                }
                listAdapter.notifyDataSetChanged()

                if (listAdapter.itemCount == 0) {
                    Toast.makeText(activity, "검색된 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }

    override fun onGetClubListFailure(message: String) {
        // 에러 발생
    }
}