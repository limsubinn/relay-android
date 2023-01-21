package com.example.relay.mypage

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.example.relay.R
import com.example.relay.databinding.FragmentMypageBinding
import com.example.relay.databinding.ItemSelectedCalendarBinding
import com.example.relay.databinding.ItemUnselectedCalendarBinding
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import java.util.*


class MypageFragment: Fragment() {
    private val binding: FragmentMypageBinding by lazy {
        FragmentMypageBinding.inflate(layoutInflater)
    }
    private lateinit var itemSelectedCalendarBinding: ItemSelectedCalendarBinding
    private lateinit var itemUnselectedCalendarBinding: ItemUnselectedCalendarBinding
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        itemSelectedCalendarBinding = ItemSelectedCalendarBinding.inflate(layoutInflater, container, false)
        itemUnselectedCalendarBinding = ItemUnselectedCalendarBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val c = Calendar.getInstance()

        // 우선 기본 선택은 현재 날짜로 설정
        // 가로 달력이 해결되면 기본 선택 위치 조정
        // 달력 디자인 수정 필요
        binding.btnCalendar.setOnClickListener {
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)
            val dlg = DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    Log.d("cal", "${year}년 ${month+1}월 ${dayOfMonth}일")
                }
            }, year, month, date)
            dlg.show()
        }

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
        binding.selCalendar.apply {
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