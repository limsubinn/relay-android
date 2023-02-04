package com.example.relay.group

import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.group.models.GroupInfoResponse
import com.example.relay.group.models.GroupListResponse
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
}