package com.example.relay.group

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class GroupPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val mSetItemCount = 2 //프래그먼트 개수 지정

    override fun createFragment(position: Int): Fragment {
        val viewIdx = getPosition(position)
        return when (viewIdx) {
            0 -> GroupMainFragment()
            1 -> GroupListFragment()
            else -> GroupMainFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    fun getPosition(position: Int): Int {
        return position
    }
}