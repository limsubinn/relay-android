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
                binding.etTmpDay.setText(data.day)
                binding.btnStart.text = data.startTime
                binding.btnEnd.text = data.endTime
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
        dataList.add(Schedule("","","",""))
        notifyItemInserted(dataList.size)
        notifyItemRangeChanged(dataList.size, getItemCount());
    }

    fun addItem(day:String, start:String, end:String, goal:String){
        dataList.add(Schedule(day, start, end, goal))
        notifyItemInserted(dataList.size)
        notifyItemRangeChanged(dataList.size, getItemCount());
    }

    fun getUpdatedSchedules(): MutableList<Schedule> {
        return dataList
    }
}