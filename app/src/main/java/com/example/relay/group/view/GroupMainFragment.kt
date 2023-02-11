package com.example.relay.group.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
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
import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.group.models.GroupDailyRecordResponse
import com.example.relay.group.models.GroupInfoResponse
import com.example.relay.group.service.*
import com.example.relay.ui.MainActivity
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class GroupMainFragment: Fragment(), GetUserClubInterface, GetClubDetailInterface, GetClubDailyInterface {
    private var _binding: FragmentGroupMainBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var currentYear = 0
    private var curDate = ""

    val today = GregorianCalendar()
    var year: Int = today.get(Calendar.YEAR)
    var month: Int = today.get(Calendar.MONTH) + 1
    var date: Int = today.get(Calendar.DATE)

    private var userIdx = prefs.getLong("userIdx", 0L)
    private var clubIdx = 0L
    private var clubName = ""
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

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

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
                holder.itemView.findViewById<TextView>(R.id.tv_date_calendar_item).text =
                    DateUtils.getDayNumber(date)

                if (isSelected) {
                    holder.itemView.findViewById<TextView>(R.id.tv_month_calendar_item).text =
                        DateUtils.getMonth3LettersName(date)
                    holder.itemView.findViewById<TextView>(R.id.tv_year_calendar_item).text =
                        DateUtils.getYear(date)
                }

            }
        }

        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                curDate = simpleDateFormat.format(date)
                GetClubDailyService(this@GroupMainFragment).tryGetClubDaily(clubIdx, curDate)
                return true
            }
        }

        val myCalendarChangesObserver = object : CalendarChangesObserver {
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                super.whenSelectionChanged(isSelected, position, date)
            }
        }

        binding.selCalendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager

//            setDates(getFutureDatesOfCurrentMonth())
//            initialPositionIndex = date - 3
//            init()
//            select(date - 1) // 오늘 날짜 선택
        }


        // 달력 버튼
        binding.btnCalendar.setOnClickListener {
            // 그룹페이지 -> 그룹레코드
            parentFragmentManager.setFragmentResult("go_to_group_record",
                bundleOf("clubIdx" to clubIdx, "curDate" to curDate)
            )
            mainActivity?.groupFragmentChange(6) // 기록 페이지로 이동
        }

        // 그룹 목록 버튼
        binding.btnRight.setOnClickListener{
            mainActivity?.groupFragmentChange(1)
        }

        // 그룹 시간표 버튼
        binding.btnTimetable.setOnClickListener{
            parentFragmentManager.setFragmentResult("go_to_timetable",
                bundleOf("clubIdx" to clubIdx, "clubName" to clubName))
            mainActivity?.groupFragmentChange(7) // 그룹 시간표로 이동
        }

        // 가입하기 버튼
        binding.btnJoinTeam.setOnClickListener{
            when (binding.btnJoinTeam.text){
                "가입하기" -> {
                    parentFragmentManager.setFragmentResult("go_to_edit_main_timetable",
                        bundleOf("clubIdx" to clubIdx, "clubName" to clubName))
                    mainActivity?.timetableFragmentChange(1) // 시간표 편집 페이지 이동
                }
                "탈퇴하기" -> {

                }
                else -> throw IllegalArgumentException("잘못된 값")
            }
        }

//        // 리스트, 멤버 -> 메인
//        setFragmentResultListener("go_to_main") {requestKey, bundle ->
//            clubIdx = bundle.getLong("clubIdx", 0L)
//            recruitStatus = bundle.getString("recruitStatus", "")
//
//            Log.d("main", clubIdx.toString())
//
//            if (recruitStatus == "recruiting") {
//                binding.btnJoinTeam.visibility = View.VISIBLE
//            } else {
//                binding.btnJoinTeam.visibility = View.GONE
//            }

//            binding.selCalendar.apply {
//                setDates(getFutureDatesOfCurrentMonth())
//                initialPositionIndex = date - 3
//                init()
//                select(date - 1) // 오늘 날짜 선택
//            }
//
//            GetClubDetailService(this).tryGetClubDetail(clubIdx, curDate)
//        }
//
//        // 사용자 그룹명 가져오기
//        Handler(Looper.getMainLooper()).postDelayed({
//            if (clubIdx == 0L) {
//                GetUserClubService(this).tryGetUserClub(userIdx)
//            }
//        }, 1)

        GetUserClubService(this).tryGetUserClub(userIdx)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        currentYear = calendar[Calendar.YEAR]
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfSelectMonth(month: Int, year: Int): List<Date> {
        currentMonth = month-1
        currentYear = year
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        calendar.set(Calendar.YEAR, currentYear)
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

            binding.btnJoinTeam.visibility = View.VISIBLE
            binding.btnJoinTeam.text = "탈퇴하기"

            clubIdx = res.clubIdx
            clubName = res.name

            // 기록 -> 그룹페이지
            setFragmentResultListener("record_to_group") { requestKey, bundle ->
                clubIdx = bundle.getLong("clubIdx")

                if (clubIdx != res.clubIdx) {
                    binding.btnJoinTeam.visibility = View.VISIBLE
                    binding.btnJoinTeam.text = "가입하기"
                }

                curDate = bundle.getString("curDate", "") // "yyyy-mm-dd"
                year = Integer.parseInt(curDate.substring(0, 4))
                month = Integer.parseInt(curDate.substring(5, 7))
                date = Integer.parseInt(curDate.substring(8, 10))
            }

            // 목록, 메인 -> 그룹페이지
            setFragmentResultListener("go_to_main") {requestKey, bundle ->
                clubIdx = bundle.getLong("clubIdx", 0L)
                recruitStatus = bundle.getString("recruitStatus", "")

                if (clubIdx != res.clubIdx) {
                    binding.btnJoinTeam.visibility = View.VISIBLE
                    binding.btnJoinTeam.text = "가입하기"

                    if (recruitStatus == "recruiting") {
                        binding.btnJoinTeam.visibility = View.VISIBLE
                    } else {
                        binding.btnJoinTeam.visibility = View.GONE
                    }
                } else {
                    binding.btnJoinTeam.visibility = View.VISIBLE
                    binding.btnJoinTeam.text = "탈퇴하기"
                }

            }

            binding.selCalendar.apply {
                setDates(getFutureDatesOfSelectMonth(month, year))
                initialPositionIndex = date - 3
                init()
                select(date - 1) // 날짜 선택
            }

            GetClubDetailService(this).tryGetClubDetail(clubIdx, curDate)

        } else { // 가입한 그룹 존재 x
            binding.profileImg.visibility = View.GONE
            binding.profileTeam.visibility = View.GONE
            binding.teamLayout.visibility = View.GONE

            binding.tvIntro.text = "가입된 그룹이 없습니다."

            // 기록 -> 그룹페이지
            setFragmentResultListener("record_to_group") { requestKey, bundle ->
                clubIdx = bundle.getLong("clubIdx")

                curDate = bundle.getString("curDate", "") // "yyyy-mm-dd"
                year = Integer.parseInt(curDate.substring(0, 4))
                month = Integer.parseInt(curDate.substring(5, 7))
                date = Integer.parseInt(curDate.substring(8, 10))

                binding.selCalendar.apply {
                    setDates(getFutureDatesOfSelectMonth(month, year))
                    initialPositionIndex = date - 3
                    init()
                    select(date - 1) // 날짜 선택
                }

                GetClubDetailService(this).tryGetClubDetail(clubIdx, curDate)
            }

            setFragmentResultListener("go_to_main") {requestKey, bundle ->
                clubIdx = bundle.getLong("clubIdx", 0L)
                recruitStatus = bundle.getString("recruitStatus", "")

                binding.profileImg.visibility = View.VISIBLE
                binding.profileTeam.visibility = View.VISIBLE
                binding.teamLayout.visibility = View.VISIBLE

                if (recruitStatus == "recruiting") {
                    binding.btnJoinTeam.visibility = View.VISIBLE
                    binding.btnJoinTeam.text = "가입하기"
                } else {
                    binding.btnJoinTeam.visibility = View.GONE
                }

                binding.selCalendar.apply {
                    setDates(getFutureDatesOfSelectMonth(month, year))
                    initialPositionIndex = date - 3
                    init()
                    select(date - 1) // 날짜 선택
                }

                GetClubDetailService(this).tryGetClubDetail(clubIdx, curDate)
            }
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

        if (userIdx == res.hostIdx) {
            binding.btnJoinTeam.text = "수정하기"
        }

        // 모두 보기 버튼
        binding.btnTeamAll.setOnClickListener {
            parentFragmentManager.setFragmentResult("go_to_member_list",
                bundleOf("clubIdx" to clubIdx, "recruitStatus" to recruitStatus, "hostIdx" to res.hostIdx))
            mainActivity?.groupFragmentChange(2) // 팀원 보기로 이동
        }
    }

    override fun onGetClubDetailFailure(message: String) {
        Toast.makeText(activity, "그룹의 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onGetClubDailySuccess(response: GroupDailyRecordResponse) {
        if (response.isSuccess) {
            val res = response.result

            var sec = res.totalTime
            var min = sec / 60
            val hour = min / 60
            min %= 60
            sec %= 60

            val hh = hour.toInt().toString().padStart(2, '0')
            val mm = min.toInt().toString().padStart(2, '0')
            val ss = sec.toInt().toString().padStart(2, '0')

            // 목표값 수정 필요
            if (res.goalType == "NOGOAL") {
                val sec = res.totalTime
                binding.goalValue.visibility = View.GONE
                binding.goalTarget.setTextColor(Color.BLACK)
                binding.goalTarget.text = "${hh} : ${mm} : ${ss}"
                binding.goalType.text = "시간"

                binding.otherType.text = "거리"
                binding.otherValue.text = res.totalDist.toString()
            } else if (res.goalType == "TIME") {
                binding.goalValue.visibility = View.VISIBLE
                binding.goalValue.text = "${hh} : ${mm} : ${ss}"
                binding.goalTarget.setTextColor(Color.RED)
                binding.goalTarget.text = res.goalValue.toString()
                binding.goalType.text = "시간"

                binding.otherType.text = "거리"
                binding.otherValue.text = res.totalDist.toString() + "km"
            } else if (res.goalType == "DISTANCE") {
                binding.goalValue.visibility = View.VISIBLE
                binding.goalValue.text = res.totalDist.toString()
                binding.goalTarget.setTextColor(Color.RED)
                binding.goalTarget.text = res.goalValue.toString()
                binding.goalType.text = "거리"

                binding.otherType.text = "시간"
                binding.otherValue.text = "${hh} : ${mm} : ${ss}"
            }

            binding.runningPace.text = res.avgPace.toString()
        }
    }

    override fun onGetClubDailyFailure(message: String) {
        Toast.makeText(activity, "해당 그룹의 기록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
    }
}