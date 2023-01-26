package com.example.relay.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import com.example.relay.R
import com.example.relay.databinding.FragmentTimetableBinding
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import kotlinx.android.synthetic.main.fragment_timetable.*


class TimetableFragment: Fragment() {
    private var viewBinding: FragmentTimetableBinding? = null
    private val day = arrayOf("일", "월", "화", "수", "목", "금", "토")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding =  FragmentTimetableBinding.inflate(layoutInflater)
        with(viewBinding!!.timetable){
            initTable(day)
            baseSetting(20, 40, 60)
        }
        return viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding!!.btnMyTimetable.setOnClickListener{

        }

        viewBinding!!.btnEdit.setOnClickListener{
            val editFragment = TimetableEditFragment()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container_edit, editFragment)
                .commit()
        }
    }

    // 메모리 누수 방지 (fragment 의 생명주기 > view 의 생명주기)
    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

}