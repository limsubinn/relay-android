package com.example.relay.group.service

import com.example.relay.BaseResponse
import com.example.relay.group.models.*
import com.example.relay.mypage.models.DailyRecordResponse
import com.example.relay.mypage.models.MonthRecordResponse
import retrofit2.Call
import retrofit2.http.*

interface GroupRetrofit {
    // 그룹 목록 조회 (전체, 검색)
    @GET("/clubs")
    fun getClubListRes(
        @Query("search") search: String
    ) : Call<GroupListResponse>

    // 그룹 생성
    @POST("/clubs")
    fun postNewClubReq(
        @Body clubInfo: GroupNewRequest
    ) : Call<BaseResponse>

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

    // 그룹에 가입 신청하기
    @POST("/clubs/member-status/{clubIdx}")
    fun postClubJoinIn(
        @Path("clubIdx") clubIdx: Long,
        @Body userInfo: GroupJoinInRequest
    ) : Call<BaseResponse>

    // 그룹 수정
    @PATCH("/clubs/{clubIdx}")
    fun patchClub(
        @Path("clubIdx") clubIdx: Long,
        @Body clubInfoReq: GroupEditRequest
    ) : Call<BaseResponse>

    // 그룹 삭제
    @PATCH("/clubs/{clubIdx}/deletion")
    fun patchClubDelete(
        @Path("clubIdx") clubIdx: Long
    ) : Call<BaseResponse>

    // 그룹 방장 위임
    @PATCH("/clubs/{clubIdx}/host-change")
    fun patchHost(
        @Path("clubIdx") clubIdx: Long,
        @Body hostReq: HostChangeRequest
    ) : Call<BaseResponse>

    // 그룹 멤버 강퇴
    @PATCH("/clubs/member-status/{clubIdx}/members/deletion")
    fun patchMember(
        @Path("clubIdx") clubIdx: Long,
        @Body patchDeleteMemberReq: MemberDeleteRequest
    ) : Call<BaseResponse>
}