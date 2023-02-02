package com.example.relay.group.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.R
import com.example.relay.databinding.FragmentGroupCreateBinding
import com.example.relay.group.GetUserClubInterface
import com.example.relay.group.GetUserClubService
import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.ui.MainActivity
import kotlinx.android.synthetic.main.dialog_goal_time.view.*
import kotlinx.android.synthetic.main.dialog_goal_type.view.*

class GroupCreateFragment: Fragment(), GetUserClubInterface {
    private var _binding: FragmentGroupCreateBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentGroupCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goalType = arrayOf("목표 없음", "시간", "거리", "스피드")

        // 모집중 상태
        binding.swRecruitStatus.isChecked = true

        // 스위치 체크
        binding.swRecruitStatus.setOnCheckedChangeListener { p0, isChecked ->
            if (isChecked) {
                binding.tvRecruitStatus.text = "모집중"
            } else {
                binding.tvRecruitStatus.text = "모집완료"
            }
        }

        // 그룹 삭제하기 화면에 안 보이게
        binding.line3.visibility = View.GONE
        binding.btnDelete.visibility = View.GONE


        // 목표치 설정 (type)
        binding.btnGoalType.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_goal_type, null)
            val alertDialog = activity?.let { AlertDialog.Builder(it).create() }

            alertDialog?.setView(dialogView)
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.show()

            val itemView = dialogView.list_dialog_item

            itemView.adapter =
               activity?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_list_item_1, goalType) }

            itemView.onItemClickListener = AdapterView.OnItemClickListener {
                    parent,
                    view,
                    position,
                    id ->
                binding.tvGoalType.text = itemView.adapter.getItem(position).toString()
                if (position == 0) {
                    binding.tvGoalValue.text = "----"
                } else if (position == 1) {
                    binding.tvGoalValue.text = "00 : 00 : 00"
                } else {
                    binding.tvGoalValue.text = "00 : 00"
                }
                alertDialog?.dismiss()
            }
        }

        // 목표치 설정 (value)
        binding.btnGoalValue.setOnClickListener {
            if (binding.tvGoalType.text.equals("시간")) {
                val dialogView = layoutInflater.inflate(R.layout.dialog_goal_time, null)
                val alertDialog = activity?.let { AlertDialog.Builder(it).create() }

                alertDialog?.setView(dialogView)
                alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog?.show()

                // 최대, 최소값 설정
                dialogView.np_hour.minValue = 0
                dialogView.np_hour.maxValue = 23
                dialogView.np_min.minValue = 0
                dialogView.np_min.maxValue = 59
                dialogView.np_sec.minValue = 0
                dialogView.np_sec.maxValue = 59

                // 기본값 설정
                dialogView.np_hour.value = Integer.parseInt(binding.tvGoalValue.text.substring(0, 2))
                dialogView.np_min.value = Integer.parseInt(binding.tvGoalValue.text.substring(5, 7))
                dialogView.np_sec.value = Integer.parseInt(binding.tvGoalValue.text.substring(10, 12))

                // 저장 버튼
                dialogView.btn_save.setOnClickListener {
                    var hour = dialogView.np_hour.value.toString().padStart(2, '0')
                    var min = dialogView.np_min.value.toString().padStart(2, '0')
                    var sec = dialogView.np_sec.value.toString().padStart(2, '0')

                    binding.tvGoalValue.text = "${hour} : ${min} : ${sec}"
                    alertDialog?.dismiss()
                }

                // 취소 버튼
                dialogView.btn_cancel.setOnClickListener {
                    alertDialog?.dismiss()
                }
            }
        }


        // 유저가 속한 그룹의 이름 가져오기
        val userIdx = prefs.getLong("userIdx", 0L)
        if (userIdx != 0L) {
            GetUserClubService(this).tryGetUserClub(userIdx)
        } else {
            Toast.makeText(activity, "유저 정보를 받아오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

        // 취소 버튼
        binding.btnBack.setOnClickListener {
            mainActivity?.groupFragmentChange(1) // 그룹 리스트로 이동
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onGetUserClubSuccess(response: GroupAcceptedResponse) {
        binding.btnNext.setOnClickListener {
            if (response.code != 2008) {
                // 가입한 그룹이 있으면 그룹 생성하기 거부
                mainActivity?.groupFragmentChange(4)
            } else {
                // 가입한 그룹 x (수정 예정)
                Toast.makeText(activity, "!!!그룹 생성 가능!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onGetUserClubFailure(message: String) {
        TODO("Not yet implemented")
    }
}