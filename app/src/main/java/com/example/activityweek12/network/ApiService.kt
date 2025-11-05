package com.example.activityweek12.network

import com.example.activityweek12.data.ApiResponse
import com.example.activityweek12.data.Record
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    
    @GET("/api/records")
    suspend fun getRecords(): Response<List<Record>>
    
    @POST("/api/records")
    suspend fun addRecord(@Body record: Record): Response<ApiResponse>
}

