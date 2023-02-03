package com.example.relay.timetable.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.relay.ApplicationClass
import com.example.relay.R
import com.example.relay.databinding.FragmentTimetableBinding
import com.example.relay.group.GetUserClubInterface
import com.example.relay.group.GetUserClubService
import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.timetable.TimetableInterface
import com.example.relay.timetable.TimetableService
import com.example.relay.timetable.models.GroupTimetableRes
import com.example.relay.timetable.models.Schedule
import com.islandparadise14.mintable.model.ScheduleEntity

class TimetableFragment: Fragment(), TimetableInterface, GetUserClubInterface {
    private var viewBinding: FragmentTimetableBinding? = null
    private val binding get() = viewBinding!!
    private val day = arrayOf("일", "월", "화", "수", "목", "금", "토")
    private var clubIdx: Long = 0
    val scheduleList = mutableListOf<Schedule>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTimetableBinding.inflate(layoutInflater)
        with(binding.timetable){
            initTable(day)
            baseSetting(20, 40, 60)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMyTimetable.setOnClickListener{

        }

        binding.btnEdit.setOnClickListener{
            val editFragment = TimetableEditFragment()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container_edit, editFragment)
                .commit()

        }

        // clubIdx 받기
        val userIdx = ApplicationClass.prefs.getLong("userIdx", 0L)
        if (userIdx != 0L) {
            GetUserClubService(this).tryGetUserClub(userIdx)
        } else {
            Toast.makeText(activity, "유저 정보를 받아오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

        TimetableService(this).tryGetGroupSchedules(clubIdx)

        scheduleList.apply {
            add(Schedule(1, "1:00", "8:00", 0, "20"))
            add(Schedule(2, "5:00", "10:00", 0, "20"))
            add(Schedule(4, "13:00", "14:00", 0, "20"))
            add(Schedule(5, "9:00", "5:00", 0, "20"))
            add(Schedule(6, "12:00", "17:00", 0, "20"))
            add(Schedule(0, "11:00", "20:00", 0, "20"))
        }

        ondPostMyTimetableSuccess(scheduleList)
    }

    // 메모리 누수 방지 (fragment 의 생명주기 > view 의 생명주기)
    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    fun ondPostMyTimetableSuccess(scheduleList: MutableList<Schedule>){
        val sList: ArrayList<ScheduleEntity> = ArrayList()
        for (item in scheduleList) {
            val schedule = ScheduleEntity(
                1, //originId,
                "라나", //scheduleName,
                item.day, //ScheduleDay object (MONDAY ~ SUNDAY)
                item.startTime, //startTime format: "HH:mm"
                item.endTime, //endTime  format: "HH:mm"
                "#F54242", //backgroundColor (optional)
                "#FFFFFF" //textColor (optional)
            )
            sList.add(schedule)
        }
        binding.timetable.updateSchedules(sList)
    }

    override fun onGetGroupTimetableSuccess(response: GroupTimetableRes) {

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

    override fun onGetUserClubSuccess(response: GroupAcceptedResponse) {
        if (response.code == 1000)
            clubIdx = response.result.clubIdx
    }

    override fun onGetUserClubFailure(message: String) {
        TODO("Not yet implemented")
    }
}