package com.example.relay.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.relay.databinding.FragmentGroupMainBinding

class GroupMainFragment: Fragment() {
    private var _binding: FragmentGroupMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupMainBinding.inflate(inflater, container, false)
        return binding.root
    }
}