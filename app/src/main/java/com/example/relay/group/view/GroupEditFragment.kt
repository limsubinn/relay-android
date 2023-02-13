package com.example.relay.group.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.R
import com.example.relay.databinding.FragmentGroupCreateBinding
import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.group.models.GroupEditRequest
import com.example.relay.group.models.GroupInfoResponse
import com.example.relay.group.service.*
import com.example.relay.ui.MainActivity
import kotlinx.android.synthetic.main.dialog_goal_km.view.*
import kotlinx.android.synthetic.main.dialog_goal_time.view.*
import kotlinx.android.synthetic.main.dialog_goal_time.view.btn_cancel
import kotlinx.android.synthetic.main.dialog_goal_time.view.btn_save
import kotlinx.android.synthetic.main.dialog_goal_type.view.*
import kotlinx.android.synthetic.main.dialog_people_cnt.view.*
import kotlinx.android.synthetic.main.dialog_question.view.*
import kotlinx.android.synthetic.main.fragment_group_main.view.*
import java.text.DecimalFormat

class GroupEditFragment: Fragment(), GetClubDetailInterface, PatchClubInterface {
    private var _binding: FragmentGroupCreateBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null

    private var goal: Float = 0F
    private var clubIdx = 0L
    private var curDate = ""

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

        // 스위치 체크
        binding.swRecruitStatus.setOnCheckedChangeListener { p0, isChecked ->
            if (isChecked) {
                binding.tvRecruitStatus.text = "모집중"
            } else {
                binding.tvRecruitStatus.text = "모집완료"
            }
        }

        binding.line3.visibility = View.VISIBLE
        binding.btnDelete.visibility = View.VISIBLE
        binding.tvGroupCreate.text = "그룹 수정"
        binding.btnNext.text = "완료"


        // 목표치 설정 (type)
        binding.btnGoalType.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_goal_type, null)
            val alertDialog = activity?.let { AlertDialog.Builder(it).create() }

            alertDialog?.setView(dialogView)
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.show()

            val itemView = dialogView.list_dialog_item
            val goalType = arrayOf("목표 없음", "시간", "거리")

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
                    binding.tvGoalValue.text = "00.00km"
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
                dialogView.np_hour.maxValue = 3
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

                    if ((hour == "00" && min == "00" && sec == "00")) {
                        Toast.makeText(activity, "시간을 설정해주세요!", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.tvGoalValue.text = "${hour} : ${min} : ${sec}"
                        alertDialog?.dismiss()
                    }
                }

                // 취소 버튼
                dialogView.btn_cancel.setOnClickListener {
                    alertDialog?.dismiss()
                }
            } else if ((binding.tvGoalType.text.equals("거리"))) {
                val dialogView = layoutInflater.inflate(R.layout.dialog_goal_km, null)
                val alertDialog = activity?.let { AlertDialog.Builder(it).create() }

                alertDialog?.setView(dialogView)
                alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog?.show()

                dialogView.tv_title.text = "거리 / km"

                // 최대, 최소값 설정
                dialogView.np1.minValue = 0
                dialogView.np1.maxValue = 40
                dialogView.np2.minValue = 0
                dialogView.np2.maxValue = 99

                // 기본값 설정
                dialogView.np1.value = Integer.parseInt(binding.tvGoalValue.text.substring(0, 2))
                dialogView.np2.value = Integer.parseInt(binding.tvGoalValue.text.substring(3, 5))

                // 저장 버튼
                dialogView.btn_save.setOnClickListener {
                    var n1 = dialogView.np1.value.toString().padStart(2, '0')
                    var n2 = dialogView.np2.value.toString().padStart(2, '0')

                    if ((n1 == "00" && n2 == "00")) {
                        Toast.makeText(activity, "거리를 설정해주세요!", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.tvGoalValue.text = "${n1}.${n2}km"
                        alertDialog?.dismiss()
                    }
                }

                // 취소 버튼
                dialogView.btn_cancel.setOnClickListener {
                    alertDialog?.dismiss()
                }
            }
        }

        // 인원수 설정
        binding.btnPeopleCnt.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_people_cnt, null)
            val alertDialog = activity?.let { AlertDialog.Builder(it).create() }

            alertDialog?.setView(dialogView)
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.show()

            // 최대, 최소값 설정
            dialogView.np_people.minValue = 1
            dialogView.np_people.maxValue = 8

            // 기본값 설정
            dialogView.np_people.value = Integer.parseInt(binding.tvPeopleCnt.text.toString())

            // 저장 버튼
            dialogView.btn_save.setOnClickListener {
                binding.tvPeopleCnt.text = dialogView.np_people.value.toString()
                alertDialog?.dismiss()
            }

            // 취소 버튼
            dialogView.btn_cancel.setOnClickListener {
                alertDialog?.dismiss()
            }
        }

        // 러너 레벨 설정
        binding.btnRunnerLevel.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_people_cnt, null)
            val alertDialog = activity?.let { AlertDialog.Builder(it).create() }

            alertDialog?.setView(dialogView)
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.show()

            dialogView.tv_people.text = "레벨"

            // 최대, 최소값, 기본값 설정
            val levelList = arrayOf("모든 러너", "초보 러너", "중급 러너", "프로 러너")

            dialogView.np_people.displayedValues = levelList
            dialogView.np_people.minValue = 0
            dialogView.np_people.maxValue = 3

            val value = binding.tvRunnerLevel.text.toString()
            val index = levelList.indexOf(value)
            dialogView.np_people.value = index

            // 저장 버튼
            dialogView.btn_save.setOnClickListener {
                binding.tvRunnerLevel.text = levelList[dialogView.np_people.value]
                alertDialog?.dismiss()
            }

            // 취소 버튼
            dialogView.btn_cancel.setOnClickListener {
                alertDialog?.dismiss()
            }
        }

        // 완료 버튼
        binding.btnNext.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_question, null)
            val alertDialog = activity?.let { AlertDialog.Builder(it).create() }

            dialogView.tv_question.text = "그룹 수정을 완료하시겠습니까?"
            alertDialog?.setView(dialogView)
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.show()

            dialogView.btn_q_cancel.setOnClickListener {
                alertDialog?.dismiss()
            }

            dialogView.btn_q_ok.setOnClickListener {
                alertDialog?.dismiss()
                val content = binding.etInfo.text.toString()
                val name = binding.etName.text.toString()
                val imgURL = binding.imgUser.context.toString()
                val maxNum = Integer.parseInt(binding.tvPeopleCnt.text.toString())

                val goalText = binding.tvGoalValue.text.toString()
                var goalType = ""
                when (binding.tvGoalType.text) {
                    "시간" -> {
                        goalType = "TIME"
                        val h = goalText.substring(0, 2).toFloat()
                        val m = goalText.substring(5, 7).toFloat()
                        val s = goalText.substring(10, 12).toFloat()
                        goal = h * 60 * 60 + m * 60 + s
                    }
                    "거리" -> {
                        goalType = "DISTANCE"
                        goal = goalText.substring(0, 5).toFloat()
                    }
                    else -> {
                        goalType = "NOGOAL"
                    }
                }

                var level = 0
                level = when (binding.tvRunnerLevel.text) {
                    "초보 러너" -> 1
                    "중급 러너" -> 2
                    "프로 러너" -> 3
                    else -> 0
                }

                var recruitingStatus = ""
                recruitingStatus = if (binding.tvRecruitStatus.text == "모집중") {
                    "recruiting"
                } else {
                    "finished"
                }

                val req = GroupEditRequest(content, goal, goalType, imgURL, level, maxNum, name, recruitingStatus)
                PatchClubService(this).tryPatchClub(clubIdx, req)
            }
        }

        // 취소 버튼
        binding.btnBack.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                "go_to_main",
                bundleOf("clubIdx" to clubIdx))
            mainActivity?.groupFragmentChange(0) // 그룹 페이지
        }

        setFragmentResultListener("go_to_edit") { requestKey, bundle ->
            clubIdx = bundle.getLong("clubIdx")
            curDate = bundle.getString("curDate", "") // "yyyy-mm-dd"

            GetClubDetailService(this).tryGetClubDetail(clubIdx, curDate)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onGetClubDetailSuccess(response: GroupInfoResponse) {
        if (response.isSuccess) {
            val res = response.result

            Glide.with(binding.imgUser.context)
                .load(res.imgUrl)
                .into(binding.imgUser)
            binding.etName.setText(res.name)
            binding.etInfo.setText(res.content)
            binding.swRecruitStatus.isChecked = res.recruitStatus == "recruiting"
            binding.tvPeopleCnt.text = res.maxNum.toString()

            when (res.goalType) {
                "TIME" -> {
                    var sec = res.goal
                    var min = sec / 60
                    val hour = min / 60
                    min %= 60
                    sec %= 60

                    val hh = hour.toInt().toString().padStart(2, '0')
                    val mm = min.toInt().toString().padStart(2, '0')
                    val ss = sec.toInt().toString().padStart(2, '0')

                    binding.tvGoalType.text = "시간"
                    binding.tvGoalValue.text = "$hh : $mm : $ss"
                }
                "DISTANCE" -> {
                    val dis = DecimalFormat("00.00").format(res.goal).toString()

                    binding.tvGoalType.text = "거리"
                    binding.tvGoalValue.text = "${dis}km"
                }
            }

            when (res.level) {
                1 -> binding.tvRunnerLevel.text = "초보 러너"
                2 -> binding.tvRunnerLevel.text = "중급 러너"
                3 -> binding.tvRunnerLevel.text = "프로 러너"
                else -> binding.tvRunnerLevel.text = "모든 러너"
            }

        }
    }

    override fun onGetClubDetailFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onPatchClubInSuccess() {
        Toast.makeText(activity, "그룹 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onPatchClubInFailure(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}