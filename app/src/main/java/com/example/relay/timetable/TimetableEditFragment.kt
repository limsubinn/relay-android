package com.example.relay.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.relay.R
import com.example.relay.databinding.FragmentTimetableEditBinding
import com.example.relay.timetable.models.Schedule
import java.sql.Time

class TimetableEditFragment : Fragment(), TimetableInterface {
    private var viewBinding : FragmentTimetableEditBinding? = null
    private val binding get() = viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTimetableEditBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var scheduleList = mutableListOf<Schedule>()
        val scheduleRvAdapter = ScheduleRvAdapter(requireActivity(), scheduleList)

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
            // 테스트 코드
            scheduleList = scheduleRvAdapter.getUpdatedSchedules()
            TimetableService(this).tryPostMySchedules(1,1, scheduleList)

            val emptyFragment = TimetableEmptyFragment()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container_edit, emptyFragment)
                .commit()
        }

        binding.btnBack.setOnClickListener{
            // 빈 Fragment 로 변경
            val emptyFragment = TimetableEmptyFragment()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container_edit, emptyFragment)
                .commit()
        }
    }

    override fun onPostMyTimetableSuccess() {
        TODO("Not yet implemented")
    }

    override fun onPostMyTimetableFailure(message: String) {
        TODO("Not yet implemented")
    }
}