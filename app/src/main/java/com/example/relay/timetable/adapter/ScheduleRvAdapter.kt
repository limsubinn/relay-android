package com.example.relay.timetable.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.relay.R
import com.example.relay.databinding.ItemRvEditTableBinding
import com.example.relay.timetable.models.Schedule
import kotlinx.android.synthetic.main.dialog_goal_km.view.*
import kotlinx.android.synthetic.main.dialog_goal_km.view.btn_cancel
import kotlinx.android.synthetic.main.dialog_goal_km.view.btn_save
import kotlinx.android.synthetic.main.dialog_goal_time.*
import kotlinx.android.synthetic.main.dialog_goal_time.view.*
import kotlinx.android.synthetic.main.dialog_goal_type.view.*
import kotlinx.android.synthetic.main.dialog_people_cnt.view.*
import kotlinx.android.synthetic.main.dialog_timepicker.view.*
import kotlinx.android.synthetic.main.item_rv_edit_table.view.*
import java.util.*


class ScheduleRvAdapter (context: Context, private val dataList:MutableList<Schedule>): RecyclerView.Adapter<ScheduleRvAdapter.DataViewHolder>() {
    private val context = context
    inner class DataViewHolder(private val binding: ItemRvEditTableBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Schedule){
                binding.btnDay.text = dayToString(data.day)
                binding.btnStart.text = data.start.substring(0,5)
                binding.btnEnd.text = data.end.substring(0,5)

                when (data.goalType) {
                    "DISTANCE" -> {
                        binding.btnGoalType.text = "거리"
                        val goalArr = data.goal.toString().split(".")
                        Log.d("Timetable", "bind: ${goalArr.get(0).padStart(2, '0')}")
                        binding.btnGoal.text = "${goalArr.get(0).padStart(2, '0')}:${goalArr.get(1).padStart(2, '0')}"
                    }
                    "TIME" -> {
                        binding.btnGoalType.text = "시간"
                        val hour = (data.goal / 3600).toInt().toString().padStart(2, '0')
                        val min = (data.goal % 3600 / 60).toInt().toString().padStart(2, '0')
                        val sec = (data.goal % 60).toInt().toString().padStart(2, '0')
                        binding.btnGoal.text = "$hour:$min:$sec"
                    }
                    "NOGOAL" -> {
                        binding.btnGoalType.text = "목표 없음"
                        binding.btnGoal.text = "----"
                    }
                    else -> throw IllegalArgumentException("잘못된 값")
                }

                binding.btnRemove.setOnClickListener{
                    removeItem(adapterPosition)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemRvEditTableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val cal = Calendar.getInstance()
        val inflater: LayoutInflater = LayoutInflater.from(context)

        holder.bind(dataList[position])
        holder.itemView.btn_day.setOnClickListener{
            val dialogView = inflater.inflate(R.layout.dialog_people_cnt, null)
            val alertDialog = context.let { AlertDialog.Builder(it).create() }

            alertDialog.setView(dialogView)
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()

            dialogView.tv_people.text = "요일"

            val dayList = arrayOf("월", "화", "수", "목", "금", "토", "일")

            dialogView.np_people.displayedValues = dayList
            dialogView.np_people.minValue = 0
            dialogView.np_people.maxValue = 6

            val index = dayList.indexOf("월")
            dialogView.np_people.value = index

            // 저장 버튼
            dialogView.btn_save.setOnClickListener {
                dataList[position].day = dialogView.np_people.value + 1
                holder.itemView.btn_day.text = dayList[dialogView.np_people.value]
                alertDialog.dismiss()
            }

            // 취소 버튼
            dialogView.btn_cancel.setOnClickListener {
                alertDialog.dismiss()
            }
        }
        holder.itemView.btn_start.setOnClickListener{
            val dialogView = inflater.inflate(R.layout.dialog_timepicker, null)
            val alertDialog = context.let { AlertDialog.Builder(it).create() }

            with(alertDialog) {
                setView(dialogView)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                show()
            }

            dialogView.timePicker.hour = holder.itemView.btn_start.text.toString().substring(0,2).toInt()
            dialogView.timePicker.minute = holder.itemView.btn_start.text.toString().substring(3).toInt()

            dialogView.btn_save.setOnClickListener{
                val hour = dialogView.timePicker.hour.toString().padStart(2, '0')
                val min = dialogView.timePicker.minute.toString().padStart(2, '0')
                dataList[position].start = "$hour:$min:00"
                notifyDataSetChanged()
                alertDialog.dismiss()
            }

            dialogView.btn_cancel.setOnClickListener{
                alertDialog.dismiss()
            }
        }
        holder.itemView.btn_end.setOnClickListener{
            val dialogView = inflater.inflate(R.layout.dialog_timepicker, null)
            val alertDialog = context.let { AlertDialog.Builder(it).create() }

            with(alertDialog) {
                setView(dialogView)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                show()
            }
            dialogView.tv_timepicker_name.text = "Finish"
            dialogView.timePicker.hour = holder.itemView.btn_end.text.toString().substring(0,2).toInt()
            dialogView.timePicker.minute = holder.itemView.btn_end.text.toString().substring(3).toInt()

            dialogView.btn_save.setOnClickListener{
                val hour = dialogView.timePicker.hour.toString().padStart(2, '0')
                val min = dialogView.timePicker.minute.toString().padStart(2, '0')
                dataList[position].end = "$hour:$min:00"
                notifyDataSetChanged()
                alertDialog.dismiss()
            }

            dialogView.btn_cancel.setOnClickListener{
                alertDialog.dismiss()
            }
        }
        holder.itemView.btn_goal_type.setOnClickListener{
            val goalType = arrayOf("목표 없음", "시간", "거리")

            val dialogView = inflater.inflate(R.layout.dialog_goal_type, null)
            val alertDialog = context.let { AlertDialog.Builder(it).create() }

            alertDialog.setView(dialogView)
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()

            val itemView = dialogView.list_dialog_item

            itemView.adapter =
                ArrayAdapter(context, android.R.layout.simple_list_item_1, goalType)

            itemView.onItemClickListener = AdapterView.OnItemClickListener {
                    parent, view, itemPosition, id ->
                if (holder.itemView.btn_goal_type.text != itemView.adapter.getItem(itemPosition).toString()) {
                    when (itemPosition) {
                        0 -> dataList[position].goalType = "NOGOAL"
                        1 -> dataList[position].goalType = "TIME"
                        2 -> dataList[position].goalType = "DISTANCE"
                        else -> throw IllegalArgumentException("잘못된 값")
                    }
                    dataList[position].goal = 0F
                    notifyDataSetChanged()
                }
                alertDialog.dismiss()
            }
        }
        holder.itemView.btn_goal.setOnClickListener{
            when (holder.itemView.btn_goal_type.text){
                "거리" -> {
                    val dialogView = inflater.inflate(R.layout.dialog_goal_km, null)
                    val alertDialog = context.let { AlertDialog.Builder(it).create() }

                    alertDialog.setView(dialogView)
                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.show()

                    with(dialogView){
                        // 최대, 최소값 설정
                        np1.minValue = 0
                        np1.maxValue = 99
                        np2.minValue = 0
                        np2.maxValue = 99

                        // 기본값 설정
                        np1.value = Integer.parseInt(holder.itemView.btn_goal.text.substring(0, 2))
                        np2.value = Integer.parseInt(holder.itemView.btn_goal.text.substring(3, 4))
                    }

                    // 저장 버튼
                    dialogView.btn_save.setOnClickListener {
                        val n1 = dialogView.np1.value.toString().padStart(2, '0')
                        val n2 = dialogView.np2.value.toString().padStart(2, '0')

                        if ((n1 == "00" && n2 == "00")) {
                            Toast.makeText(context, "거리를 설정해주세요!", Toast.LENGTH_SHORT).show()
                        } else {
                            val distance = ("$n1.$n2").toFloat()
                            dataList[position].goal = distance
                            holder.itemView.btn_goal.text = "$n1:$n2"
                            alertDialog.dismiss()
                        }
                    }
                    // 취소 버튼
                    dialogView.btn_cancel.setOnClickListener {
                        alertDialog.dismiss()
                    }
                }
                "시간" -> {
                    val dialogView = inflater.inflate(R.layout.dialog_goal_time, null)
                    val alertDialog = context.let { AlertDialog.Builder(it).create() }

                    alertDialog.setView(dialogView)
                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.show()

                    val formatter =
                        NumberPicker.Formatter { value ->
                            "" + value + "0"
                        }

                    with(dialogView){
                        np_min.setFormatter(formatter)

                        // 최대, 최소값 설정
                        np_hour.minValue = 0
                        np_hour.maxValue = 23
                        np_min.minValue = 0
                        np_min.maxValue = 5
                        np_sec.minValue = 0
                        np_sec.maxValue = 0

                        // 기본값 설정
                        np_hour.value = Integer.parseInt(holder.itemView.btn_goal.text.substring(0, 2))
                        np_min.value = Integer.parseInt(holder.itemView.btn_goal.text.substring(3, 4))
                        np_sec.value = Integer.parseInt(holder.itemView.btn_goal.text.substring(6, 8))
                    }

                    // 저장 버튼
                    dialogView.btn_save.setOnClickListener {
                        val hour = alertDialog.np_hour.value.toString().padStart(2, '0')
                        val min = (alertDialog.np_min.value * 10).toString()
                        if ((hour == "00" && min == "00")) {
                            Toast.makeText(context, "시간을 설정해주세요!", Toast.LENGTH_SHORT).show()
                        } else {
                            val goal = alertDialog.np_hour.value * 3600 + alertDialog.np_min.value * 600 + alertDialog.np_sec.value
                            dataList[position].goal = goal.toFloat()
                            notifyDataSetChanged()
                            alertDialog.dismiss()
                        }
                    }

                    // 취소 버튼
                    dialogView.btn_cancel.setOnClickListener {
                        alertDialog.dismiss()
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
        dataList.add(Schedule(1,"09:00:00","10:00:00",1800F, "TIME"))
        notifyItemInserted(dataList.size)
        notifyItemRangeChanged(dataList.size, itemCount);
    }

    fun addItem(day:Int, start:String, end:String, goal:Float, goalType:String){
        dataList.add(Schedule(day, start, end, goal, goalType))
        notifyItemInserted(dataList.size)
        notifyItemRangeChanged(dataList.size, itemCount);
    }

    fun getUpdatedSchedules(): MutableList<Schedule> {
        return dataList
    }

    private fun dayToString(dayInt:Int): String{
        return when(dayInt){
            1 -> "월"
            2 -> "화"
            3 -> "수"
            4 -> "목"
            5 -> "금"
            6 -> "토"
            7 -> "일"
            else -> throw IllegalArgumentException("잘못된 값")
        }
    }
}