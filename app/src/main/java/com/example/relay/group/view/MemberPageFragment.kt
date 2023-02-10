package com.example.relay.group.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
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
import com.example.relay.ApplicationClass
import com.example.relay.R
import com.example.relay.databinding.FragmentMemberPageBinding
import com.example.relay.group.service.GetClubDailyService
import com.example.relay.group.service.GetMemberListService
import com.example.relay.mypage.models.DailyRecordResponse
import com.example.relay.mypage.models.UserProfileResponse
import com.example.relay.mypage.service.MypageInterface
import com.example.relay.mypage.service.MypageService
import com.example.relay.mypage.view.decorator.SelectDecorator
import com.example.relay.ui.MainActivity
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*


class MemberPageFragment: Fragment(), MypageInterface {
    private var _binding: FragmentMemberPageBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var currentYear = 0
    private var curDate = ""

    val today = GregorianCalendar()
    var year: Int = today.get(Calendar.YEAR)
    var month: Int = today.get(Calendar.MONTH)
    var date: Int = today.get(Calendar.DATE)

    private var userIdx = 0L
    private var hostIdx = 0L
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
        _binding = FragmentMemberPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 멤버리스트 -> 멤버페이지
        setFragmentResultListener("go_to_member_page") { requestKey, bundle ->
            clubIdx = bundle.getLong("clubIdx")
            hostIdx = bundle.getLong("hostIdx")
            recruitStatus = bundle.getString("recruitStatus", "")
            userIdx = bundle.getLong("userIdx")

            // 유저 프로필
            MypageService(this).tryGetUserProfile(userIdx)
        }

        // 레코드 -> 멤버
        setFragmentResultListener("record_to_member") { requestKey, bundle ->

            curDate = bundle.getString("curDate", "")
            userIdx = bundle.getLong("userIdx", 0L)
            clubIdx = bundle.getLong("clubIdx", 0L)
            hostIdx = bundle.getLong("hostIdx", 0L)
            recruitStatus = bundle.getString("recruitStatus", "")

            year = Integer.parseInt(curDate.substring(0, 4))
            month = Integer.parseInt(curDate.substring(5, 7))
            date = Integer.parseInt(curDate.substring(8, 10))

            // 유저 프로필
            MypageService(this).tryGetUserProfile(userIdx)

            binding.selCalendar.apply {
                setDates(getFutureDatesOfSelectMonth(month, year))
                initialPositionIndex = date - 3
                init()
                select(date-1)
            }
        }

        // 멤버페이지 -> 멤버리스트
        binding.btnRight.setOnClickListener {
            // 멤버 페이지 -> 멤버 리스트
            parentFragmentManager.setFragmentResult("go_to_member_list",
                bundleOf("clubIdx" to clubIdx, "hostIdx" to hostIdx, "recruitStatus" to recruitStatus)
            )

            mainActivity?.groupFragmentChange(2) // 멤버리스트로 이동
        }

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
                MypageService(this@MemberPageFragment).tryGetDailyRecord(curDate, userIdx)
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

            setDates(getFutureDatesOfCurrentMonth())
            initialPositionIndex = date - 3
            init()
            select(date - 1) // 오늘 날짜 선택
        }


        // 달력 버튼
        binding.btnCalendar.setOnClickListener {
            // 멤버 -> 레코드
            parentFragmentManager.setFragmentResult("go_to_member_record",
                bundleOf("curDate" to curDate, "userIdx" to userIdx, "clubIdx" to clubIdx, "hostIdx" to hostIdx, "recruitStatus" to recruitStatus)
            )
            mainActivity?.groupFragmentChange(7) // 기록 페이지로 이동
        }

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

    override fun onGetUserProfileSuccess(response: UserProfileResponse) {
        if (response.isSuccess) {
            val res = response.result

            binding.profileName.text = "${res.nickname} / ${res.clubName}"
            binding.tvIntro.text = res.statusMsg
            Glide.with(binding.profileImg.context)
                .load(res.imgUrl)
                .into(binding.profileImg)

            // 기록
            // MypageService(this).tryGetDailyRecord(curDate, userIdx)
        }
    }

    override fun onGetUserProfileFailure(message: String) {
        Toast.makeText(activity, "유저의 프로필을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onGetDailyRecordSuccess(response: DailyRecordResponse) {
        if (response.isSuccess) {
            val res = response.result

            binding.tvNotRecord.visibility = View.GONE
            binding.recordLayout.visibility = View.VISIBLE

            // 거리, 시간, 페이스
            if ((res.clubName == "그룹에 속하지 않습니다.") || (res.goalType == "목표없음")) {
                binding.goalValue.visibility = View.GONE
                binding.goalTarget.text = res.time.toString() // 수정 필요
                binding.goalTarget.setTextColor(Color.BLACK)
                binding.goalType.text = "시간"
            } else {
                binding.goalType.text = res.goalType

                if (res.goalType == "시간") {
                    binding.goalValue.text = res.time.toString() // 수정 필요
                    binding.goalTarget.text = res.goalValue.toString() // 수정 필요
                    binding.otherType.text = "거리"
                    binding.otherValue.text = res.distance.toString() + "km"
                } else {
                    binding.goalValue.text = res.distance.toString() + "km"
                    binding.goalTarget.text = res.goalValue.toString() + "km"
                    binding.otherType.text = "시간"
                    binding.otherValue.text = res.time.toString() // 수정 필요
                }

                binding.runningPace.text = res.pace.toString() // 수정 필요
            }
        } else {
            binding.tvNotRecord.visibility = View.VISIBLE
            binding.recordLayout.visibility = View.GONE

            binding.tvNotRecord.text = response.message
        }

    }

    override fun onGetDailyRecordFailure(message: String) {
        TODO("Not yet implemented")
    }
}