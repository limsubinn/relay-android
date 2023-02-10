package com.example.relay.group.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

            MypageService(this).tryGetUserProfile(userIdx)
        }

        // 멤버페이지 -> 멤버리스트
        binding.btnNext.setOnClickListener {
            // 멤버 페이지 -> 멤버 리스트
            parentFragmentManager.setFragmentResult("go_to_member_list",
                bundleOf("clubIdx" to clubIdx, "hostIdx" to hostIdx, "recruitStatus" to recruitStatus)
            )
            Log.d("memberPage2", clubIdx.toString())
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
            mainActivity?.mypageFragmentChange(1);

            // 선택된 날짜로 가로 달력 교체
//                    singleRowCalendar.apply {
//                        setDates(getFutureDatesOfSelectMonth(m))
//                        initialPositionIndex = d-3
//                        init()
//                        select(d-1)
//                    }
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

    private fun getFutureDatesOfSelectMonth(month: Int): List<Date> {
        currentMonth = month
        return getDates(mutableListOf())
    }

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

    override fun onGetUserProfileSuccess(response: UserProfileResponse) {
        if (response.isSuccess) {
            val res = response.result

            binding.profileName.text = "${res.nickname} / ${res.clubName}"
            binding.tvIntro.text = res.statusMsg
            Glide.with(binding.profileImg.context)
                .load(res.imgUrl)
                .into(binding.profileImg)
        }
    }

    override fun onGetUserProfileFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onGetDailyRecordSuccess(response: DailyRecordResponse) {
        TODO("Not yet implemented")
    }

    override fun onGetDailyRecordFailure(message: String) {
        TODO("Not yet implemented")
    }
}