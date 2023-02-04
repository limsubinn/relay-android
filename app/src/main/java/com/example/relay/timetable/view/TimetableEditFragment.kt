package com.example.relay.timetable.view

import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
import kotlinx.android.synthetic.main.dialog_goal_time.view.*
import kotlinx.android.synthetic.main.dialog_people_cnt.view.*
import kotlinx.android.synthetic.main.dialog_people_cnt.view.btn_cancel
import kotlinx.android.synthetic.main.dialog_people_cnt.view.btn_save
import kotlinx.android.synthetic.main.item_rv_edit_table.view.*
import java.util.*

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

        TimetableService(this).tryGetMySchedules(59)

        binding.containerRv.adapter = scheduleRvAdapter

        binding.btnAdd.setOnClickListener{
            val dialogView = layoutInflater.inflate(R.layout.dialog_people_cnt, null)
            val alertDialog = activity?.let { AlertDialog.Builder(it).create() }

            alertDialog?.setView(dialogView)
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.show()

            dialogView.tv_people.text = "요일"

            val dayList = arrayOf("일", "월", "화", "수", "목", "금", "토")

            dialogView.np_people.displayedValues = dayList
            dialogView.np_people.minValue = 0
            dialogView.np_people.maxValue = 6

            val index = dayList.indexOf("월")
            dialogView.np_people.value = index

            // 저장 버튼
            dialogView.btn_save.setOnClickListener {
                val day = dialogView.np_people.value
                var start = "00:00"
                var end = "00:30"
                alertDialog?.dismiss()

                val cal = Calendar.getInstance()

                TimePickerDialog(context, { timePicker, h, m ->
                    var start = "$h:$m"
                }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true ).show()

                TimePickerDialog(context, { timePicker, h, m ->
                    val end = "$h:$m"
                }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true ).show()

                scheduleRvAdapter.addItem(day, start, end, 0, "목표없음")
            }

            // 취소 버튼
            dialogView.btn_cancel.setOnClickListener {
                alertDialog?.dismiss()
            }
        }

        binding.btnSave.setOnClickListener{
            // 테스트 코드
            scheduleList = scheduleRvAdapter.getUpdatedSchedules()
            val sList = mutableListOf<Schedule>()
            for ((index,item) in scheduleList.withIndex()){
                scheduleList[index].start = scheduleList[index].start + ":00"
                scheduleList[index].end = scheduleList[index].end + ":00"
                scheduleList[index].goalType = goalTypeToEn(scheduleList[index].goalType)
            }
            TimetableService(this).tryPostMySchedules(59, scheduleList)

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

    private fun goalTypeToEn(goalType:String): String{
        val kor = when(goalType){
            "거리" -> "DISTANCE"
            "시간" -> "TIME"
            else -> "목표없음"
        }
        return kor
    }

    override fun onGetMyTimetableSuccess(response: MyTimetableRes) {
        if (response.code == 1000) {
            for (item in response.result) {
                scheduleList.add(
                    Schedule(item.day.toInt(), item.start.substring(0, 5), item.end.substring(0, 5), item.goal.toInt(), goalTypeToKor(item.goalType))
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
        Log.d("Timetable", "onPostMyTimetableSuccess: ")
    }

    override fun onPostMyTimetableFailure(message: String) {
        Log.d("Timetable", "onPostMyTimetableFailure: ")
    }
}