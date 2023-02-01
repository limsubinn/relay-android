package com.example.relay.group.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.relay.databinding.FragmentGroupCreateDeniedBinding
import com.example.relay.ui.MainActivity

class GroupCreateNFragment: Fragment() {
    private var _binding: FragmentGroupCreateDeniedBinding? = null
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
        _binding = FragmentGroupCreateDeniedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 돌아가기 버튼
        binding.btnBack.setOnClickListener{
            mainActivity?.groupFragmentChange(1) // 그룹 리스트로 이동
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}