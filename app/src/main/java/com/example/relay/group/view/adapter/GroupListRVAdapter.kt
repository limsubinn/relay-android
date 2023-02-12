package com.example.relay.group.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.relay.databinding.ItemRvGroupListBinding
import com.example.relay.group.models.GroupInfoResult

class GroupListRVAdapter(private val dataList: ArrayList<GroupInfoResult>): RecyclerView.Adapter<GroupListRVAdapter.DataViewHolder>() {

    // 아이템 클릭 인터페이스
    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    // 아이템 클릭 리스너
    private lateinit var itemClickListner: ItemClickListener

    // 아이템 클릭 리스너 등록
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

    // ViewHolder 객체
    inner class DataViewHolder(val binding: ItemRvGroupListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GroupInfoResult) {
            binding.teamName.text = data.name
            binding.teamIntro.text = data.content

            if (data.recruitStatus == "recruiting") {
                binding.teamRecruitStatus.text = "모집중"
            } else {
                binding.teamRecruitStatus.text = "모집완료"
            }

            when (data.level) {
                0 -> binding.teamLevel.text = "모든러너"
                1 -> binding.teamLevel.text = "초보"
                2 -> binding.teamLevel.text = "중급"
                3 -> binding.teamLevel.text = "프로"
            }

            Glide.with(binding.teamImg.context)
                .load(data.imgURL)
                .into(binding.teamImg)
        }
    }

    // ViewHolder 만들어질 때 실행할 동작
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            ItemRvGroupListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    // ViewHolder가 실제로 데이터를 표시해야 할 때 호출되는 함수
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])

        // 아이템 클릭 리스너
        holder.itemView.setOnClickListener {
            itemClickListner.onItemClick(it, position)
        }
    }

    // 표현할 Item의 총 개수
    override fun getItemCount(): Int = dataList.size
}