package com.example.relay.running

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.relay.databinding.FragmentWindowBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_window.*

class WindowFragment : BottomSheetDialogFragment() {
    private lateinit var viewBinding: FragmentWindowBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentWindowBinding.inflate(layoutInflater)

        return viewBinding.root
    }
}