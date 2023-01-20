package com.example.relay.mypage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.relay.R
import com.example.relay.databinding.FragmentMypageBinding
import com.example.relay.databinding.ItemSelectedCalendarBinding
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import java.util.*

class MypageFragment: Fragment() {
    private val fragmentMyPageBinding: FragmentMypageBinding by lazy {
        FragmentMypageBinding.inflate(layoutInflater)
    }
    private val itemSelectedCalendarBinding: ItemSelectedCalendarBinding by lazy {
        ItemSelectedCalendarBinding.inflate(layoutInflater)
    }
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return fragmentMyPageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set current date to calendar and current month to currentMonth variable
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        // calendar view manager is responsible for our displaying logic
        val myCalendarViewManager = object :
            CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                val cal = Calendar.getInstance()
                cal.time = date
                return if (isSelected) // 선택o 날짜
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> R.layout.item_selected_calendar

                    }
                else // 선택x 날짜
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> R.layout.item_unselected_calendar
                    }
            }
            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                // bind data to calendar view

                // holder.itemView.tv_date_calendar_item.text = DateUtils.getDayNumber(date)
                // holder.itemView.tv_month_calendar_item.text = DateUtils.getDay3LettersName(date)
            }
        }

        val rowCalendarChangesObserver = object: CalendarChangesObserver {
            @SuppressLint("SetTextI18n")
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                super.whenSelectionChanged(isSelected, position, date)
            }
        }
        val rowSelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                return true
            }
        }
        fragmentMyPageBinding.selCalendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = rowCalendarChangesObserver
            calendarSelectionManager = rowSelectionManager
            setDates(getFutureDatesOfCurrentMonth())
            init()
        }

    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        currentMonth = calendar[Calendar.MONTH]
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