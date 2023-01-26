package com.example.relay.group

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.relay.databinding.FragmentGroupListBinding
import com.example.relay.group.models.GroupListResponse
import com.example.relay.group.models.GroupListResult
import com.softsquared.template.kotlin.src.main.myPage.ListRVAdapter
import android.view.inputmethod.InputMethodManager

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

        // 클럽 리스트 받아오기 (전체)
        ListService(this).tryGetClubList("")

        // EditText에서 엔터 키를 누르면 검색한 클럽의 리스트를 가져온다.
        binding.etGroupSearch.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> {
                    val search = binding.etGroupSearch.text.toString()
                    ListService(this).tryGetClubList(search)

                    val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
            true
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
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
        }

        listAdapter.notifyDataSetChanged()

        if (listAdapter.itemCount == 0) {
            Toast.makeText(activity, "검색된 결과가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onGetClubListFailure(message: String) {
        // 에러 발생
    }
}