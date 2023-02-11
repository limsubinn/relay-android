package com.example.relay.group.service

import com.example.relay.group.models.*
import com.example.relay.mypage.models.DailyRecordResponse
import com.example.relay.mypage.models.MonthRecordResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupRetrofit {
    // 그룹 목록 조회 (전체, 검색)
    @GET("/clubs")
    fun getClubListRes(
        @Query("search") search: String
    ) : Call<GroupListResponse>

    // 속한 그룹의 이름 가져오기
    @GET("/users/clubs/accepted")
    fun getUserProfileClubRes(
        @Query("id") id: Long
    ) : Call<GroupAcceptedResponse>

    // 그룹 페이지 정보 조회
    @GET("/clubs/{clubIdx}")
    fun getClubDetailRes(
        @Path("clubIdx") clubIdx: Long,
        @Query("date") date: String
    ) : Call<GroupInfoResponse>

    // 그룹 멤버 관련 정보 조회
    @GET("/clubs/{clubIdx}/members")
    fun getClubMemberRes(
        @Path("clubIdx") clubIdx: Long,
        @Query("date") date: String
    ) : Call<MemberResponse>

    // 해당 그룹 일별 기록 조회
    @GET("/record/summary/club")
    fun getClubDailyRes(
        @Query("clubIdx") clubIdx: Long,
        @Query("date") date: String
    ) : Call<GroupDailyRecordResponse>

    // 해당 그룹 월별 기록 조회
    @GET("record/calender/club")
    fun getClubMonthRes(
        @Query("clubIdx") clubIdx: Long,
        @Query("year") year: Int,
        @Query("month") month: Int
    ) : Call<MonthRecordResponse>
}