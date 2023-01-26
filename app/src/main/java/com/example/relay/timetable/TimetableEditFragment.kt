package com.example.relay.timetable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.relay.R
import com.example.relay.databinding.FragmentTimetableEditBinding

class TimetableEditFragment : Fragment() {
    private var viewBinding : FragmentTimetableEditBinding? = null
    private val binding get() = viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTimetableEditBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener{

        }

        binding.btnSave.setOnClickListener{
            // 통신 코드 추가 필요

            // 빈 Fragment 로 변경
            val emptyFragment = TimetableEmptyFragment()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container_edit, emptyFragment)
                .commit()
        }
    }
}