package com.example.relay.timetable.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import com.example.relay.ApplicationClass
import com.example.relay.databinding.FragmentTimetableEditMainBinding
import com.example.relay.timetable.models.GroupTimetableRes
import com.example.relay.timetable.models.MyTimetableRes
import com.example.relay.timetable.service.TimetableGetInterface
import com.example.relay.timetable.service.TimetableGetService
import com.example.relay.ui.MainActivity
import com.islandparadise14.mintable.model.ScheduleEntity

class TimetableEditMainFragment : Fragment(), TimetableGetInterface {
    private var _binding: FragmentTimetableEditMainBinding ?= null
    private val binding get() = _binding!!

    private val day = arrayOf("일", "월", "화", "수", "목", "금", "토")
    private val colorCode =  arrayOf("#FE0000", "#01A6EA", "#FFAD01", "#FFDD00", "#BBDA00", "#F71873", "#6DD0E7", "#84C743")
    private var userIdx = ApplicationClass.prefs.getLong("userIdx", 0L)
    private var clubIdx = 0L

    private var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = activity as MainActivity?
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimetableEditMainBinding.inflate(inflater, container, false)
        with(binding.timetable){
            baseSetting(35, 40, 40)
            initTable(day)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*
        parentFragmentManager
            .beginTransaction()
            .add(R.id.container_edit, TimetableEditFragment())
            .commit()
*/
        binding.btnBack.setOnClickListener{
            parentFragmentManager
                .beginTransaction()
                .remove(this)
                .commitAllowingStateLoss()
            (activity as MainActivity).timetableChangeFragment(0)
        }

        clubIdxSetting()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun clubIdxSetting(){
        setFragmentResultListener("go_to_edit_main_timetable") {requestKey, bundle ->
            clubIdx = bundle.getLong("clubIdx", 0L)
            TimetableGetService(this).tryGetGroupSchedules(clubIdx)
        }
    }

    override fun onGetGroupTimetableSuccess(response: GroupTimetableRes) {
        if (response.code == 1000){
            val clubMemList = response.result
            val sList: ArrayList<ScheduleEntity> = ArrayList()
            for ((index, mem) in clubMemList.withIndex()){
                val color = colorCode[index]
                for (s in mem.timeTables){
                    val schedule = ScheduleEntity(
                        1,
                        mem.nickName,
                        s.day,
                        s.start,
                        s.end,
                        color,
                        "#FFFFFF"
                    )
                    sList.add(schedule)
                }
            }
            binding.timetable.updateSchedules(sList)
        } else
            Log.d("Timetable", "onGetGroupTimetableSuccess: code-${response.code}")
    }

    override fun onGetGroupTimetableFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onGetMyTimetableSuccess(response: MyTimetableRes) {
        TODO("Not yet implemented")
    }

    override fun onGetMyTimetableFailure(message: String) {
        TODO("Not yet implemented")
    }
}