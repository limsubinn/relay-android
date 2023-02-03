package com.example.relay.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.relay.databinding.FragmentMyRecordBinding
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import java.text.SimpleDateFormat
import java.util.*


class MyRecordFragment: Fragment() {
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

        val formatter = SimpleDateFormat("yyyy.MM.dd")
        val date = formatter.parse("2023.02.03")

        // 데코레이터 테스트
        dayDecorator = activity?.let { Decorator1(date, it) }
        binding.calendarView.addDecorators(dayDecorator)


    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}