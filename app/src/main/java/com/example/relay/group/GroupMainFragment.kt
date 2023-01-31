package com.example.relay.group

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.R
import com.example.relay.databinding.FragmentGroupMainBinding
import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.ui.MainActivity
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import java.util.*

class GroupMainFragment: Fragment(), GroupMainInterface {
    private var _binding: FragmentGroupMainBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0

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

        val today = GregorianCalendar()
//        var year: Int = today.get(Calendar.YEAR)
//        var month: Int = today.get(Calendar.MONTH)
        var date: Int = today.get(Calendar.DATE)

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

            val clubIdx = bundle.getLong("clubIdx")
            val content = bundle.getString("content")
            val imgURL = bundle.getString("imgURL")
            val name = bundle.getString("name")
            val recruitStatus = bundle.getString("recruitStatus")

            if (recruitStatus.equals("finished")) {
                binding.btnJoinTeam.visibility = View.GONE
            } else {
                binding.btnJoinTeam.visibility = View.VISIBLE
            }

            binding.profileTeam.text = name
            binding.tvIntro.text = content
//            Glide.with(binding.profileImg.context)
//                .load(imgURL)
//                .into(binding.profileImg)

            // 모두 보기 버튼
            binding.btnTeamAll.setOnClickListener {
                // 메인 -> 멤버
                parentFragmentManager.setFragmentResult("main_to_member",
                    bundleOf("clubIdx" to clubIdx, "content" to content,
                        "imgURL" to imgURL, "name" to name, "recruitStatus" to recruitStatus))
                mainActivity?.groupFragmentChange(2) // 팀원 보기로 이동
            }
        }

        // 사용자 그룹명 가져오기
        Handler(Looper.getMainLooper()).postDelayed({
            val profileIdx = prefs.getLong("profileIdx", 0L)
            if (binding.profileTeam.text.isEmpty()) {
                GroupMainService(this).tryGetUserClub(profileIdx)
            }
        }, 10)

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
        val res = response.result

        // 유저가 가입된 그룹이 존재하면 화면에 띄우고, 존재하지 않으면 그룹의 목록을 보여준다.
        // !!! 현재 들어간 그룹이 없다는 문구가 띄워진 화면 기획 완료되면 수정할 예정 !!!
        if ((res != null) && (res.clubIdx != 0L)) {
            binding.tvTeam.text = res.name
            binding.btnJoinTeam.visibility = View.GONE

            // 모두 보기 버튼
            binding.btnTeamAll.setOnClickListener {
                mainActivity?.groupFragmentChange(2)

                parentFragmentManager.setFragmentResult("main_to_member",
                    bundleOf("clubIdx" to res.clubIdx)
                )
                mainActivity?.groupFragmentChange(2) // 팀원 보기로 이동
            }
        } else {
            mainActivity?.groupFragmentChange(1)
        }
    }

    override fun onGetUserClubFailure(message: String) {
        TODO("Not yet implemented")
    }
}