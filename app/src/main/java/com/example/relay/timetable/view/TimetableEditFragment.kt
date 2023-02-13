package com.example.relay.timetable.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.R
import com.example.relay.databinding.FragmentTimetableEditBinding
import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.group.models.GroupNewRequest
import com.example.relay.group.service.*
import com.example.relay.timetable.adapter.ScheduleRvAdapter
import com.example.relay.timetable.models.GroupTimetableRes
import com.example.relay.timetable.models.MyTimetableRes
import com.example.relay.timetable.models.Schedule
import com.example.relay.timetable.service.*
import com.example.relay.ui.MainActivity
import kotlinx.android.synthetic.main.dialog_timetable_alert.view.*

class TimetableEditFragment : Fragment(), TimetableGetInterface, TimetablePostInterface, GetUserClubInterface,
    PostClubJoinInInterface, PostNewClubInterface {
    private var viewBinding : FragmentTimetableEditBinding? = null
    private val binding get() = viewBinding!!

    private var scheduleList = mutableListOf<Schedule>()
    private val userIdx = prefs.getLong("userIdx", 0L)
    private var clubIdx = 0L
    private var serverClubIdx = 0L
    private var newGroup:GroupNewRequest ?= null

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

        GetUserClubService(this).tryGetUserClub(userIdx)
        TimetableGetService(this).tryGetMySchedules(userIdx)

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
            } else if (clubIdx < 0){
                Log.d("Timetable", "onViewCreated: 그룹 생성하기")
                newGroup?.timeTable = scheduleList
                PostNewClubService(this).tryPostNewClub(newGroup!!)
            } else if (serverClubIdx == 0L){
                Log.d("Timetable", "onViewCreated: 그룹 신청하기")
                PostClubJoinInService(this).tryPostClubJoinIn(userIdx, clubIdx, scheduleList)
            } else {
                TimetablePostService(this).tryPostMySchedules(userIdx, scheduleList)
            }
        }

        binding.btnBack.setOnClickListener{
            if (serverClubIdx == clubIdx)
                backToTimetableFragment()
            else {
                backToClubMainFragment()
            }
        }

        clubIdxSetting()
    }

    private fun backToTimetableFragment(){
        (activity as MainActivity).timetableFragmentChange(0)
    }

    private fun backToClubMainFragment(){
        parentFragmentManager.setFragmentResult("go_to_main",
            bundleOf("clubIdx" to clubIdx)
        )
        (activity as MainActivity).groupFragmentChange(0) // 그룹 메인으로 이동
    }

    private fun clubIdxSetting(){
        setFragmentResultListener("forJoin") {requestKey, bundle ->
            clubIdx = bundle.getLong("clubIdx", 0L)
            Log.d("Timetable", "clubIdxSetting: in edit 1")
            Log.d("Timetable", "clubIdx: $clubIdx")
        }

        setFragmentResultListener(("forNewGroup")) {requestKey, bundle ->
            clubIdx = bundle.getLong("clubIdx", -2L)
            newGroup =  GroupNewRequest(
                bundle.getString("content", "러너들!"),
                bundle.getFloat("goal", 0F),
                bundle.getString("goalType", "TIME"),
                userIdx,
                "임시 이미지 URL",
                bundle.getInt("level", 0),
                bundle.getInt("maxNum", 8),
                bundle.getString("name", "디폴트"),
                scheduleList
            )
            Log.d("Timetable", "clubIdxSetting: in edit 2")
        }
    }

    override fun onGetUserClubSuccess(response: GroupAcceptedResponse) {
        if (response.code != 4900){
            serverClubIdx = response.result.clubIdx
        }
    }

    override fun onGetUserClubFailure(message: String) {
        TODO("Not yet implemented")
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
        backToTimetableFragment()
    }

    override fun onPostMyTimetableFailure(message: String) {
        Log.d("Timetable", "onPostMyTimetableFailure: ")
    }

    override fun onPostClubJoinInSuccess() {
        backToClubMainFragment()
    }

    override fun onPostClubJoinInFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onPostNewClubSuccess() {
        backToClubMainFragment()
    }

    override fun onPostNewClubFailure(message: String) {
        TODO("Not yet implemented")
    }
}