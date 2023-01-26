package com.example.relay.timetable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.relay.R
import com.example.relay.databinding.FragmentTimetableEditBinding
import com.example.relay.timetable.models.Schedule

class TimetableEditFragment : Fragment() {
    private var viewBinding : FragmentTimetableEditBinding? = null
    private val binding get() = viewBinding!!
    val scheduleList = mutableListOf<Schedule>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTimetableEditBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scheduleRvAdapter = ScheduleRvAdapter(scheduleList)

        // 테스트 더미
        scheduleList.apply {
            add(Schedule("월", "1:00", "8:00", "20"))
            add(Schedule("화", "5:00", "10:00", "20"))
            add(Schedule("수", "13:00", "14:00", "20"))
            add(Schedule("목", "9:00", "5:00", "20"))
            add(Schedule("금", "12:00", "17:00", "20"))
            add(Schedule("일", "11:00", "20:00", "20"))
        }

        binding.containerRv.adapter = scheduleRvAdapter
        binding.containerRv.layoutManager = LinearLayoutManager(context)

        binding.btnAdd.setOnClickListener{
            scheduleRvAdapter.addEmptyItem()
        }

        binding.btnSave.setOnClickListener{
            // 통신 코드 추가 필요

            // 빈 Fragment 로 변경
            val emptyFragment = TimetableEmptyFragment()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container_edit, emptyFragment)
                .commit()
        }
    }
}