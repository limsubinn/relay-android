package com.softsquared.template.kotlin.src.main.myPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.relay.databinding.ItemGroupListBinding
import com.example.relay.group.models.GroupListResult

class GroupListRVAdapter(private val dataList: ArrayList<GroupListResult>): RecyclerView.Adapter<GroupListRVAdapter.DataViewHolder>() {

    // ViewHolder 객체
    inner class DataViewHolder(val binding: ItemGroupListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GroupListResult) {
            binding.teamName.text = data.name
            binding.teamIntro.text = data.content
            binding.teamState.text = data.recruitStatus
            Glide.with(binding.teamImg.context)
                .load(data.imgURL)
                .into(binding.teamImg)
        }
    }

    // ViewHolder 만들어질 때 실행할 동작
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            ItemGroupListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    // ViewHolder가 실제로 데이터를 표시해야 할 때 호출되는 함수
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    // 표현할 Item의 총 개수
    override fun getItemCount(): Int = dataList.size
}