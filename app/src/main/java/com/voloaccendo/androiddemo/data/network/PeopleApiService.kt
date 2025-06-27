package com.voloaccendo.androiddemo.data.network

import com.voloaccendo.androiddemo.data.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PeopleApiService {
    @GET("api/")
    suspend fun getPeople(
        @Query("results") count: Int
    ): Response<ApiResponse>
}
