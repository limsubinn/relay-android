package com.example.relay.timetable.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.relay.R
import com.example.relay.databinding.FragmentTimetableEditBinding
import com.example.relay.timetable.ScheduleRvAdapter
import com.example.relay.timetable.TimetableInterface
import com.example.relay.timetable.TimetableService
import com.example.relay.timetable.models.GroupTimetableRes
import com.example.relay.timetable.models.MyTimetableRes
import com.example.relay.timetable.models.Schedule
import com.islandparadise14.mintable.model.ScheduleEntity

class TimetableEditFragment : Fragment(), TimetableInterface {
    private var viewBinding : FragmentTimetableEditBinding? = null
    private val binding get() = viewBinding!!
    private var scheduleList = mutableListOf<Schedule>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTimetableEditBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scheduleRvAdapter = ScheduleRvAdapter(requireActivity(), scheduleList)

        TimetableService(this).tryGetMySchedules(53)

        binding.containerRv.adapter = scheduleRvAdapter

        binding.btnAdd.setOnClickListener{
            scheduleRvAdapter.addEmptyItem()
        }

        binding.btnSave.setOnClickListener{
            // 테스트 코드
            scheduleList = scheduleRvAdapter.getUpdatedSchedules()
            // TimetableService(this).tryPostMySchedules(1, scheduleList)

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

    private fun goalTypeToKor(goalType:String): String{
        val kor = when(goalType){
            "DISTANCE" -> "거리"
            "TIME" -> "시간"
            else -> "목표없음"
        }
        return kor
    }

    override fun onGetMyTimetableSuccess(response: MyTimetableRes) {
        if (response.code == 1000) {
            for (item in response.result) {
                scheduleList.add(
                    Schedule(item.day.toInt(), item.start, item.end, item.goal.toInt(), goalTypeToKor(item.goalType))
                )
            }
            binding.containerRv.layoutManager = LinearLayoutManager(context)
        } else
            Log.d("Timetable", "onGetMyTimetableSuccess: code-${response.code}")
    }

    override fun onGetMyTimetableFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onGetGroupTimetableSuccess(response: GroupTimetableRes) {
        TODO("Not yet implemented")
    }

    override fun onGetGroupTimetableFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onPostMyTimetableSuccess() {
        TODO("Not yet implemented")
    }

    override fun onPostMyTimetableFailure(message: String) {
        TODO("Not yet implemented")
    }
}