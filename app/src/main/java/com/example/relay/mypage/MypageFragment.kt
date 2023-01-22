package com.example.relay.mypage

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.relay.R
import com.example.relay.databinding.FragmentMypageBinding
import com.example.relay.databinding.ItemSelectedCalendarBinding
import com.example.relay.databinding.ItemUnselectedCalendarBinding
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import java.time.Month
import java.util.*


class MypageFragment: Fragment() {
    private val binding: FragmentMypageBinding by lazy {
        FragmentMypageBinding.inflate(layoutInflater)
    }

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val today = GregorianCalendar()
        var year: Int = today.get(Calendar.YEAR)
        var month: Int = today.get(Calendar.MONTH)
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
                // findViewId 안 쓰고 싶은데 자꾸 바인딩이 안 된다 ㅠㅠ,,,,,
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
            // includeCurrentDate = true
            setDates(getFutureDatesOfCurrentMonth())
            initialPositionIndex = date-3
            init()
            select(date-1) // 오늘 날짜 선택
        }

        // 달력 디자인 수정 필요
        // 프래그먼트가 재개됐을 때의 액션 구현 필요
        binding.btnCalendar.setOnClickListener {
            val dlg = DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, y: Int, m: Int, d: Int) {
                    date = d
                    month = m
                    year = y

                    // 선택된 날짜로 가로 달력 교체
                    singleRowCalendar.setDates(getFutureDatesOfSelectMonth(m))
                    singleRowCalendar.initialPositionIndex = d-3
                    singleRowCalendar.init()
                    singleRowCalendar.select(d-1)
                }
            }, year, month, date)
            dlg.show()
        }
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
}