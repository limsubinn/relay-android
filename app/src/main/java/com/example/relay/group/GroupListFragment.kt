package com.example.relay.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.relay.databinding.FragmentGroupListBinding
import com.example.relay.group.models.GroupListResponse
import com.example.relay.group.models.GroupListResult
import com.softsquared.template.kotlin.src.main.myPage.ListRVAdapter

class GroupListFragment: Fragment(), ListInterface {
    private var _binding: FragmentGroupListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 클럽 리스트 받아오기
        ListService(this).tryGetClubList("")
    }

    override fun onGetClubListSuccess(response: GroupListResponse) {
        val res = response.result

        // 리사이클러뷰
        val clubList: ArrayList<GroupListResult> = arrayListOf()
        val listAdapter = ListRVAdapter(clubList)
        binding.rvGroupAll.adapter = listAdapter
        binding.rvGroupAll.layoutManager = LinearLayoutManager(activity)
        if (res != null) {
            clubList.addAll(res)
            listAdapter.notifyDataSetChanged()
        }
    }

    override fun onGetClubListFailure(message: String) {
        TODO("Not yet implemented")
    }
}