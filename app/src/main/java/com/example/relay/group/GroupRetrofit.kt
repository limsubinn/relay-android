package com.example.relay.group

import com.example.relay.group.models.GroupListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupRetrofit {
    // 그룹 목록 조회 (전체, 검색)
    @GET("/clubs")
    fun getClubListRes(
        @Query("search") search: String
    ) : Call<GroupListResponse>
}