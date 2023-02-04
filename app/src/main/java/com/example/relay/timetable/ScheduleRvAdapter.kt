package com.example.relay.timetable

import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.relay.databinding.ItemRvEditTableBinding
import com.example.relay.timetable.models.Schedule
import kotlinx.android.synthetic.main.item_rv_edit_table.view.*
import java.time.LocalTime
import java.util.*

class ScheduleRvAdapter (context: Context, private val dataList:MutableList<Schedule>): RecyclerView.Adapter<ScheduleRvAdapter.DataViewHolder>() {
    private val context = context
    inner class DataViewHolder(private val binding: ItemRvEditTableBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Schedule){
                binding.btnDay.text = dayToString(data.day)
                binding.btnStart.text = data.start
                binding.btnEnd.text = data.end
                binding.btnGoalType.text = data.goalType
                binding.btnGoal.text = data.goal.toString()

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
        val cal = Calendar.getInstance()
        holder.bind(dataList[position])
        holder.itemView.btn_start.setOnClickListener{
            TimePickerDialog(context, { timePicker, h, m ->
                holder.itemView.btn_start.text = "$h:$m"
            }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true ).show()
        }
        holder.itemView.btn_end.setOnClickListener{
            TimePickerDialog(context, { timePicker, h, m ->
                holder.itemView.btn_end.text = "$h:$m"
            }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true ).show()
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun removeItem(position: Int){
        dataList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount);
    }

    fun addEmptyItem(){
        // val day = dayToString()
        dataList.add(Schedule(0,"9:00","9:30",0, "목표없음"))
        notifyItemInserted(dataList.size)
        notifyItemRangeChanged(dataList.size, getItemCount());
    }

    fun addItem(name: String, day:Int, start:String, end:String, goal:Int, goalType:String){
        dataList.add(Schedule(day, start, end, goal, goalType))
        notifyItemInserted(dataList.size)
        notifyItemRangeChanged(dataList.size, getItemCount());
    }

    fun getUpdatedSchedules(): MutableList<Schedule> {
        return dataList
    }

    private fun dayToString(dayInt:Int): String{
        var day = when(dayInt){
            1 -> "월"
            2 -> "화"
            3 -> "수"
            4 -> "목"
            5 -> "금"
            6 -> "토"
            0 -> "일"
            else -> ""
        }
        return day
    }

    private fun goalTypeToString(goalType:Int): String{
        var type = when(goalType){
            0 -> "시간"
            1 -> "거리"
            else -> "목표없음"
       }
        return type
    }
}