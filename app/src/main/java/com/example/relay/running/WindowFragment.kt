package com.example.relay.running

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.relay.R
import com.example.relay.ui.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_window.*

class WindowFragment : BottomSheetDialogFragment(), OnBottomSheetCallbacks {
    //private lateinit var viewBinding: FragmentWindowBinding

    private var currentState: Int = BottomSheetBehavior.STATE_EXPANDED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //viewBinding = FragmentWindowBinding.inflate(layoutInflater)

        (activity as MainActivity).setOnBottomSheetCallbacks(this)

        return inflater.inflate(R.layout.fragment_window, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterImage.setOnClickListener {
            (activity as MainActivity).openBottomSheet()
        }

        btn_pause.setOnClickListener{
            if (currentState == BottomSheetBehavior.STATE_EXPANDED) {
                Log.d("State","expanded")
                (activity as MainActivity).closeBottomSheet()
            } else  {
                Log.d("State","collapsed")
                (activity as MainActivity).openBottomSheet()
            }
        }

//        filterImage.setOnClickListener {
//            if (currentState == BottomSheetBehavior.STATE_EXPANDED) {
//                (activity as MainActivity).closeBottomSheet()
//            } else  {
//                (activity as MainActivity).openBottomSheet()
//            }
//        }
    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        currentState = newState
        when (newState) {
            BottomSheetBehavior.STATE_EXPANDED -> {
                layout_bottom_sheet.visibility = View.GONE

            }
            BottomSheetBehavior.STATE_COLLAPSED -> {
                layout_bottom_sheet.visibility = View.VISIBLE
            }
        }
    }
}