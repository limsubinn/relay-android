package com.example.relay.mypage.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.Constants
import com.example.relay.R
import com.example.relay.databinding.FragmentMypageBinding
import com.example.relay.mypage.models.DailyRecordResponse
import com.example.relay.mypage.models.LocationList
import com.example.relay.mypage.models.UserProfileResponse
import com.example.relay.mypage.service.MypageInterface
import com.example.relay.mypage.service.MypageService
import com.example.relay.running.service.Polyline
import com.example.relay.ui.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*


class MypageFragment: Fragment(), MypageInterface {
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var currentYear = 0
    private var curDate = ""

    private val userIdx = prefs.getLong("userIdx", 0L)
    private val name = prefs.getString("name", "")

    private var map: GoogleMap? = null
    private lateinit var mapView: MapView

    private var pathPoints = mutableListOf<Polyline>()
    private var locationList = mutableListOf<LocationList>()
    var latLngList = mutableListOf<LatLng>()

    private var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        if (context != null) {
            super.onAttach(context)
        }
        mainActivity = activity as MainActivity?
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        mapView = binding.mypageMapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            moveCameraToUser()
            map = it
            addAllPolylines()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val today = GregorianCalendar()
        var year: Int = today.get(Calendar.YEAR)
        var month: Int = today.get(Calendar.MONTH)
        var date: Int = today.get(Calendar.DATE)

        // set current date to calendar and current month to currentMonth variable
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

        val myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                val cal = Calendar.getInstance()
                cal.time = date

                return if (isSelected) R.layout.item_selected_calendar
                else R.layout.item_unselected_calendar
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                // bind data to calendar item views
                // findViewId 안 쓰고 싶은데 자꾸 바인딩이 안 된다 ㅠㅠ,,,,,
                holder.itemView.findViewById<TextView>(R.id.tv_date_calendar_item).text =
                    DateUtils.getDayNumber(date)

                if (isSelected) {
                    holder.itemView.findViewById<TextView>(R.id.tv_month_calendar_item).text =
                        DateUtils.getMonth3LettersName(date)
                    holder.itemView.findViewById<TextView>(R.id.tv_year_calendar_item).text =
                        DateUtils.getYear(date)
                }

            }
        }

        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                // Log.d("calender selection", "$position, $date")

                curDate = simpleDateFormat.format(date)
                MypageService(this@MypageFragment).tryGetDailyRecord(curDate, userIdx)

                return true
            }
        }

        val myCalendarChangesObserver = object : CalendarChangesObserver {
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                super.whenSelectionChanged(isSelected, position, date)
            }
        }

        binding.selCalendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager

            setDates(getFutureDatesOfCurrentMonth())
            initialPositionIndex = date - 3
            init()
            select(date - 1) // 오늘 날짜 선택
        }

        // 기록 -> 마이페이지
        setFragmentResultListener("record_to_mypage") { requestKey, bundle ->

            curDate = bundle.getString("curDate", "") // "yyyy-mm-dd"
            year = Integer.parseInt(curDate.substring(0, 4))
            month = Integer.parseInt(curDate.substring(5, 7))
            date = Integer.parseInt(curDate.substring(8, 10))

            binding.selCalendar.apply {
                setDates(getFutureDatesOfSelectMonth(month, year))
                initialPositionIndex = date - 3
                init()
                select(date-1)
            }
        }

        // 달력 버튼
        binding.btnCalendar.setOnClickListener {
            // 마이페이지 -> 마이레코드
            parentFragmentManager.setFragmentResult("go_to_my_record",
                bundleOf("curDate" to curDate)
            )
            mainActivity?.mypageFragmentChange(1) // 기록 페이지로 이동
        }

        // 프로필 불러오기
        if ((userIdx != 0L)) {
            MypageService(this).tryGetUserProfile(userIdx)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        currentYear = calendar[Calendar.YEAR]
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfSelectMonth(month: Int, year: Int): List<Date> {
        currentMonth = month-1
        currentYear = year
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        calendar.set(Calendar.YEAR, currentYear)
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }

    private fun moveCameraToUser() {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    10f
                )
            )
        }
    }

    private fun addAllPolylines() {
        for(latLngList in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(Color.parseColor("#FFFF3E00"))
                .width(Constants.POLYLINE_WIDTH)
                .addAll(latLngList)
            map?.addPolyline(polylineOptions)

        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onGetUserProfileSuccess(response: UserProfileResponse) {
        val res = response.result

        // 닉네임 & 그룹 이름 받아오기
        if (res.clubIdx == 0L) {
            binding.profileName.text = "${res.nickname} / -"
        } else {
            binding.profileName.text = "${res.nickname} / ${res.clubName}"
        }
        // 자기소개
        binding.tvIntro.text = res.statusMsg
        // 프로필 사진
        Glide.with(binding.profileImg.context)
            .load(res.imgUrl)
            .override(90,90) // 사이즈 조정
            .into(binding.profileImg)

        // 설정 버튼
        binding.btnMySettings.setOnClickListener {
            val intent = Intent(activity, MySettingsActivity::class.java)
            intent.apply {
                putExtra("imgUrl", res.imgUrl)
                putExtra("name", res.nickname)
                putExtra("email", res.email)
                putExtra("statusMsg", res.statusMsg)
                putExtra("isAlarmOn", res.isAlarmOn)
            }
            startActivity(intent);
        }
    }

    override fun onGetUserProfileFailure(message: String) {
        Toast.makeText(activity, "유저 정보를 받아오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onGetDailyRecordSuccess(response: DailyRecordResponse) {
        if (response.isSuccess) {
            val res = response.result

            binding.tvNotRecord.visibility = View.GONE
            binding.recordLayout.visibility = View.VISIBLE
            binding.mypageMapView.visibility = View.VISIBLE

            locationList = res.locationList.clone() as MutableList<LocationList>
            for (i in 0 until locationList.size){
                val location = LatLng(locationList[i].latitude.toDouble(), locationList[i].longitude.toDouble())
                latLngList.add(location)
            }

            binding.mypageMapView.getMapAsync {
                moveCameraToUser()
                map = it
                addAllPolylines()
            }

            var sec = res.time
            var min = sec / 60
            val hour = min / 60
            min %= 60
            sec %= 60

            val hh = hour.toInt().toString().padStart(2, '0')
            val mm = min.toInt().toString().padStart(2, '0')
            val ss = sec.toInt().toString().padStart(2, '0')

            // 거리, 시간, 페이스
            if ((res.clubName == "그룹에 속하지 않습니다.") || (res.goalType == "NOGOAL")) {
                binding.goalValue.visibility = View.GONE
                binding.goalTarget.setTextColor(Color.BLACK)
                binding.goalTarget.text = "${hh} : ${mm} : ${ss}"
                binding.goalType.text = "시간"

                binding.otherType.text = "거리"
                binding.otherValue.text = res.distance.toString() + "km"
            } else {
                if (res.goalType == "TIME") {
                    var goalSec = res.goalValue
                    var goalMin = goalSec / 60
                    val goalHour = goalMin / 60
                    goalMin %= 60
                    goalSec %= 60

                    val goalH = goalHour.toInt().toString().padStart(2, '0')
                    val goalM = goalMin.toInt().toString().padStart(2, '0')
                    val goalS = goalSec.toInt().toString().padStart(2, '0')

                    binding.goalValue.visibility = View.VISIBLE
                    binding.goalValue.text = "${hh} : ${mm} : ${ss}"
                    binding.goalTarget.setTextColor(Color.RED)
                    binding.goalTarget.text = "${goalH} : ${goalM} : ${goalS}"
                    binding.goalType.text = "시간"

                    binding.otherType.text = "거리"
                    binding.otherValue.text = res.distance.toString() + "km"
                } else {
                    binding.goalValue.visibility = View.VISIBLE
                    binding.goalValue.text = res.distance.toString()  + "km"
                    binding.goalTarget.setTextColor(Color.RED)
                    binding.goalTarget.text = res.goalValue.toString()
                    binding.goalType.text = "거리"

                    binding.otherType.text = "시간"
                    binding.otherValue.text = "${hh} : ${mm} : ${ss}"
                }

                binding.runningPace.text = res.pace.toString()
            }
        } else {
            binding.tvNotRecord.visibility = View.VISIBLE
            binding.recordLayout.visibility = View.GONE
            binding.mypageMapView.visibility = View.GONE

            binding.tvNotRecord.text = response.message
        }

    }

    override fun onGetDailyRecordFailure(message: String) {
        TODO("Not yet implemented")
    }
}