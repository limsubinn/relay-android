package com.example.relay.mypage.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.relay.ApplicationClass
import com.example.relay.databinding.FragmentMyRecordBinding
import com.example.relay.mypage.service.MyRecordInterface
import com.example.relay.mypage.service.MyRecordService
import com.example.relay.mypage.models.MonthRecordResponse
import com.example.relay.mypage.view.decorator.*
import com.example.relay.ui.MainActivity
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import java.text.SimpleDateFormat


class MyRecordFragment: Fragment(), MyRecordInterface {
    private var _binding: FragmentMyRecordBinding? = null
    private val binding get() = _binding!!

    private val userIdx = ApplicationClass.prefs.getLong("userIdx", 0L)
    private var status = 0 // 거리, 시간, 속도 선택 상태
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    private var curDate = ""
    private var year = 0
    private var month = 0

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
        _binding = FragmentMyRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 마이페이지 -> 마이레코드
        setFragmentResultListener("go_to_my_record") { requestKey, bundle ->

            curDate = bundle.getString("curDate", "")

            if (curDate.isNotEmpty()) {
                val selDate = simpleDateFormat.parse(curDate)
                year = Integer.parseInt(curDate.substring(0, 4))
                month = Integer.parseInt(curDate.substring(5, 7))

                // Log.d("month record", "$selDate, $year, $month")

                binding.calendarView.addDecorator(activity?.let { SelectDecorator(selDate, it) })
                binding.calendarView.setCurrentDate(selDate)

            // 월별 기록 불러오기
            MyRecordService(this).tryGetDailyRecord(year, month, userIdx)
            }
        }

        // 탭
        binding.btnDistance.setOnClickListener {
            status = 0

            binding.barDistance.visibility = View.VISIBLE
            binding.barTime.visibility = View.INVISIBLE
            binding.barSpeed.visibility = View.INVISIBLE

            // 월별 기록 불러오기
            MyRecordService(this).tryGetDailyRecord(year, month, userIdx)
        }

        binding.btnTime.setOnClickListener {
            status = 1

            binding.barDistance.visibility = View.INVISIBLE
            binding.barTime.visibility = View.VISIBLE
            binding.barSpeed.visibility = View.INVISIBLE

            // 월별 기록 불러오기
            MyRecordService(this).tryGetDailyRecord(year, month, userIdx)
        }

        binding.btnSpeed.setOnClickListener {
            status = 2

            binding.barDistance.visibility = View.INVISIBLE
            binding.barTime.visibility = View.INVISIBLE
            binding.barSpeed.visibility = View.VISIBLE

            // 월별 기록 불러오기
            MyRecordService(this).tryGetDailyRecord(year, month, userIdx)
        }

        // 날짜 선택
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            val selDate = date.date
            curDate = simpleDateFormat.format(selDate)

            // 마이레코드 -> 마이페이지
            parentFragmentManager.setFragmentResult("record_to_mypage",
                bundleOf("curDate" to curDate)
            )
            mainActivity?.mypageFragmentChange(0) // 마이페이지로 이동
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onGetMonthRecordSuccess(response: MonthRecordResponse) {
        if ((response.isSuccess) && (response.result.isNotEmpty())) {
            val res = response.result

            var date = ""
            var value = 0.0

            for (i in res) {
                when (status) {
                    0 -> { // 거리
                        for (i in res) {
                            date = i.date
                            value = i.totalDist

                            if ((value > 0.0) && (value < 4.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator1(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator1(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if ((value >= 4.0) && (value < 8.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator2(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator2(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if ((value >= 8.0) && (value < 12.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator3(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator3(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if ((value >= 12.0) && (value < 16.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator4(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator4(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if ((value >= 16.0) && (value < 20.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator5(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator5(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if (value >= 20.0) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator6(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator6(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            }
                        }
                    }
                    1 -> { // 시간
                        for (i in res) {
                            date = i.date
                            value = i.totalTime / 60 // 분 단위

                            if ((value > 0.0) && (value < 20.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator1(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator1(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if ((value >= 20.0) && (value < 40.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator2(
                                            simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator2(
                                            simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if ((value >= 40.0) && (value < 60.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator3(
                                            simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator3(
                                            simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if ((value >= 60.0) && (value < 80.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator4(
                                            simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator4(
                                            simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if ((value >= 80.0) && (value < 100.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator5(
                                            simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator5(
                                            simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if (value >= 100) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator6(
                                            simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator6(
                                            simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            }
                        }
                    }
                    2 -> { // 속도
                        for (i in res) {
                            date = i.date
                            value = i.avgPace

                            if ((value > 0.0) && (value < 2.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator1(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator1(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if ((value >= 2.0) && (value < 4.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator2(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator2(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if ((value >= 4.0) && (value < 6.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator3(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator3(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if ((value >= 6.0) && (value < 8.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator4(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator4(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if ((value >= 8.0) && (value < 10.0)) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator5(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator5(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            } else if (value >= 10.0) {
                                if (date == curDate) {
                                    binding.calendarView.addDecorator(activity?.let {
                                        SelectDecorator6(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                } else {
                                    binding.calendarView.addDecorator(activity?.let {
                                        Decorator6(simpleDateFormat.parse(date),
                                            it
                                        )
                                    })
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onGetMonthRecordFailure(message: String) {
        Toast.makeText(activity, "사용자의 기록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
    }
}