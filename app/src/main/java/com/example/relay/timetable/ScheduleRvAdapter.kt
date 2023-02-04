package com.example.relay.timetable

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.relay.R
import com.example.relay.databinding.ItemRvEditTableBinding
import com.example.relay.timetable.models.Schedule
import kotlinx.android.synthetic.main.dialog_goal_km.*
import kotlinx.android.synthetic.main.dialog_goal_time.*
import kotlinx.android.synthetic.main.dialog_goal_time.btn_cancel
import kotlinx.android.synthetic.main.dialog_goal_time.btn_save
import kotlinx.android.synthetic.main.dialog_goal_type.*
import kotlinx.android.synthetic.main.item_rv_edit_table.view.*
import kotlinx.android.synthetic.main.item_rv_edit_table.view.btn_goal_type
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
        holder.itemView.btn_day.setOnClickListener{

        }
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
        holder.itemView.btn_goal_type.setOnClickListener{
            val goalType = arrayOf("목표 없음", "시간", "거리", "스피드")

            val alertDialog = context.let { AlertDialog.Builder(it).create() }
            alertDialog.setContentView(R.layout.dialog_goal_type)
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()

            val itemView = alertDialog.list_dialog_item

            itemView.adapter =
                ArrayAdapter(context, android.R.layout.simple_list_item_1, goalType)

            itemView.onItemClickListener = AdapterView.OnItemClickListener {
                    parent,
                    view,
                    position,
                    id ->
                holder.itemView.btn_goal_type.text = itemView.adapter.getItem(position).toString()
                when (position) {
                    0 -> holder.itemView.btn_goal.text = "----"
                    1 -> holder.itemView.btn_goal.text = "00 : 00 : 00"
                    else -> holder.itemView.btn_goal.text  = "00 : 00"
                }
                alertDialog?.dismiss()
            }
        }
        holder.itemView.btn_goal.setOnClickListener{
            when (holder.itemView.btn_goal_type.text){
                "거리" -> {
                    val alertDialog = context.let { AlertDialog.Builder(it).create() }
                    alertDialog.setContentView(R.layout.dialog_goal_km)
                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.show()

                    with(alertDialog){
                        // 최대, 최소값 설정
                        np1.minValue = 0
                        np1.maxValue = 99
                        np2.minValue = 0
                        np2.maxValue = 99

                        // 기본값 설정
                        np1.value = Integer.parseInt(holder.itemView.btn_goal.text.substring(0, 2))
                        np2.value = Integer.parseInt(holder.itemView.btn_goal.text.substring(5, 7))
                    }

                    // 저장 버튼
                    alertDialog.btn_save.setOnClickListener {
                        val hour = alertDialog.np_hour.value.toString().padStart(2, '0')
                        val min = alertDialog.np_min.value.toString().padStart(2, '0')
                        val sec = alertDialog.np_sec.value.toString().padStart(2, '0')

                        if ((hour == "00" && min == "00" && sec == "00")) {
                            Toast.makeText(context, "시간을 설정해주세요!", Toast.LENGTH_SHORT).show()
                        } else {
                            holder.itemView.btn_goal.text = "${hour} : ${min}"
                            alertDialog?.dismiss()
                        }
                    }

                    // 취소 버튼
                    alertDialog.btn_cancel.setOnClickListener {
                        alertDialog?.dismiss()
                    }
                }
                "시간" -> {
                    val alertDialog = context.let { AlertDialog.Builder(it).create() }
                    alertDialog.setContentView(R.layout.dialog_goal_time)
                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.show()

                    with(alertDialog){
                        // 최대, 최소값 설정
                        np_hour.minValue = 0
                        np_hour.maxValue = 23
                        np_min.minValue = 0
                        np_min.maxValue = 59
                        np_sec.minValue = 0
                        np_sec.maxValue = 59

                        // 기본값 설정
                        np_hour.value = Integer.parseInt(holder.itemView.btn_goal.text.substring(0, 2))
                        np_min.value = Integer.parseInt(holder.itemView.btn_goal.text.substring(5, 7))
                        np_sec.value = Integer.parseInt(holder.itemView.btn_goal.text.substring(10, 12))
                    }

                    // 저장 버튼
                    alertDialog.btn_save.setOnClickListener {
                        val hour = alertDialog.np_hour.value.toString().padStart(2, '0')
                        val min = alertDialog.np_min.value.toString().padStart(2, '0')
                        val sec = alertDialog.np_sec.value.toString().padStart(2, '0')

                        if ((hour == "00" && min == "00" && sec == "00")) {
                            Toast.makeText(context, "시간을 설정해주세요!", Toast.LENGTH_SHORT).show()
                        } else {
                            holder.itemView.btn_goal.text = "${hour} : ${min}"
                            alertDialog?.dismiss()
                        }
                    }

                    // 취소 버튼
                    alertDialog.btn_cancel.setOnClickListener {
                        alertDialog?.dismiss()
                    }
                }
            }
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

    fun addItem(day:Int, start:String, end:String, goal:Int, goalType:String){
        dataList.add(Schedule(day, start, end, goal, goalType))
        notifyItemInserted(dataList.size)
        notifyItemRangeChanged(dataList.size, getItemCount());
    }

    fun getUpdatedSchedules(): MutableList<Schedule> {
        return dataList
    }

    private fun dayToString(dayInt:Int): String{
        val day = when(dayInt){
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
        val type = when(goalType){
            0 -> "시간"
            1 -> "거리"
            else -> "목표없음"
       }
        return type
    }
}