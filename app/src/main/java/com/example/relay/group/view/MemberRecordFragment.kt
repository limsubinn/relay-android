package com.example.relay.group.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.relay.ApplicationClass
import com.example.relay.databinding.FragmentMyRecordBinding
import com.example.relay.mypage.service.MyRecordInterface
import com.example.relay.mypage.service.MyRecordService
import com.example.relay.mypage.models.MonthRecordResponse
import com.example.relay.mypage.view.decorator.*
import com.example.relay.ui.MainActivity
import java.text.SimpleDateFormat


class MemberRecordFragment: Fragment() {
    private var _binding: FragmentMyRecordBinding? = null
    private val binding get() = _binding!!

    private var userIdx = 0L
    private var clubIdx = 0L
    private var hostIdx = 0L
    private var recruitStatus = ""

    private var status = 0 // 거리, 시간, 속도 선택 상태
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    private var curDate = ""

    private var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        if (context != null) {
            super.onAttach(context)
        }
        mainActivity = activity as MainActivity?
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 멤버 -> 레코드
        setFragmentResultListener("go_to_member_record") { requestKey, bundle ->

            curDate = bundle.getString("curDate", "")
            userIdx = bundle.getLong("userIdx", 0L)
            clubIdx = bundle.getLong("clubIdx", 0L)
            hostIdx = bundle.getLong("hostIdx", 0L)
            recruitStatus = bundle.getString("recruitStatus", "")

            if (curDate.isNotEmpty()) {
                val selDate = simpleDateFormat.parse(curDate)
                val year = Integer.parseInt(curDate.substring(0, 4))
                val month = Integer.parseInt(curDate.substring(5, 7))

                // Log.d("month record", "$selDate, $year, $month")

                binding.calendarView.addDecorator(activity?.let { SelectDecorator(selDate, it) })
                binding.calendarView.setCurrentDate(selDate)
            }
        }

        // 탭
        binding.btnDistance.setOnClickListener {
            status = 0
            binding.barDistance.visibility = View.VISIBLE
            binding.barTime.visibility = View.INVISIBLE
            binding.barSpeed.visibility = View.INVISIBLE
        }

        binding.btnTime.setOnClickListener {
            status = 1
            binding.barDistance.visibility = View.INVISIBLE
            binding.barTime.visibility = View.VISIBLE
            binding.barSpeed.visibility = View.INVISIBLE
        }

        binding.btnSpeed.setOnClickListener {
            status = 2
            binding.barDistance.visibility = View.INVISIBLE
            binding.barTime.visibility = View.INVISIBLE
            binding.barSpeed.visibility = View.VISIBLE
        }

        // 날짜 선택
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            val selDate = date.date
            curDate = simpleDateFormat.format(selDate)

            // 레코드 -> 멤버
            parentFragmentManager.setFragmentResult("record_to_member",
                bundleOf("curDate" to curDate, "userIdx" to userIdx, "clubIdx" to clubIdx, "hostIdx" to hostIdx, "recruitStatus" to recruitStatus)
            )
            mainActivity?.groupFragmentChange(5) // 멤버페이지로 이동
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}