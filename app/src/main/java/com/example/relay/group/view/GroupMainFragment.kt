package com.example.relay.group.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.R
import com.example.relay.databinding.FragmentGroupMainBinding
import com.example.relay.group.GetClubDetailInterface
import com.example.relay.group.GetClubDetailService
import com.example.relay.group.GetUserClubInterface
import com.example.relay.group.GetUserClubService
import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.group.models.GroupInfoResponse
import com.example.relay.ui.MainActivity
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import java.util.*

class GroupMainFragment: Fragment(), GetUserClubInterface, GetClubDetailInterface {
    private var _binding: FragmentGroupMainBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0

    val today = GregorianCalendar()
    var year: Int = today.get(Calendar.YEAR)
    var month: Int = today.get(Calendar.MONTH)
    var date: Int = today.get(Calendar.DATE)

    var strY = year.toString()
    var strM = (month+1).toString().padStart(2, '0')
    var strD = date.toString().padStart(2, '0')
    var curDate = "${strY}-${strM}-${strD}"

    private var userIdx = prefs.getLong("userIdx", 0L)
    private var clubIdx = 0L
    private var recruitStatus = ""

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
        _binding = FragmentGroupMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set current date to calendar and current month to currentMonth variable
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        val myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                val cal = Calendar.getInstance()
                cal.time = date

                return if (isSelected) R.layout.item_selected_calendar
                else R.layout.item_unselected_calendar
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                // bind data to calendar item views
                holder.itemView.findViewById<TextView>(R.id.tv_date_calendar_item).text = DateUtils.getDayNumber(date)

                if (isSelected) {
                    holder.itemView.findViewById<TextView>(R.id.tv_month_calendar_item).text = DateUtils.getMonth3LettersName(date)
                    holder.itemView.findViewById<TextView>(R.id.tv_year_calendar_item).text = DateUtils.getYear(date)
                }

            }
        }

        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                return true
            }
        }

        val myCalendarChangesObserver = object : CalendarChangesObserver {
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                super.whenSelectionChanged(isSelected, position, date)
            }
        }

        val singleRowCalendar = binding.selCalendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager

            setDates(getFutureDatesOfCurrentMonth())
            initialPositionIndex = date-3
            init()
            select(date-1) // 오늘 날짜 선택
        }

        // 그룹 목록 버튼
        binding.btnRight.setOnClickListener{
            mainActivity?.groupFragmentChange(1)
        }

        // 리스트, 멤버 -> 메인
        setFragmentResultListener("go_to_main") {requestKey, bundle ->
            clubIdx = bundle.getLong("clubIdx", 0L)
            recruitStatus = bundle.getString("recruitStatus", "")

            if (recruitStatus == "recruiting") {
                binding.btnJoinTeam.visibility = View.VISIBLE
            } else {
                binding.btnJoinTeam.visibility = View.GONE
            }

            GetClubDetailService(this).tryGetClubDetail(clubIdx, curDate)
        }

        // 사용자 그룹명 가져오기
        if (clubIdx == 0L) {
            GetUserClubService(this).tryGetUserClub(userIdx)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }

//    private fun getFutureDatesOfSelectMonth(month: Int): List<Date> {
//        currentMonth = month
//        return getDates(mutableListOf())
//    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }

    override fun onGetUserClubSuccess(response: GroupAcceptedResponse) {
        if (response.code != 2008) { // 가입한 그룹 존재 o
            val res = response.result

            if (userIdx == res.clubIdx) {
                binding.btnJoinTeam.text = "탈퇴하기"
            }

            GetClubDetailService(this).tryGetClubDetail(res.clubIdx, curDate)

        } else { // 가입한 그룹 존재 x
            binding.profileImg.visibility = View.GONE
            binding.profileTeam.visibility = View.GONE
            binding.teamLayout.visibility = View.GONE

            binding.tvIntro.text = "가입된 그룹이 없습니다."
        }
    }

    override fun onGetUserClubFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onGetClubDetailSuccess(response: GroupInfoResponse) {
        binding.profileImg.visibility = View.VISIBLE
        binding.profileTeam.visibility = View.VISIBLE
        binding.teamLayout.visibility = View.VISIBLE

        val res = response.result

        binding.profileTeam.text = res.name
        binding.tvIntro.text = res.content

        if (res.imgURL != null) {
            Glide.with(binding.profileImg.context)
                .load(res.imgURL)
                .into(binding.profileImg)
        }
        binding.tvTeamCnt.text = res.member.size.toString()
        binding.targetType.text = res.goalType
        // 목표값 보류

        if (prefs.getLong("userIdx", 0L) == res.hostIdx) {
            binding.btnJoinTeam.text = "수정하기"
        }


        // 모두 보기 버튼
        binding.btnTeamAll.setOnClickListener {
            parentFragmentManager.setFragmentResult("main_to_member",
                bundleOf("clubIdx" to clubIdx, "recruitStatus" to recruitStatus))
            mainActivity?.groupFragmentChange(2) // 팀원 보기로 이동
        }
    }

    override fun onGetClubDetailFailure(message: String) {
        Toast.makeText(activity, "그룹의 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
    }
}