package com.example.relay.mypage.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.relay.databinding.FragmentMyRecordBinding
import com.example.relay.mypage.service.MyRecordInterface
import com.example.relay.mypage.service.MyRecordService
import com.example.relay.mypage.decorator.Decorator1
import com.example.relay.mypage.decorator.SelectDecorator
import com.example.relay.mypage.decorator.SelectDecorator1
import com.example.relay.mypage.models.MonthRecordResponse
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import java.text.SimpleDateFormat
import java.util.*


class MyRecordFragment: Fragment(), MyRecordInterface {
    private var _binding: FragmentMyRecordBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0

    private var dayDecorator: DayViewDecorator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

        // 마이페이지 -> 마이레코드
        setFragmentResultListener("go_to_my_record") { requestKey, bundle ->

            val curDate = bundle.getString("curDate", "")

            if (curDate.isNotEmpty()) {
                val selDate = simpleDateFormat.parse(curDate)
                val year = Integer.parseInt(curDate.substring(0, 4))
                val month = Integer.parseInt(curDate.substring(5, 7))

                Log.d("month record", "$selDate, $year, $month")

//            binding.calendarView.addDecorator(activity?.let { SelectDecorator(selDate, it) })
//
//            // 월별 기록 불러오기
//            MyRecordService(this).tryGetDailyRecord(year, month)
            }
        }


        val setDate = simpleDateFormat.parse("2023-02-03")

        // 데코레이터 테스트
        dayDecorator = activity?.let { Decorator1(setDate, it) }
        binding.calendarView.addDecorators(dayDecorator)

        // 선택 날짜 데코레이터 달기
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            val selDate = date.date
            if (selDate == setDate) {
                activity?.let {
                    binding.calendarView.removeDecorators()
                    binding.calendarView.addDecorator(SelectDecorator1(selDate, it))
                }
            } else {
                activity?.let {
                    binding.calendarView.addDecorator(SelectDecorator(selDate, it))
                    binding.calendarView.addDecorator(Decorator1(setDate, it))
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onGetMonthRecordSuccess(response: MonthRecordResponse) {
        Log.d("month", "success")
    }

    override fun onGetMonthRecordFailure(message: String) {
        Log.d("month", "fail")
    }
}