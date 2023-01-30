package com.example.relay.group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.relay.databinding.ItemGroupMemberBinding

class GroupMemberRVAdapter(private val dataList: ArrayList<Member>): RecyclerView.Adapter<GroupMemberRVAdapter.DataViewHolder>() {

    // ViewHolder 객체
    inner class DataViewHolder(val binding: ItemGroupMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Member) {
            binding.memberName.text = data.name
            binding.memberIntro.text = data.info
        }
    }

    // ViewHolder 만들어질 때 실행할 동작
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            ItemGroupMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    // ViewHolder가 실제로 데이터를 표시해야 할 때 호출되는 함수
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    // 표현할 Item의 총 개수
    override fun getItemCount(): Int = dataList.size
}