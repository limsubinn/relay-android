package com.example.relay.timetable

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.relay.R
import com.example.relay.databinding.FragmentScheduleBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ScheduleBottomSheetFragment(context: Context) : BottomSheetDialogFragment() {
    private var viewBinding: FragmentScheduleBottomSheetBinding? = null
    private val binding get() = viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentScheduleBottomSheetBinding.inflate(layoutInflater)

        binding.btnBack.setOnClickListener{
            dialog?.dismiss()
        }
        return binding.root
    }
}