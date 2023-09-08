package com.example.iverify.devices.data.network

import com.example.iverify.devices.models.responses.GetDevicesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface IInternetAPI {

    @GET("devices")
    suspend fun getDevices(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ) : GetDevicesResponse
}