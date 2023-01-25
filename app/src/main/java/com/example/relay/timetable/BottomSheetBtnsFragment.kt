package com.example.relay.timetable

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.relay.databinding.FragmentBottomSheetBtnsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetBtnsFragment(context: Context) : BottomSheetDialogFragment() {
    private val activityContext: Context = context
    private var viewBinding: FragmentBottomSheetBtnsBinding ?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentBottomSheetBtnsBinding.inflate(layoutInflater)
        viewBinding!!.btnAdd.setOnClickListener{

        }
        viewBinding!!.btnDelete.setOnClickListener{

        }
        return viewBinding!!.root
    }
}