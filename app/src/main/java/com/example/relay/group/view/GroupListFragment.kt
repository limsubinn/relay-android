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
import com.example.relay.ApplicationClass
import com.example.relay.R
import com.example.relay.databinding.FragmentGroupListBinding
import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.group.models.GroupInfoResult
import com.example.relay.group.service.GetClubListInterface
import com.example.relay.group.service.GetClubListService
import com.example.relay.group.view.adapter.GroupListRVAdapter
import com.example.relay.group.models.GroupListResponse
import com.example.relay.group.service.GetUserClubInterface
import com.example.relay.group.service.GetUserClubService
import com.example.relay.ui.MainActivity

class GroupListFragment: Fragment(), GetClubListInterface, GetUserClubInterface {
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
            var search = binding.etGroupSearch.text.toString()

            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> { // 엔터키
                    GetClubListService(this).tryGetClubList(search)

                    // 키보드 내리기
                    val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                }
                KeyEvent.KEYCODE_DEL -> { // 백스페이스
                    binding.etGroupSearch.setText(search.replaceFirst(".$".toRegex(), ""))
                }
            }
            true
        })

        // 그룹 생성 버튼
        binding.btnCreate.setOnClickListener {
            // 유저가 속한 그룹의 이름 가져오기
            val userIdx = ApplicationClass.prefs.getLong("userIdx", 0L)
            if (userIdx != 0L) {
                GetUserClubService(this).tryGetUserClub(userIdx)
            } else {
                Toast.makeText(activity, "유저 정보를 받아오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
            // mainActivity?.groupFragmentChange(3)
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onGetClubListSuccess(response: GroupListResponse) {
        val res = response.result

        // 리사이클러뷰
        val clubList: ArrayList<GroupInfoResult> = arrayListOf()
        val listAdapter = GroupListRVAdapter(clubList)

        binding.rvGroupAll.adapter = listAdapter
        binding.rvGroupAll.layoutManager = LinearLayoutManager(activity)

        // 리사이클러뷰 아이템 클릭 이벤트
        listAdapter.setItemClickListener( object : GroupListRVAdapter.ItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val clubIdx = clubList[position].clubIdx

                // 리스트 -> 메인
                parentFragmentManager.setFragmentResult("go_to_main",
                    bundleOf("clubIdx" to clubIdx))
                mainActivity?.groupFragmentChange(0) // 그룹 메인으로 이동
            }
        })

        // 모집 상태
        val recruitList = listOf("모집전체", "모집중", "모집완료")
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
                when (p2) {
                    1 -> { // 모집중
                        clubList.addAll(res)
                        res.forEach {
                            if (it.recruitStatus == "recruiting") {
                                clubList.add(it)
                            }
                        }
                    }
                    2 -> { // 모집 완료
                        res.forEach {
                            if (it.recruitStatus != "recruiting") {
                                clubList.add(it)
                            }
                        }
                    }
                    else -> { // 전체
                        clubList.addAll(res)
                    }
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

//        // 러너 레벨
//        val levelList = listOf("레벨전체", "초보러너", "중급러너", "프로러너")
//        val levelAdapter = activity?.let {
//            ArrayAdapter<String>(
//                it,
//                R.layout.spinner_item,
//                levelList
//            )
//        }
//        // 레벨 api 아직 x
    }

    override fun onGetClubListFailure(message: String) {
        Toast.makeText(activity, "그룹 목록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onGetUserClubSuccess(response: GroupAcceptedResponse) {
        if (response.code != 4900) {
            // 가입한 그룹이 있으면 그룹 생성하기 거부
            mainActivity?.groupFragmentChange(4)
        } else {
            // 그룹 생성 페이지로 이동
            mainActivity?.groupFragmentChange(3)
        }
    }

    override fun onGetUserClubFailure(message: String) {
        TODO("Not yet implemented")
    }
}