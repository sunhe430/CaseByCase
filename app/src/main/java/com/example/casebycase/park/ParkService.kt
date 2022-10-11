package com.example.casebycase.park

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ParkService {
    @GET("/sensor/holder/")
    fun PS (
    ): Call<ParkResponse>
}