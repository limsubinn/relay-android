package com.example.relay.timetable.view

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.dialog_timetable_alert.view.*

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

        TimetableService(this).tryGetMySchedules(66)

        binding.containerRv.adapter = scheduleRvAdapter

        binding.btnAdd.setOnClickListener{
            scheduleRvAdapter.addEmptyItem()
        }

        binding.btnSave.setOnClickListener{
            scheduleList = scheduleRvAdapter.getUpdatedSchedules()

            if (scheduleList.size == 0){
                val dialogView = layoutInflater.inflate(R.layout.dialog_timetable_alert, null)
                val alertDialog = activity?.let { AlertDialog.Builder(it).create() }

                alertDialog?.setView(dialogView)
                alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog?.show()

                dialogView.btn_check.setOnClickListener{
                    alertDialog?.dismiss()
                }

            } else {
                TimetableService(this).tryPostMySchedules(66, scheduleList)
            }
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

    override fun onGetMyTimetableSuccess(response: MyTimetableRes) {
        if (response.code == 1000) {
            for (item in response.result) {
                scheduleList.add(
                    Schedule(item.day, item.start, item.end, item.goal, item.goalType)
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