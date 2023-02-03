package com.example.relay.mypage

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
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

class Decorator2(value: Date?, context: Context) :
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
        view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.decorator_state2)!!)
    }
}

class Decorator3(value: Date?, context: Context) :
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
        view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.decorator_state3)!!)
    }
}

class Decorator4(value: Date?, context: Context) :
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
        view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.decorator_state4)!!)
    }
}

class Decorator5(value: Date?, context: Context) :
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
        view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.decorator_state5)!!)
    }
}

class Decorator6(value: Date?, context: Context) :
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
        view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.decorator_state6)!!)
    }
}