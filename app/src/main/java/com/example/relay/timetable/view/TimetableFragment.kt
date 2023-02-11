package com.example.relay.timetable.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.databinding.FragmentTimetableBinding
import com.example.relay.group.service.GetUserClubInterface
import com.example.relay.group.service.GetUserClubService
import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.timetable.models.GroupTimetableRes
import com.example.relay.timetable.models.MyTimetableRes
import com.example.relay.timetable.service.TimetableGetInterface
import com.example.relay.timetable.service.TimetableGetService
import com.example.relay.ui.MainActivity
import com.islandparadise14.mintable.model.ScheduleEntity

class TimetableFragment: Fragment(), TimetableGetInterface, GetUserClubInterface {
    private var viewBinding: FragmentTimetableBinding? = null
    private val binding get() = viewBinding!!

    private val day = arrayOf("일", "월", "화", "수", "목", "금", "토")
    private val myColor = "#FE0000"
    private val colorCode =  arrayOf("#FE0000", "#01A6EA", "#FFAD01", "#FFDD00", "#BBDA00", "#F71873", "#6DD0E7", "#84C743")

    private val userIdx = prefs.getLong("userIdx", 0L)
    private var clubIdx: Long = 0
    private var clubName: String = "팀"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTimetableBinding.inflate(layoutInflater)
        with(binding.timetable){
            baseSetting(35, 40, 40)
            initTable(day)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMyTimetable.setOnClickListener{
            if (binding.tvTitle.text == "팀 시간표") {
                binding.timetable.initTable(day)
                TimetableGetService(this).tryGetMySchedules(userIdx)
                binding.tvTitle.text = "개인 시간표"
            } else {
                binding.timetable.initTable(day)
                TimetableGetService(this).tryGetGroupSchedules(clubIdx)
                binding.tvTitle.text = "팀 시간표"
            }
        }

        binding.btnEdit.setOnClickListener{
            parentFragmentManager.setFragmentResult("go_to_edit_main_timetable",
                bundleOf("clubIdx" to clubIdx, "clubName" to clubName)
            )
            (activity as MainActivity).timetableFragmentChange(1)
        }

        GetUserClubService(this).tryGetUserClub(userIdx)
    }

    // 메모리 누수 방지 (fragment 의 생명주기 > view 의 생명주기)
    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
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
        if (response.code == 1000){
            val sList: ArrayList<ScheduleEntity> = ArrayList()
            for (item in response.result){
                val schedule = ScheduleEntity(
                    1,
                    "수정",
                    item.day,
                    item.start,
                    item.end,
                    myColor,
                    "#FFFFFF"
                )
                sList.add(schedule)
            }
            binding.timetable.updateSchedules(sList)
        }
    }

    override fun onGetMyTimetableFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onGetUserClubSuccess(response: GroupAcceptedResponse) {
        if (response.code == 1000) {
            clubIdx = response.result.clubIdx
            clubName = response.result.name
            binding.tvTitle.text = clubName + " 팀"
            TimetableGetService(this).tryGetGroupSchedules(clubIdx)
        } else {
            Log.d("Timetable", "TimetableFragment 클럽정보 받아오기 : ${response.code}")
            binding.btnEdit.visibility = View.GONE
            binding.btnMyTimetable.visibility = View.GONE
        }
    }

    override fun onGetUserClubFailure(message: String) {
        TODO("Not yet implemented")
    }
}