package com.example.relay.group.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.databinding.FragmentGroupCreateBinding
import com.example.relay.group.GetClubListService
import com.example.relay.group.GetUserClubInterface
import com.example.relay.group.GetUserClubService
import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.ui.MainActivity

class GroupCreateFragment: Fragment(), GetUserClubInterface {
    private var _binding: FragmentGroupCreateBinding? = null
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
        _binding = FragmentGroupCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 스피너 설정 -> 화면 기획 완성되면 수정
//        val sGoal = resources.getStringArray(R.array.group_goal)
//        val goalAdapter =
//            activity?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, sGoal) }
//        binding.spGoal.adapter = goalAdapter
//        binding.spGoal.setSelection(0)
//        binding.spGoal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//        }

        // 유저가 속한 그룹의 이름 가져오기
        val userIdx = prefs.getLong("userIdx", 0L)
        if (userIdx != 0L) {
            GetUserClubService(this).tryGetUserClub(userIdx)
        } else {
            Toast.makeText(activity, "유저 정보를 받아오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

        // 취소 버튼
        binding.btnBack.setOnClickListener {
            mainActivity?.groupFragmentChange(1) // 그룹 리스트로 이동
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onGetUserClubSuccess(response: GroupAcceptedResponse) {
        binding.btnNext.setOnClickListener {
            if (response.code != 2008) {
                // 가입한 그룹이 있으면 그룹 생성하기 거부
                mainActivity?.groupFragmentChange(4)
            } else {
                // 가입한 그룹 x (수정 예정)
                Toast.makeText(activity, "!!!그룹 생성 가능!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onGetUserClubFailure(message: String) {
        TODO("Not yet implemented")
    }
}