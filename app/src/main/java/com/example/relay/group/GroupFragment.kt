package com.example.relay.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.relay.databinding.FragmentGroupBinding


class GroupFragment: Fragment() {
    private var _binding: FragmentGroupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = activity?.let { GroupPagerAdapter(it) }
        binding.viewPager2.adapter = pagerAdapter

        binding.indicator.setViewPager(binding.viewPager2)
        binding.indicator.createIndicators(2, 0)

        binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 가로 슬라이드
        binding.viewPager2.currentItem = 0 //시작 지점
        binding.viewPager2.offscreenPageLimit = 2; //최대 이미지 수

        // 뷰페이저 설정
        binding.viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (positionOffsetPixels == 0) {
                    binding.viewPager2.currentItem = position
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.indicator.animatePageSelected(position)
            }
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}