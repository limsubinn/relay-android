package com.example.relay.timetable

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.relay.databinding.FragmentTimetableBinding
import com.example.relay.mypage.MySettingsActivity

class TimetableFragment: Fragment() {
    private var viewBinding: FragmentTimetableBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding =  FragmentTimetableBinding.inflate(layoutInflater)
        return viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding!!.btnTmp.setOnClickListener{
            val intent = Intent(activity, MySettingsActivity::class.java)
            startActivity(intent)
        }
    }

    // 메모리 누수 방지 (fragment 의 생명주기 > view 의 생명주기)
    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}