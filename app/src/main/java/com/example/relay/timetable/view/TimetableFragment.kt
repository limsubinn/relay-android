package com.example.relay.timetable.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.relay.R
import com.example.relay.databinding.FragmentTimetableBinding
import com.example.relay.group.service.GetUserClubInterface
import com.example.relay.group.service.GetUserClubService
import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.timetable.service.TimetableInterface
import com.example.relay.timetable.service.TimetableService
import com.example.relay.timetable.models.GroupTimetableRes
import com.example.relay.timetable.models.MyTimetableRes
import com.islandparadise14.mintable.model.ScheduleEntity

class TimetableFragment: Fragment(), TimetableInterface, GetUserClubInterface {
    private var viewBinding: FragmentTimetableBinding? = null
    private val binding get() = viewBinding!!
    private val day = arrayOf("일", "월", "화", "수", "목", "금", "토")
    private val myColor = "#FE0000"
    private val colorCode =  arrayOf("#FE0000", "#01A6EA", "#FFAD01", "#FFDD00", "#BBDA00", "#F71873", "#6DD0E7", "#84C743")
    private var clubIdx: Long = 0

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
                TimetableService(this).tryGetMySchedules(66)
                binding.tvTitle.text = "개인 시간표"
            } else {
                binding.timetable.initTable(day)
                TimetableService(this).tryGetGroupSchedules(clubIdx)
                binding.tvTitle.text = "팀 시간표"
            }
        }

        binding.btnEdit.setOnClickListener{
            val editFragment = TimetableEditFragment()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container_edit, editFragment)
                .commit()
        }

        /* clubIdx 받기
         val userIdx = ApplicationClass.prefs.getLong("userIdx", 0L)    // prefs 저장된 값 없음, 임의값은 동작
        if (userIdx != 0L) {
            GetUserClubService(this).tryGetUserClub(userIdx)
        } else {
            Toast.makeText(activity, "유저 정보를 받아오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
        } */

        GetUserClubService(this).tryGetUserClub(66)
    }

    // 메모리 누수 방지 (fragment 의 생명주기 > view 의 생명주기)
    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    @SuppressLint("LogNotTimber")
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
                        s.day.toInt(),
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
                    item.day.toInt(),
                    item.start,
                    item.end,
                    myColor,
                    "#FFFFFF"
                )
                sList.add(schedule)
            }
            binding.timetable.updateSchedules(sList)
        } else
            Log.d("Timetable", "onGetMyTimetableSuccess: code-${response.code}")
    }

    override fun onGetMyTimetableFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onPostMyTimetableFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onPostMyTimetableSuccess() {
        TODO("Not yet implemented")
    }

    @SuppressLint("LogNotTimber")
    override fun onGetUserClubSuccess(response: GroupAcceptedResponse) {
        if (response.code == 1000) {
            clubIdx = response.result.clubIdx
            TimetableService(this).tryGetGroupSchedules(clubIdx)
            Log.d("Timetable", "onGetUserClubSuccess: clubIdx-$clubIdx")
        }
    }

    override fun onGetUserClubFailure(message: String) {
        TODO("Not yet implemented")
    }
}