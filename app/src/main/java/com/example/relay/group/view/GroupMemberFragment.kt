package com.example.relay.group.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.databinding.FragmentGroupMemberBinding
import com.example.relay.group.GetClubDetailService
import com.example.relay.group.GetMemberListInterface
import com.example.relay.group.GetMemberListService
import com.example.relay.group.adapter.GroupMemberRVAdapter
import com.example.relay.group.models.Member
import com.example.relay.group.models.MemberResponse
import com.example.relay.ui.MainActivity
import java.util.*

class GroupMemberFragment: Fragment(), GetMemberListInterface {
    private var _binding: FragmentGroupMemberBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null

    private var userIdx = prefs.getLong("userIdx", 0L)
    private var clubIdx = 0L
    private var recruitStatus = ""

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
        _binding = FragmentGroupMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val today = GregorianCalendar()
        var year: Int = today.get(Calendar.YEAR)
        var month: Int = today.get(Calendar.MONTH)
        var date: Int = today.get(Calendar.DATE)

        var strY = year.toString()
        var strM = (month+1).toString().padStart(2, '0')
        var strD = date.toString().padStart(2, '0')
        var curDate = "${strY}-${strM}-${strD}"

        // 메인 -> 멤버
        setFragmentResultListener("main_to_member") { requestKey, bundle ->
            clubIdx = bundle.getLong("clubIdx")
            recruitStatus = bundle.getString("recruitStatus", "")

            GetMemberListService(this).tryGetMemberList(clubIdx, curDate)
        }

        // > 버튼
            binding.btnRight.setOnClickListener {
                // 멤버 -> 메인
                parentFragmentManager.setFragmentResult("go_to_main",
                    bundleOf("clubIdx" to clubIdx, "recruitStatus" to recruitStatus))
                mainActivity?.groupFragmentChange(0) // 그룹 메인으로 이동
            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onGetMemberListSuccess(response: MemberResponse) {
        val res = response.result

        // 리사이클러뷰
        val memberList: ArrayList<Member> = arrayListOf()
        val memberAdapter = GroupMemberRVAdapter(memberList)

        binding.rvGroupMember.adapter = memberAdapter
        binding.rvGroupMember.layoutManager = LinearLayoutManager(activity)

        if (res != null) {
            memberList.addAll(res)
        }

        memberAdapter.notifyDataSetChanged()

        // 리사이클러뷰 아이템 클릭 이벤트
//        memberAdapter.setItemClickListener( object : GroupMemberRVAdapter.ItemClickListener {
//            override fun onMemberClick(view: View, position: Int) {
//                val clubIdx = clubList[position].clubIdx
//                val recruitStatus = clubList[position].recruitStatus
//
//                // 리스트 -> 메인
//                parentFragmentManager.setFragmentResult("go_to_main",
//                    bundleOf("clubIdx" to clubIdx, "recruitStatus" to recruitStatus))
//                mainActivity?.groupFragmentChange(0) // 그룹 메인으로 이동
//            }
//        })
    }

    override fun onGetMemberListFailure(message: String) {
        TODO("Not yet implemented")
    }
}