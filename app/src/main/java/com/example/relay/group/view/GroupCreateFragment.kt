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
import com.bumptech.glide.Glide
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.R
import com.example.relay.databinding.FragmentGroupCreateBinding
import com.example.relay.group.service.GetUserClubInterface
import com.example.relay.group.service.GetUserClubService
import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.ui.MainActivity
import kotlinx.android.synthetic.main.dialog_goal_km.view.*
import kotlinx.android.synthetic.main.dialog_goal_time.view.*
import kotlinx.android.synthetic.main.dialog_goal_time.view.btn_cancel
import kotlinx.android.synthetic.main.dialog_goal_time.view.btn_save
import kotlinx.android.synthetic.main.dialog_goal_type.view.*
import kotlinx.android.synthetic.main.dialog_people_cnt.view.*
import kotlinx.android.synthetic.main.fragment_group_main.view.*

class GroupCreateFragment: Fragment() {
    private var _binding: FragmentGroupCreateBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null

    private var goal: Float = 0F

    private val bucketURL = "https://team23-bucket.s3.ap-northeast-2.amazonaws.com/public/club"

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

        // 모집중 상태
        binding.swRecruitStatus.isChecked = true

        // 그룹 삭제하기 & 프로필 변경 버튼 화면에 안 보이게
        binding.line3.visibility = View.GONE
        binding.btnDelete.visibility = View.GONE

        // 이미지
        Glide.with(binding.imgGroup.context)
            .load(bucketURL + "/yellow.png")
            .into(binding.imgGroup)

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
                        goal = dialogView.np_hour.value * 3600 + dialogView.np_min.value * 60 + dialogView.np_sec.value + 0F
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
                        goal = ("$n1.$n2").toFloat()
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

        // 다음 버튼
        binding.btnNext.setOnClickListener {
            parentFragmentManager.setFragmentResult("go_to_edit_for_new_group",
                bundleOf(
                    "content" to binding.etInfo.text.toString(),
                    "name" to binding.etName.text.toString(),
                    "goalType" to goalTypeToEn(binding.tvGoalType.text.toString()),
                    "goal" to goal,
                    "level" to levelToInt(binding.tvRunnerLevel.text.toString()),
                    "maxNum" to binding.tvPeopleCnt.text.toString().toInt()
                )
            )
            mainActivity?.timetableFragmentChange(1) // 시간표 편집 페이지 이동
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

    private fun goalTypeToEn(goalType: String):String{
        return when(goalType){
            "목표 없음" -> "NOGOAL"
            "시간" -> "TIME"
            "거리" -> "DISTANCE"
            else -> throw IllegalArgumentException("잘못된 값")
        }
    }

    private fun levelToInt(level:String) :Int{
        return when(level){
            "초보 러너" -> 1
            "중급 러너" -> 2
            "프로 러너" -> 3
            else -> 0
        }
    }
}