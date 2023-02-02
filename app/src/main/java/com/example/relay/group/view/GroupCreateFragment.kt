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
import kotlinx.android.synthetic.main.dialog_goal_km.view.*
import kotlinx.android.synthetic.main.dialog_goal_time.view.*
import kotlinx.android.synthetic.main.dialog_goal_time.view.btn_cancel
import kotlinx.android.synthetic.main.dialog_goal_time.view.btn_save
import kotlinx.android.synthetic.main.dialog_goal_type.view.*
import kotlinx.android.synthetic.main.dialog_people_cnt.view.*

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
            } else if ((binding.tvGoalType.text.equals("거리")) || (binding.tvGoalType.text.equals("스피드"))) {
                val dialogView = layoutInflater.inflate(R.layout.dialog_goal_km, null)
                val alertDialog = activity?.let { AlertDialog.Builder(it).create() }

                alertDialog?.setView(dialogView)
                alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog?.show()

                dialogView.tv_title.text = "${binding.tvGoalType.text} / km"

                // 최대, 최소값 설정
                dialogView.np1.minValue = 0
                dialogView.np1.maxValue = 99
                dialogView.np2.minValue = 0
                dialogView.np2.maxValue = 99

                // 기본값 설정
                dialogView.np1.value = Integer.parseInt(binding.tvGoalValue.text.substring(0, 2))
                dialogView.np2.value = Integer.parseInt(binding.tvGoalValue.text.substring(5, 7))

                // 저장 버튼
                dialogView.btn_save.setOnClickListener {
                    var n1 = dialogView.np1.value.toString().padStart(2, '0')
                    var n2 = dialogView.np2.value.toString().padStart(2, '0')

                    if ((n1 == "00" && n2 == "00")) {
                        Toast.makeText(activity, "${binding.tvGoalType.text}를 설정해주세요!", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.tvGoalValue.text = "${n1} : ${n2}"
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

            // 최대, 최소값, 기본값 설정
            val levelList = arrayOf("초보 러너", "중급 러너", "프로 러너")

            dialogView.np_people.displayedValues = levelList
            dialogView.np_people.minValue = 0
            dialogView.np_people.maxValue = 2

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