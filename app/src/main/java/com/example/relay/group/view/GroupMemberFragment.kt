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
import com.example.relay.group.GetClubDetailInterface
import com.example.relay.group.GetClubDetailService
import com.example.relay.group.adapter.GroupListRVAdapter
import com.example.relay.group.adapter.GroupMemberRVAdapter
import com.example.relay.group.models.GroupInfoResponse
import com.example.relay.group.models.GroupListResult
import com.example.relay.group.models.Member
import com.example.relay.ui.MainActivity
import java.util.*

class GroupMemberFragment: Fragment(), GetClubDetailInterface {
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

            GetClubDetailService(this).tryGetClubDetail(clubIdx, curDate)

            // 리사이클러뷰
            // val memberList: ArrayList<Member> = arrayListOf()
            // val memberAdapter = GroupMemberRVAdapter(memberList)

            // binding.rvGroupMember.adapter = memberAdapter
            // binding.rvGroupMember.layoutManager = LinearLayoutManager(activity)


//            memberList.apply {
//                add(Member("리페", "팀장"))
//                add(Member("노창", "디자이너"))
//                add(Member("비카", "서버"))
//                add(Member("설기", "서버"))
//                add(Member("솜", "서버"))
//                add(Member("채리", "서버"))
//                add(Member("라나", "안드로이드"))
//                add(Member("리미", "안드로이드"))
//                add(Member("샐리", "안드로이드"))
//                add(Member("야옹", "iOS"))
//                add(Member("테오", "iOS"))
//                add(Member("혜콩", "iOS"))
//            }

            // memberAdapter.notifyDataSetChanged()
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

    override fun onGetClubDetailSuccess(response: GroupInfoResponse) {
        val res = response.result

        // 리사이클러뷰
        val memberList: ArrayList<Member> = arrayListOf()
        val memberAdapter = GroupMemberRVAdapter(memberList)

        binding.rvGroupMember.adapter = memberAdapter
        binding.rvGroupMember.layoutManager = LinearLayoutManager(activity)

        if (res.member != null) {
            memberList.addAll(res.member)
        }

        memberAdapter.notifyDataSetChanged()
    }

    override fun onGetClubDetailFailure(message: String) {
        TODO("Not yet implemented")
    }
}