package com.example.relay.timetable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.relay.databinding.ItemRvEditTableBinding
import com.example.relay.timetable.models.Schedule

class ScheduleRvAdapter (private val dataList:MutableList<Schedule>): RecyclerView.Adapter<ScheduleRvAdapter.DataViewHolder>() {
    inner class DataViewHolder(private val binding: ItemRvEditTableBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Schedule){
                binding.etTmpDay.setText(data.day)
                binding.etTmpStart.setText(data.startTime)
                binding.etTmpEnd.setText(data.endTime)
                binding.etTmpGoal.setText(data.goal)

                binding.btnRemove.setOnClickListener{
                    removeItem(adapterPosition)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleRvAdapter.DataViewHolder {
        val binding = ItemRvEditTableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

/*
    interface ScheduleClickListener{
        fun onItemClick(data: Schedule)
        fun onAddSchedule()
        fun onRemoveSchedule(position: Int)
    }

    private lateinit var scheduleClickListener: ScheduleClickListener

    fun setScheduleClickListener(itemClickListener: ScheduleClickListener){
        scheduleClickListener = itemClickListener
    }
*/
    fun removeItem(position: Int){
        dataList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount);
    }

    fun addEmptyItem(){
        dataList.add(Schedule("","","",""))
        notifyItemInserted(dataList.size)
        notifyItemRangeChanged(dataList.size, getItemCount());
    }

    fun addItem(day:String, start:String, end:String, goal:String){
        dataList.add(Schedule(day, start, end, goal))
        notifyItemInserted(dataList.size)
        notifyItemRangeChanged(dataList.size, getItemCount());
    }
}