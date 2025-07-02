package com.voloaccendo.androiddemo.data.network

import com.voloaccendo.androiddemo.data.models.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PeopleApiService {
    @GET("api/?nat=us&seed=abc")
    suspend fun getPeople(
        @Query("results") count: Int,
        @Query("page") page: Int = 1
    ): Response<ApiResponse>
}

