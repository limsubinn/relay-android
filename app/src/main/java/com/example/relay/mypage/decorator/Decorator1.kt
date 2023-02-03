package com.example.relay.mypage.decorator

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.relay.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*

class Decorator1(value: Date?, context: Context) :
    DayViewDecorator {

    private val date: CalendarDay
    private val calendar = Calendar.getInstance()
    var context: Context

    init {
        date = CalendarDay.from(value)
        this.context = context
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        day.copyTo(calendar)
        return day == date
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.decorator_state1)!!)
    }
}
